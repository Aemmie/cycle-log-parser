package ru.aemmie.cycle.events.parser;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.basic.Event;

import java.time.Instant;

@Getter
@SuperBuilder
public class ParserEvent extends Event {
    private final Instant time;
}
