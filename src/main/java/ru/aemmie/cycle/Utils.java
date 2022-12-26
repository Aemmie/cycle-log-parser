package ru.aemmie.cycle;

import com.google.common.eventbus.EventBus;
import lombok.SneakyThrows;
import org.ocpsoft.prettytime.PrettyTime;
import ru.aemmie.cycle.win.Kernel32;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Utils {

    public static final EventBus eventBus = new EventBus();

    public static final PrettyTime prettyTime = new PrettyTime(Locale.US);
    public static final String PLAYER_PREFIX = "PRO_PlayerCharacter_C_";
    public static final String AI_CHAR_PREFIX = "AIChar_";

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    @SneakyThrows
    public static <T> T getInstance(Class<T> cl) {
        return cl.getConstructor().newInstance();
    }

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
