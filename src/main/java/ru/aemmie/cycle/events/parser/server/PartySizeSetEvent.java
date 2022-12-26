package ru.aemmie.cycle.events.parser.server;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;

@Getter
@SuperBuilder
public class PartySizeSetEvent extends ParserEvent {
    private final int size;
}
