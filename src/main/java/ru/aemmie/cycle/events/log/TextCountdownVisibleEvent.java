package ru.aemmie.cycle.events.log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.Instant;

public class TextCountdownVisibleEvent extends TextVisibleEvent {

    protected final JLabel countdown;

    public TextCountdownVisibleEvent(Instant time, Duration duration, String message) {
        super(time, duration, message);
        countdown = new JLabel();
        countdown.setForeground(Color.PINK);
        countdown.setBorder(new EmptyBorder(0, 0, 0, 7));
        this.add(countdown, 0);
    }

    @Override
    public void onTimerTick(ActionEvent e) {
        long between = endTime.toEpochMilli() - e.getWhen();
        countdown.setText(String.valueOf(between / 1000));
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        countdown.setFont(font);
    }
}
