package ru.aemmie.cycle.events.log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.Instant;

public abstract class VisibleEvent extends Box {

    public final Instant time;
    public final Duration duration;
    public final Instant endTime;

    public VisibleEvent(Instant time, Duration duration) {
        super(BoxLayout.X_AXIS);
        this.time = time;
        this.duration = duration;
        this.endTime = time.plus(duration);
    }

    public void onTimerTick(ActionEvent e) {

    }
}
