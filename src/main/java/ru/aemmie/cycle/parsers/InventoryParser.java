package ru.aemmie.cycle.parsers;

import ru.aemmie.cycle.basic.Line;
import ru.aemmie.cycle.basic.Parser;
import ru.aemmie.cycle.events.parser.ParserEvent;
import ru.aemmie.cycle.events.parser.player.GameStateFailed;

public class InventoryParser implements Parser {
    @Override
    public String type() {
        return "LogYInventory";
    }

    @Override
    public ParserEvent parse(Line line) {
        if (line.startsWith("GetInventoryComponentManager | Could not retrieve YGameStateMatch!")) {
            return GameStateFailed.builder().time(line.getTime()).build();
        }
        return null;
    }
}
