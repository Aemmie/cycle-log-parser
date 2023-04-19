package ru.aemmie.cycle.global;

import ru.aemmie.cycle.events.StateUpdated;
import ru.aemmie.cycle.objects.Game;

import java.util.*;

public class StateHolder {
    private static final LinkedList<Game> games = new LinkedList<>();

    private static boolean inGame = false;

    public static void leaveGame() {
        inGame = false;
        if (!games.isEmpty()) {
            games.peekFirst().clear();
        }
        Utils.eventBus.post(new StateUpdated(null));
    }

    public static void setGame(Game game) {
        inGame = true;
        if (!games.isEmpty()) {
            games.peekFirst().clear();
        }
        games.push(games.stream().filter(g -> g.instanceId.equals(game.instanceId)).findAny().orElse(game));
        Utils.eventBus.post(new StateUpdated(games.peek()));
    }

    public static Game getCurrentGame() {
        return games.peek();
    }

    public static boolean isInGame() {
        return inGame;
    }
}
