package ru.aemmie.cycle.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.events.state.GameStateUpdated;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Accessors(fluent = true)
public class Game {
    private final String instanceId;
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private GameMap map = GameMap.BRIGHT_SANDS;
    private int partySize = 1;
    private int playerCount = 0;
    private boolean evacuated = false;

    private Instant createdAt;
    private Instant enteredAt;

    private boolean ended;

    public Game(String instanceId) {
        this.instanceId = instanceId;
    }

    public Instant getInstanceDeathTime() {
        return createdAt == null ? null : createdAt.plus(6, ChronoUnit.HOURS);
    }

    public Game plusPlayer() {
        playerCount += 1;
        return this;
    }

    public Game minusPlayer() {
        playerCount -= 1;
        return this;
    }

    public Optional<Player> findPlayerByCharacterId(String characterId) {
        return players.values().stream()
                .filter(v -> v.characterIds().contains(characterId))
                .findAny();
    }

    public void updateUI() {
        Utils.eventBus.post(GameStateUpdated.builder().game(this).build());
    }

}
