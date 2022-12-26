package ru.aemmie.cycle.overlay.component;

import com.google.common.eventbus.Subscribe;
import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.events.state.GameStateUpdated;
import ru.aemmie.cycle.objects.Player;
import ru.aemmie.cycle.objects.State;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ServerBlock extends JPanel {

    private final JLabel serverLabel = new JLabel();
    private final JLabel playersLabel = new JLabel();
    private final Color orange = new Color(255, 128, 0);

    public ServerBlock() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(RIGHT_ALIGNMENT);

        serverLabel.setFont(new Font("Serif", Font.BOLD, 20));
        serverLabel.setForeground(orange);
        playersLabel.setFont(new Font("Serif", Font.BOLD, 60));
        playersLabel.setForeground(orange);

        this.add(Utils.rightAlign(serverLabel));
        this.add(Utils.rightAlign(playersLabel));

        this.revalidate();

        Utils.eventBus.register(this);
    }

    @Subscribe
    public void refreshUI(GameStateUpdated event) {
        var game = event.getGame();
        serverLabel.setText(game.instanceId());
        serverLabel.setForeground(Objects.equals(game.instanceId(), State.lastInstanceId) ? Color.GREEN : orange);
        playersLabel.setText("%d/%d".formatted(game.players().values().stream().filter(p -> p.state() == Player.State.ACTIVE).count(), game.playerCount()));
    }

}
