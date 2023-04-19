package ru.aemmie.cycle.overlay;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.events.StateUpdated;
import ru.aemmie.cycle.events.log.VisibleEvent;
import ru.aemmie.cycle.global.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

@Slf4j
class EventLogBlock extends JPanel {

    private final Font font = new Font("Monospace", Font.BOLD, 20);
    private final Timer timer = new Timer(100, null);

    public EventLogBlock() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(RIGHT_ALIGNMENT);

        Utils.eventBus.register(this);
    }

    @Subscribe
    public void onStateUpdate(StateUpdated event) {
        var game = event.getGame();
        if (game == null) {
            this.removeAll();
            timer.stop();
        } else {
            timer.start();
        }
    }

    @Subscribe
    public void showEvent(VisibleEvent event) {
        Instant now = Instant.now();
        if (now.compareTo(event.endTime) > 0) {
            return;
        }
        long endTime = event.endTime.toEpochMilli();

        event.setFont(font);
        event.setAlignmentX(RIGHT_ALIGNMENT);
        this.add(event);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (endTime - e.getWhen() < 0) {
                    EventLogBlock.this.remove(event);
//                    EventLogBlock.this.revalidate();
                timer.removeActionListener(this);
                } else {
                    event.onTimerTick(e);
                }
            }
        });

        this.revalidate();
    }

}
