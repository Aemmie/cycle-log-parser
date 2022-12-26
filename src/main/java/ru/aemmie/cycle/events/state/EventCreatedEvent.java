package ru.aemmie.cycle.events.state;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.Instant;

@Getter
@SuperBuilder
public class EventCreatedEvent extends StateEvent {
    private final Instant time;
    private final Duration duration;
    private final String message;
    @Builder.Default
    private final boolean needCountdown = false;
}
