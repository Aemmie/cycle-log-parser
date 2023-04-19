package ru.aemmie.cycle.parsers;

import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.global.Utils;
import ru.aemmie.cycle.events.log.TextCountdownVisibleEvent;
import ru.aemmie.cycle.global.StateHolder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
class ActivitiesParser implements Parser {

    @Override
    public void parse(Instant time, String type, String text) {
        if (!"LogYActivities".equals(type) || !StateHolder.isInGame()) {
            return;
        }

        if (text.startsWith("Warning: AC_EvacShip_BP")) {
            log.info("Evac ship called");
            Utils.eventBus.post(new TextCountdownVisibleEvent(time, Duration.of(86, ChronoUnit.SECONDS), "evac ship [called]") {

                private boolean flying = true;

                @Override
                public void onTimerTick(ActionEvent e) {
                    long now = e.getWhen();
                    long diff = now - time.toEpochMilli();
                    if (diff < 76_000) {
                        countdown.setText(String.valueOf((76_000 - diff) / 1000));
                        if (flying && diff > 46_000) {
                            flying = false;
                            label.setText("evac ship [landed]");
                        }
                    } else {
                        countdown.setText(String.valueOf((endTime.toEpochMilli() - now) / 1000));
                        if (!flying) {
                            flying = true;
                            label.setText("evac ship [flying]");
                            label.setForeground(Color.LIGHT_GRAY);
                        }
                    }
                }
            });
        } else if (text.startsWith("Warning: AA_MeteorShowerSpawner")) {
            log.info("Meteors event");
            Utils.eventBus.post(new TextCountdownVisibleEvent(time, Duration.of(45, ChronoUnit.SECONDS), "Meteors event!"));
        }
    }
}
