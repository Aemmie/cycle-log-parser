package ru.aemmie.cycle.events.parser.server;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;

@Getter
@SuperBuilder
public class ServerChangedEvent extends ParserEvent {
    private final String serverId;
    private final boolean isHub;
}
