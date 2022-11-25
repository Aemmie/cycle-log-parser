package ru.aemmie.cycle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.ocpsoft.prettytime.PrettyTime;
import ru.aemmie.cycle.win.Kernel32;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBetween;

@Slf4j
class Listener implements TailerListener {

    private final String PLAYER_PREFIX = "PRO_PlayerCharacter_C_";
    private final PrettyTime prettyTime = new PrettyTime(Locale.US);
    private Game game = new Game("initial", true);

    private String lastInstanceId;

    @Override
    public void handle(String string) {
        if (!string.startsWith("[202")) {
            return;
        }

        var line = new Line(string);

        switch (line.getType()) {
            case "LogYTravel" -> {
                if (line.startsWith("UYControllerTravelComponent::TravelToServer")) {
                    var serverId = substringBetween(line.getMsg(), "sessionId [", "]");
                    var gameType = substringBetween(line.getMsg(), "m_isMatch [", "]");
                    var isHub = "0".equals(gameType);
                    game = new Game(serverId, isHub);
                } else if (line.startsWith("Forcing transition to match")) {
                    game.setPartySize(Integer.parseInt(substringBetween(line.getMsg(), "SquadSize=", "?")));
                }
            }
            case "LogHandshake" -> {
                if (line.startsWith("SendChallengeResponse")) {
                    int secondsSinceStart = Integer.parseInt(substringBetween(line.getMsg(), "Timestamp: ", "."));
                    game.setStartTime(line.getTime().minus(secondsSinceStart, ChronoUnit.SECONDS));
                    if (game.isNotHub()) {
                        log.info("=".repeat(70));
                        log.info("Instance id: " + game.getId() + (game.getId().equals(lastInstanceId) ? " (same as last game)" : ""));
                        log.info("Created: %s (%s)".formatted(prettyTime.format(game.getStartTime()), game.getStartTime()));
                        log.info("=".repeat(70));
                        lastInstanceId = game.getId();
                    }
                }
            }
            case "LogYCustomization" -> {
                if (line.startsWith("UYCharacterCustomizationComponent::BeginPlay")) {
                    var playerId = substringBetween(line.getMsg(), PLAYER_PREFIX, "]");
                    var playerRole = substringBetween(line.getMsg(), "Role ", ".");
                    if ("2".equals(playerRole)) {
                        game.setMyId(playerId);
                    }
                    game.getPlayers().add(playerId);
                    if (game.isNotHub()) {
                        log.info("+++" + formatPlayersChange(playerId));
                        if (game.getPlayers().size() > game.getPartySize()) {
                            beep(2000, 250, line.getTime());
                        }
                    }
                }
            }
            case "LogYPlayer" -> {
                if (line.startsWith("AYPlayerCharacter::Destroyed()")) {
                    var playerId = substringAfter(line.getMsg(), PLAYER_PREFIX);
                    if (game.getPlayers().remove(playerId) && game.isNotHub()) {
                        log.info("---" + formatPlayersChange(playerId));
                        if (playerId.equals(game.getMyId())) {
                            game.setEvacuated(true);
                        } else if (!game.isEvacuated()) {
                            if (game.getPlayers().size() >= game.getPartySize()) {
                                beep(400, 150, line.getTime());
                            }
                        }
                    }
                }
            }
            case "LogYActivities" -> {
                if (line.startsWith("Warning: AC_EvacShip_BP")) {
                    log.info("Evac ship called");
                }
            }
            default -> {

            }
        }
//        log.info(line);
    }

    private String formatPlayersChange(String id) {
        return "player (%2d): %s".formatted(game.getPlayers().size(), id.substring(id.length() - 3))
                + (id.equals(game.getMyId()) ? " (self)" : "");
    }

    private void beep(int freq, int duration) {
        Kernel32.INSTANCE.Beep(freq, duration);
    }

    private void beep(int freq, int duration, Instant time) {
        if (ChronoUnit.SECONDS.between(time, Instant.now()) < 60) {
            beep(freq, duration);
        }
    }

    @Override
    public void init(Tailer tailer) {
    }

    @Override
    public void fileNotFound() {
        log.error("Log file not found");
    }

    @Override
    public void fileRotated() {
        log.info("File rotated");
    }

    @Override
    public void handle(Exception ex) {
        log.error("Exception? Meh.", ex);
    }
}
