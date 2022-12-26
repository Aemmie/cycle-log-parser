package ru.aemmie.cycle.processors.ingame;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.basic.GameProcessor;
import ru.aemmie.cycle.events.parser.server.PartySizeSetEvent;
import ru.aemmie.cycle.events.parser.server.ServerTimeSetEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static ru.aemmie.cycle.Utils.prettyTime;
import static ru.aemmie.cycle.objects.State.game;

@Slf4j
public class ServerProcessor implements GameProcessor {


    @Subscribe
    public void time(ServerTimeSetEvent event) {
        game.createdAt(event.getCreatedAt());
//        if (game.isNotHub()) {
//            log.info("=".repeat(70));
            log.info("Instance id: " + game.instanceId());
            log.info("Created: %s (%s)".formatted(prettyTime.format(prettyTime.calculatePreciseDuration(game.createdAt())), game.enteredAt()));
            Instant deadTime = game.createdAt().plus(6, ChronoUnit.HOURS);
            log.info("Dead in: %s (%s)".formatted(prettyTime.format(prettyTime.calculatePreciseDuration(deadTime)), deadTime));
            log.info("=".repeat(70));
//        }
    }

    @Subscribe
    public void partySize(PartySizeSetEvent event) {
        game.partySize(event.getSize());
    }
}
