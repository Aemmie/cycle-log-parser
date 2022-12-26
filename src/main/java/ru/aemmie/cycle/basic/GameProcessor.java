package ru.aemmie.cycle.basic;

import com.google.common.eventbus.Subscribe;
import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.events.parser.server.ServerChangedEvent;

public interface GameProcessor extends Processor {

    @Subscribe
    default void stopProcessing(ServerChangedEvent event) {
        Utils.eventBus.unregister(this);
    }
}
