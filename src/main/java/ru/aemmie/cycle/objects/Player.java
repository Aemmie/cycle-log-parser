package ru.aemmie.cycle.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Accessors(fluent = true)
public class Player {
    private String stateId;
    private final Set<String> characterIds = ConcurrentHashMap.newKeySet();
    private final Set<String> weapons = ConcurrentHashMap.newKeySet();
    private final Role role;
    private State state = State.ACTIVE;

    public Player(String stateId) {
        this.stateId = stateId;
        this.role = Role.DEFAULT;
    }

    public Player(String stateId, Role role) {
        this.stateId = stateId;
        this.role = role;
    }

    public enum Role {
        ME,
        TEAMMATE,
        DEFAULT
    }

    public enum State {
        ACTIVE,
        INACTIVE,
        EVACUATED,
        DEAD
    }
}
