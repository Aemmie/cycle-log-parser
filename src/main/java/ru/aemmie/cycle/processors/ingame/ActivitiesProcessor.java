package ru.aemmie.cycle.processors.ingame;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.basic.GameProcessor;
import ru.aemmie.cycle.events.parser.event.EvacShipCalledEvent;
import ru.aemmie.cycle.events.parser.event.MeteorsEvent;
import ru.aemmie.cycle.events.state.EventCreatedEvent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
public class ActivitiesProcessor implements GameProcessor {
    @Subscribe
    private void processEvacShip(EvacShipCalledEvent event) {
        log.info("Evac ship called");
        sendEvent(EventCreatedEvent.builder()
                .time(event.getTime())
//                .time(Instant.now())
                .message("Evac ship called")
                .duration(Duration.of(76, ChronoUnit.SECONDS))
                .needCountdown(true)
                .build());
    }

    @Subscribe
    private void processMeteors(MeteorsEvent event) {
        log.info("Meteor shower");
        sendEvent(EventCreatedEvent.builder()
                .time(event.getTime())
                .message("Meteors")
                .duration(Duration.of(10, ChronoUnit.SECONDS))
                .build());
    }
}
