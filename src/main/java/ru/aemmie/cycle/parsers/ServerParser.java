package ru.aemmie.cycle.parsers;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.global.StateHolder;
import ru.aemmie.cycle.objects.Game;
import ru.aemmie.cycle.objects.GameMap;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.StringUtils.substringBetween;

@Slf4j
class ServerParser implements Parser {

    private Game.GameBuilder builder;

    @Override
    public void parse(Instant time, String type, String text) {
        switch (type) {
            case "LogYTravel" -> {
                if (text.startsWith("UYControllerTravelComponent::TravelToServer")) {
                    if ("0".equals(substringBetween(text, "m_isMatch [", "]"))) {
                        StateHolder.leaveGame();
                    } else {
                        builder = Game.builder()
                                .instanceId(substringBetween(text, "sessionId [", "]"))
                                .region(substringBetween(text, "region [", "]"));
                    }
                } else if (builder != null && text.startsWith("Forcing transition to match")) {
                    String size = substringBetween(text, "SquadSize=", "?");
                    builder.partySize(size == null ? 1 : Integer.parseInt(size));
                }
            }
            case "LogHandshake" -> {
                if (builder != null && text.startsWith("SendChallengeResponse")) {
                    int secondsSinceStart = Integer.parseInt(substringBetween(text, "Timestamp: ", ".")) - 5;
                    builder.createdAt(time.minus(secondsSinceStart, ChronoUnit.SECONDS));
                }
            }
            case "LogNet" -> {
                if (builder != null && text.startsWith("Welcomed by server")) {
                    builder.map(parseMap(substringBetween(text, "/Game/Maps/MP/", "/")));
                    var game = builder.build();
                    builder = null;

                    log.info(Strings.repeat("=", 30));
                    log.info("New instance: " + game.name);
                    log.info(Strings.repeat("=", 30));

                    StateHolder.setGame(game);
                }
            }
        }
    }

    private GameMap parseMap(String map) {
        if (map == null) {
            return null;
        }
        return switch (map) {
            case "MAP01" -> GameMap.BRIGHT_SANDS;
            case "MAP02" -> GameMap.CRESCENT_FALLS;
            case "AlienCaverns" -> GameMap.THARIS_ISLAND;
            default -> null;
        };
    }
}
