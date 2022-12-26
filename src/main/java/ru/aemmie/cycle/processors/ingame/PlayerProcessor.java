package ru.aemmie.cycle.processors.ingame;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.basic.GameProcessor;
import ru.aemmie.cycle.events.parser.player.*;
import ru.aemmie.cycle.events.state.EventCreatedEvent;
import ru.aemmie.cycle.objects.Player;
import ru.aemmie.cycle.objects.Weapons;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static ru.aemmie.cycle.Utils.*;
import static ru.aemmie.cycle.objects.State.game;
import static ru.aemmie.cycle.objects.Weapons.UNKNOWN;

@Slf4j
public class PlayerProcessor implements GameProcessor {

    private boolean lastFinished = false;

    @Subscribe
    public void processState(PlayerStateEvent event) {
        switch (event.getState()) {
            case IN_MATCH -> {
                game.plusPlayer();
                if (game.playerCount() > game.partySize()) {
                    beep(2000, 250, event.getTime());
                }
                lastFinished = false;
            }
            case FINISHED -> {
                game.minusPlayer();
                if (game.playerCount() > game.partySize()) {
                    beep(400, 150, event.getTime());
                }
                lastFinished = true;
            }
        }

        game.updateUI();

    }

    @Subscribe
    public void processFinished(PlayerFinishedEvent event) {
        var builder = EventCreatedEvent.builder()
                .time(event.getTime())
                .duration(Duration.of(10, ChronoUnit.SECONDS));
        if (event.getState() == PlayerFinishedEvent.PlayerState.ESCAPED) {
            builder.message("Player escaped");
        } else {
            var damage = String.format(Locale.ROOT, "%.2f", event.getDamage());
            String from;
            if (startsWith(event.getCauser(), PLAYER_PREFIX)) {
                if ("None".equals(event.getOrigin())) {
                    if (event.getDamage() == 150f) {
                        from = Weapons.SUICIDE.toColorfulString();
                    } else {
                        from = Weapons.HEIGHT.toColorfulString();
                    }
                } else {
                    var weapon = Weapons.parse(event.getOrigin());
                    from = weapon == UNKNOWN ? event.getOrigin() : weapon.toColorfulString();
                }
            } else if (startsWith(event.getCauser(), AI_CHAR_PREFIX)) {
                var aiName = substringBetween(event.getCauser(), AI_CHAR_PREFIX, "_");
                var ai = Weapons.parse(aiName);
                from = ai == UNKNOWN ? aiName : ai.toColorfulString();
            } else {
                from = event.getCauser();
            }
            builder.message("<html>Player dead (%s: %s)</html>".formatted(from, damage));
        }
        sendEvent(builder.build());

        if (event.getState() == PlayerFinishedEvent.PlayerState.ESCAPED) {
            log.info("Player evacuated");
        } else {
            var damage = String.format(Locale.ROOT, "%.2f", event.getDamage());
            log.info("Player dead, causer: %s, last damage: %s, origin: %s".formatted(event.getCauser(), damage, event.getOrigin()));
        }
    }


    @Subscribe
    public void processBodyCreated(PlayerBodyCreatedEvent event) {
        game.players().computeIfAbsent(event.getStateId(), id ->
                        new Player(id, switch (event.getRole()) {
                            case "1" -> Player.Role.DEFAULT;
                            case "2", "3" -> Player.Role.ME;
                            default -> Player.Role.DEFAULT;
                        }))
                .state(Player.State.ACTIVE)
                .characterIds().add(event.getCharacterId());
        if ("2".equals(event.getRole())) {
            game.map(event.getMap());
        }
        game.updateUI();
    }

    @Subscribe
    public void processBodyDestroyed(PlayerBodyDestroyedEvent event) {
        game.findPlayerByCharacterId(event.getCharacterId())
                .map(p -> p.state(Player.State.INACTIVE));
        game.updateUI();
    }


    // fix situation when players wasn't loaded when finishing game (on server loading)
    @Subscribe
    public void failed(GameStateFailed event) {
        if (lastFinished) {
            game.plusPlayer();
            log.info("Player finished before loading, revert player count.");
            log.info("Total player count: " + game.playerCount());
        }
    }

}
