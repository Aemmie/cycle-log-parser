package ru.aemmie.cycle.events.log;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class TextVisibleEvent extends VisibleEvent {

    protected final JLabel label;
    public TextVisibleEvent(Instant time, Duration duration, String message) {
        super(time, duration);
        this.label = new JLabel(message);
        label.setForeground(Color.GREEN);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        this.add(label);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        label.setFont(font);
    }
}
