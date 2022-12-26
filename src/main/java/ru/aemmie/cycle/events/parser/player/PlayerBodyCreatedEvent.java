package ru.aemmie.cycle.events.parser.player;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.events.parser.ParserEvent;
import ru.aemmie.cycle.objects.GameMap;

@Getter
@SuperBuilder
public class PlayerBodyCreatedEvent extends ParserEvent {
    private final String stateId;
    private final String characterId;
    private final String role;
    private final GameMap map;
}
