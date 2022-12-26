package ru.aemmie.cycle.overlay.component;

import com.google.common.eventbus.Subscribe;
import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.events.state.EventCreatedEvent;
import ru.aemmie.cycle.events.state.GameStateUpdated;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class EventLogBlock extends JPanel {

//    private final Color color = new Color(0, 255, 0);
    private final Font font = new Font("Monospace", Font.BOLD, 20);


    public EventLogBlock() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(RIGHT_ALIGNMENT);


//        Color color = new Color(255, 128, 0, 255);

//        serverLabel.setFont(new Font("Serif", Font.BOLD, 20));
//        serverLabel.setForeground(color);
////        serverLabel.setOpaque(true);
////        serverLabel.setBackground(new Color(0, 0, 0, 0));
//        playersLabel.setFont(new Font("Serif", Font.BOLD, 60));
//        playersLabel.setForeground(color);
//        playersLabel.setOpaque(true);
//        playersLabel.setBackground(new Color(0, 0, 0, 0));
//        radarLabel.setFont(font);
//        radarLabel.setForeground(color);


//        this.add(serverLabel);
//        this.add(Utils.rightAlign(playersLabel));
//        this.add(Overlay.rightAlign(radarLabel));

//        this.revalidate();

        Utils.eventBus.register(this);
    }


    @Subscribe
    public void clearUI(GameStateUpdated event) {
        if (event.getGame().ended()) {
            this.removeAll();
        }
    }

    @Subscribe
    public void showEvent(EventCreatedEvent event) {

        Instant now = Instant.now();
        Instant time = event.getTime() != null ? event.getTime() : now;

        if (Duration.between(time, now).compareTo(event.getDuration()) > 0) {
            return;
        }

        var label = new JLabel(event.getMessage());
        label.setForeground(Color.GREEN);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        if (event.isNeedCountdown()) {
            var timeLabel = new JLabel();
            timeLabel.setForeground(Color.PINK);
            timeLabel.setFont(font);

            this.add(new LogMessage(label, time.plus(event.getDuration()), timeLabel));
        } else {
            this.add(new LogMessage(label, event.getDuration()));
        }

        this.revalidate();

    }


    private class LogMessage extends Box {

        private LogMessage(Component ... components) {
            super(BoxLayout.X_AXIS);
            this.add(Box.createHorizontalGlue());
            for (var c : components) {
                this.add(c);
            }
        }

        public LogMessage(JLabel component, Instant endTime, JLabel timeSection) {
            this(timeSection, component);

            component.setText( " - " + component.getText());

            var endMillis = endTime.toEpochMilli();

            Timer timer = new Timer(1000, e -> {
                long between = endMillis - e.getWhen();

                if (between < 0) {
                    EventLogBlock.this.remove(LogMessage.this);
                    EventLogBlock.this.revalidate();
                } else {
                    timeSection.setText(String.valueOf(between / 1000));
                }
            });
            timer.setInitialDelay(0);
            timer.start();
        }


        public LogMessage(JLabel component, Duration duration) {
            this(component);

            Timer timer = new Timer((int) duration.toMillis(), e -> {
                EventLogBlock.this.remove(LogMessage.this);
                EventLogBlock.this.revalidate();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}
