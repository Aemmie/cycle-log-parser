package ru.aemmie.cycle.events.parser.player;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;

@Getter
@SuperBuilder
public class PlayerStateEvent extends ParserEvent {
    private final PlayerState state;

    public enum PlayerState {
        IN_MATCH,
        FINISHED,
    }
}


