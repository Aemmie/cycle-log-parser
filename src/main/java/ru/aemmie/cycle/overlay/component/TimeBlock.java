package ru.aemmie.cycle.overlay.component;

import com.google.common.eventbus.Subscribe;
import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.events.state.GameStateUpdated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TimeBlock extends JPanel {
    private final JLabel timeLabel = new JLabel();
//    private final JLabel deathLabel = new JLabel();
    private long gameStart;
    private long gameEnd;
    private Timings timings;
    private final Timer timer = new Timer(1000, this::timerTask);

    public TimeBlock() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(RIGHT_ALIGNMENT);

        timeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        timeLabel.setForeground(new Color(255, 128, 0));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        this.add(Utils.rightAlign(timeLabel));

        this.revalidate();

        Utils.eventBus.register(this);
    }


    private void timerTask(ActionEvent event) {

        var time = event.getWhen() - gameStart;

        time %= timings.timeBetweenStorms;

        var target = 0;
        var toMorning = calculateDiff(time, target, timings.timeBetweenStorms);
        var toDay = calculateDiff(time, target += timings.morning, timings.timeBetweenStorms);
        var toEvening = calculateDiff(time, target += timings.day, timings.timeBetweenStorms);
        var toNight = calculateDiff(time, target += timings.evening, timings.timeBetweenStorms);

        long toServerDeath = gameEnd - event.getWhen();
        timeLabel.setText("<html>" + String.join(" <font color='gray'>/</font> ",
                format(toMorning, "#00CCFF"),
                format(toDay, "#FFFF00"),
                format(toEvening, "#FFEFD5"),
                format(toNight, "#FF0099"),
                format(toServerDeath, toServerDeath > 2700_000 ? "#996666" : "red")
        ) + "</html>");
//        deathLabel.setText(format(gameEnd - event.getWhen()));
    }

    private long calculateDiff(long now, long target, long cycle) {
        var diff = target - now;
        if (diff > 0) {
            return diff;
        } else {
            return cycle + diff;
        }
    }

    private String format(long delay, String color) {
        var minutes = delay / 60_000;
        var seconds = (delay % 60_000) / 1000;
        return "<font color='%s'>%d:%02d</font>".formatted(color, minutes, seconds);
    }

    @Subscribe
    public void refreshUI(GameStateUpdated event) {
        var game = event.getGame();
        if (game.createdAt() != null && game.map() != null) {

            this.timings = switch (game.map()) {
                case BRIGHT_SANDS, CRESCENT_FALLS -> new Timings(
                        duration(4, 0),
                        duration(16, 40),
                        duration(13, 20),
                        duration(4, 40));

                // time between storms on 3rd map is shorter
                case THARIS_ISLAND -> new Timings(
                        duration(4, 0),
                        duration(12, 40),
                        duration(8, 20),
                        duration(4, 40));
            };

            this.gameStart = game.createdAt().toEpochMilli()  - timings.morning + 3000;
            this.gameEnd = game.createdAt().plus(6, ChronoUnit.HOURS).toEpochMilli();

            if (!timer.isRunning()) {
                timer.start();
            }
        }
    }

    private long duration(long minutes, long seconds) {
        return Duration.ofMinutes(minutes).plusSeconds(seconds).toMillis();
    }

    private static final class Timings {
        public final long timeBetweenStorms;
        public final long morning;
        public final long day;
        public final long evening;
        public final long night;

        private Timings(long morning, long day, long evening, long night) {
            this.timeBetweenStorms = morning + day + evening + night;
            this.morning = morning;
            this.day = day;
            this.evening = evening;
            this.night = night;
        }

    }
}
