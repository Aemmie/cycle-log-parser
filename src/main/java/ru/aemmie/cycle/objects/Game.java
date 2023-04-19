package ru.aemmie.cycle.objects;

import com.github.javafaker.Faker;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game {
    public final String instanceId;
    public final String region;
    public final String name;
    public final GameMap map;
    public final Instant createdAt;
    public final int partySize;

    public final Map<String, Integer> killCount = new HashMap<>();
    public int players = 0;
    public int radar = 0;

    @Builder
    public Game(String instanceId, String region, GameMap map, Instant createdAt, int partySize) {
        this.instanceId = instanceId;
        this.region = region;
        this.map = map;
        this.createdAt = createdAt;
        this.partySize = partySize;

        long seed = Long.parseLong(StringUtils.substringAfterLast(instanceId, '-'), 16);
        Faker faker = new Faker(new Random(seed));
        this.name = faker.color().name() + " " + faker.animal().name();
    }

    public Game clear() {
        killCount.clear();
        players = 0;
        radar = 0;
        return this;
    }

    public int kill(String id) {
        return killCount.compute(id, (k, v) -> (v == null ? 0 : v) + 1);
    }

}
