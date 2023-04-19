package ru.aemmie.cycle.overlay;

import com.google.common.eventbus.Subscribe;
import ru.aemmie.cycle.global.Utils;
import ru.aemmie.cycle.events.PlayerCountUpdated;
import ru.aemmie.cycle.events.StateUpdated;
import ru.aemmie.cycle.global.StateHolder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class ServerBlock extends JPanel {

    private final JLabel serverLabel = new JLabel();
    private final JLabel playersLabel = new JLabel();
    public ServerBlock() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(RIGHT_ALIGNMENT);

        serverLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        serverLabel.setForeground(Utils.ORANGE);
        serverLabel.setBorder(new EmptyBorder(0, 0, 0, 30));
        playersLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 60));
        playersLabel.setForeground(Utils.ORANGE);

        this.add(serverLabel);
        this.add(playersLabel);

        this.setVisible(false);

        Utils.eventBus.register(this);
    }

    @Subscribe
    public void onStateUpdate(StateUpdated event) {
        var game = event.getGame();
        if (game == null) {
            this.setVisible(false);
        } else {
            serverLabel.setText(game.name);
            this.setVisible(true);
        }
    }

    @Subscribe
    private void onPlayerCountUpdate(PlayerCountUpdated event) {
        var game = StateHolder.getCurrentGame();
        playersLabel.setText("%d/%d".formatted(game.radar, game.players));
    }

}
