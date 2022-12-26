package ru.aemmie.cycle.processors;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import ru.aemmie.cycle.basic.Processor;
import ru.aemmie.cycle.objects.State;
import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.basic.GameProcessor;
import ru.aemmie.cycle.events.parser.server.ServerChangedEvent;
import ru.aemmie.cycle.objects.Game;

import java.util.Set;

import static ru.aemmie.cycle.objects.State.game;

@Slf4j
public class MainProcessor implements Processor {
    private final Set<Class<? extends GameProcessor>> gameProcessors;

    public MainProcessor(Set<Class<? extends GameProcessor>> gameProcessors) {
        this.gameProcessors = gameProcessors;
    }

    @Subscribe
    public void newServer(ServerChangedEvent event) {
        if (event.isHub()) {
            game.ended(true).updateUI();
        } else {
            State.lastInstanceId = game.instanceId();
            game = new Game(event.getServerId()).enteredAt(event.getTime());
            gameProcessors.stream().map(Utils::getInstance).forEach(Utils.eventBus::register);
            game.updateUI();
        }
    }

}
