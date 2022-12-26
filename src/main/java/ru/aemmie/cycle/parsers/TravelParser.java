package ru.aemmie.cycle.parsers;

import ru.aemmie.cycle.basic.Line;
import ru.aemmie.cycle.basic.Parser;
import ru.aemmie.cycle.events.parser.ParserEvent;
import ru.aemmie.cycle.events.parser.server.PartySizeSetEvent;
import ru.aemmie.cycle.events.parser.server.ServerChangedEvent;

import static org.apache.commons.lang3.StringUtils.substringBetween;

public class TravelParser implements Parser {

    @Override
    public String type() {
        return "LogYTravel";
    }

    @Override
    public ParserEvent parse(Line line) {
        if (line.startsWith("UYControllerTravelComponent::TravelToServer")) {
            return ServerChangedEvent.builder()
                    .time(line.getTime())
                    .serverId(substringBetween(line.getMsg(), "sessionId [", "]"))
                    .isHub("0".equals(substringBetween(line.getMsg(), "m_isMatch [", "]")))
                    .build();
        } else if (line.startsWith("Forcing transition to match")) {
            String size = substringBetween(line.getMsg(), "SquadSize=", "?");
            return PartySizeSetEvent.builder()
                    .time(line.getTime())
                    .size(size == null ? 1 : Integer.parseInt(size))
                    .build();
        }
        return null;
    }
}
