package ru.aemmie.cycle.overlay;

import com.google.common.eventbus.Subscribe;
import ru.aemmie.cycle.global.Utils;
import ru.aemmie.cycle.events.StateUpdated;
import ru.aemmie.cycle.objects.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.temporal.ChronoUnit;

class TimeBlock extends JPanel {
    private static final int TIME_WARNING_MS = 2700_000;
    private final JLabel timeLabel = new JLabel();
    private final Timer timer = new Timer(100, this::timerTask);
    private long gameStart;
    private long gameEnd;
    private GameMap map;

    public TimeBlock() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(RIGHT_ALIGNMENT);

        timeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        timeLabel.setForeground(Utils.ORANGE);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        this.add(timeLabel);

        this.setVisible(false);

        Utils.eventBus.register(this);
    }

    @Subscribe
    public void onStateUpdate(StateUpdated event) {
        var game = event.getGame();
        if (game == null) {
            timer.stop();
            this.setVisible(false);
        } else {
            this.map = game.map;
            this.gameStart = game.createdAt.toEpochMilli()  - game.map.timings.morning;
            this.gameEnd = game.createdAt.plus(6, ChronoUnit.HOURS).toEpochMilli();
            timer.setInitialDelay(0);
            timer.restart();
            this.setVisible(true);
        }
    }

    private void timerTask(ActionEvent event) {
        if (map == null) {
            return;
        }

        var time = event.getWhen() - gameStart;

        var timings = map.timings;
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
                format(toServerDeath, toServerDeath > TIME_WARNING_MS ? "#996666" : "red")
        ) + "</html>");
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

}
