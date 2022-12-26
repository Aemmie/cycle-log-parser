package ru.aemmie.cycle.events.parser.player;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;

@Getter
@SuperBuilder
public class PlayerFinishedEvent extends ParserEvent {

    private final PlayerState state;
    private final String causer;
    private final Double damage;
    private final String origin;

    public enum PlayerState {
        ESCAPED,
        DEAD,
    }
}
