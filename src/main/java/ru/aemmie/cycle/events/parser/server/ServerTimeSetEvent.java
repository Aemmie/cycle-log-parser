package ru.aemmie.cycle.events.parser.server;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;

import java.time.Instant;

@Getter
@SuperBuilder
public class ServerTimeSetEvent extends ParserEvent {
    private final Instant createdAt;
    private final int secondsSinceStart;
}
