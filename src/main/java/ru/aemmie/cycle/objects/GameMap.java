package ru.aemmie.cycle.objects;

public enum GameMap {
    BRIGHT_SANDS,
    CRESCENT_FALLS,
    THARIS_ISLAND;

    public static GameMap parse(String map) {
        if (map == null) {
            return null;
        }
        return switch(map) {
            case "MAP01" -> BRIGHT_SANDS;
            case "MAP02" -> CRESCENT_FALLS;
            case "AlienCaverns" -> THARIS_ISLAND;
            default -> null;
        };
    }
}
