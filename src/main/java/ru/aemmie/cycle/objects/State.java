package ru.aemmie.cycle.objects;

import ru.aemmie.cycle.overlay.Overlay;

public class State {

    public static Overlay overlay;

    public static Game game = new Game("initial").ended(true);
    public static String lastInstanceId;
}
