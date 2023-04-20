package ru.aemmie.cycle.global;

import com.google.common.eventbus.EventBus;
import ru.aemmie.cycle.win.Kernel32;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Utils {

    public static final EventBus eventBus = new EventBus();
    public static final Color ORANGE = new Color(255, 128, 0);

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void beep(int freq, int duration) {
        Kernel32.INSTANCE.Beep(freq, duration);
    }

    public static void beep(int freq, int duration, Instant time) {
        if (SECONDS.between(time, Instant.now()) < 60) {
            scheduler.submit(() -> beep(freq, duration));
        }
    }

    public static Box rightAlign(Component component) {
        var box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(component);
        return box;
    }
}
