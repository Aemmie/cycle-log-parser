package ru.aemmie.cycle.parsers;

import ru.aemmie.cycle.basic.Line;
import ru.aemmie.cycle.basic.Parser;
import ru.aemmie.cycle.events.parser.ParserEvent;
import ru.aemmie.cycle.events.parser.event.EvacShipCalledEvent;
import ru.aemmie.cycle.events.parser.event.MeteorsEvent;

public class ActivitiesParser implements Parser {
    @Override
    public String type() {
        return "LogYActivities";
    }

    @Override
    public ParserEvent parse(Line line) {
        if (line.startsWith("Warning: AC_EvacShip_BP")) {
            return EvacShipCalledEvent.builder().time(line.getTime()).build();
        } else if (line.startsWith("Warning: AA_MeteorShowerSpawner")) {
            return MeteorsEvent.builder().time(line.getTime()).build();
        }
        return null;
    }
}
