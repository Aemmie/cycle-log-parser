package ru.aemmie.cycle.events.state;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.aemmie.cycle.objects.Game;

@Getter
@SuperBuilder
public class GameStateUpdated extends StateEvent {
    private final Game game;
}
