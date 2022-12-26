package ru.aemmie.cycle.basic;

import ru.aemmie.cycle.Utils;
import ru.aemmie.cycle.events.state.StateEvent;

public interface Processor {

    default void sendEvent(StateEvent event) {
        Utils.eventBus.post(event);
    }
}
