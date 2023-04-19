package ru.aemmie.cycle.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aemmie.cycle.objects.Game;

@Getter
@AllArgsConstructor
public class StateUpdated {
    private final Game game;
}
