package ru.aemmie.cycle.parsers;

import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.events.PlayerCountUpdated;
import ru.aemmie.cycle.events.log.TextVisibleEvent;
import ru.aemmie.cycle.global.StateHolder;
import ru.aemmie.cycle.global.Utils;
import ru.aemmie.cycle.objects.Weapon;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.StringUtils.substringBetween;
import static ru.aemmie.cycle.global.Utils.beep;

@Slf4j
class PlayerParser implements Parser {

    private boolean lastFinished = false;

    @Override
    public void parse(Instant time, String type, String text) {
        if (!StateHolder.isInGame()) {
            return;
        }

        switch (type) {
            case "LogYPlayer" -> {
                if (text.startsWith("OnRep_PlayerMatchState")) {
                    var state = substringBetween(text, "[", "]");
                    var game = StateHolder.getCurrentGame();
                    if ("inMatch".equals(state)) {
                        game.players++;
                        if (game.players > game.partySize) {
                            beep(2000, 250, time);
                        }
                        lastFinished = false;
                    } else {
                        game.players--;
                        if (game.players > game.partySize) {
                            beep(400, 150, time);
                        }
                        lastFinished = true;
                    }
                    Utils.eventBus.post(new PlayerCountUpdated());
                } else if (text.startsWith("OnPlayerStateChanged")) {
                    StateHolder.getCurrentGame().radar++;
                    Utils.eventBus.post(new PlayerCountUpdated());
                } else if (text.startsWith("AYPlayerCharacter::Destroyed()")) {
                    StateHolder.getCurrentGame().radar--;
                    Utils.eventBus.post(new PlayerCountUpdated());
                } else if (text.startsWith("AYPlayerState::OnRep_PlayerMatchFinishedResult")) {
                    var result = substringBetween(text, "Result:", " ");
                    switch (result) {
                        case "escaped" -> {
                            log.info("Player escaped");
                            Utils.eventBus.post(new TextVisibleEvent(time, Duration.of(15, ChronoUnit.SECONDS),
                                    "Player escaped"));
                        }
                        case "Dead" -> {
                            var causerParts = substringBetween(text, "Damage:Causer:", " ").split("_C_", 2);
                            String causerString = causerParts[0];
                            var causer = Weapon.parse(causerString);
                            var originString = substringBetween(text, "Origin:OriginRow:[", "]");
                            var origin = Weapon.parse(originString);
                            var damage = "%.2f".formatted(Double.parseDouble(substringBetween(text, "m_healthDamage:", " ")));
                            int times = 0;
                            Weapon weapon = causer == null ? null : switch (causer) {
                                case NONE -> Weapon.SUICIDE;
                                case PLAYER -> {
                                    if (origin == Weapon.NONE) {
                                        yield Weapon.HEIGHT;
                                    }
                                    times = StateHolder.getCurrentGame().kill(causerParts[1]);
                                    yield origin;
                                }
                                default -> causer;
                            };
                            log.info("Player killed: %s: %s".formatted(weapon, damage));
                            if (weapon == null) {
                                log.error(text);
                            }
                            Utils.eventBus.post(new TextVisibleEvent(time, Duration.of(15, ChronoUnit.SECONDS),
                                    "<html>Player dead (%s: %s)%s</html>".formatted(weapon == null ? originString : weapon.toColorfulString(), damage, times > 1 ? " [x%d]".formatted(times) : "")));
                        }
                        default -> {
                            log.error("Unknown result:\n" + text);
                        }
                    }
                    Utils.eventBus.post(new PlayerCountUpdated());
                }
            }
            case "LogYInventory" -> {
                if (lastFinished && text.startsWith("GetInventoryComponentManager | Could not retrieve YGameStateMatch!")) {
                    StateHolder.getCurrentGame().players++;
                    lastFinished = false;
                    log.info("Player finished before loading, revert player count.");
                    Utils.eventBus.post(new PlayerCountUpdated());
                }
            }
        }
    }
}
