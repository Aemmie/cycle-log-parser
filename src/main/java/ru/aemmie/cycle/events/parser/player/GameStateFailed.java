package ru.aemmie.cycle.events.parser.player;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;

@Getter
@SuperBuilder
public class GameStateFailed extends ParserEvent {
}
