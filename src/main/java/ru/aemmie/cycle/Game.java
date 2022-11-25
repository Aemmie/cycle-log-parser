package ru.aemmie.cycle;

import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class Game {
    private final String id;
    private final boolean hub;
    private final Set<String> players = new HashSet<>();
    private String myId;
    private boolean evacuated = false;
    private int partySize = 1;
    private Instant startTime;

    public boolean isNotHub() {
        return !hub;
    }
}
