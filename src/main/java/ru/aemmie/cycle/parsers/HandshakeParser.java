package ru.aemmie.cycle.parsers;

import ru.aemmie.cycle.basic.Line;
import ru.aemmie.cycle.basic.Parser;
import ru.aemmie.cycle.events.parser.ParserEvent;
import ru.aemmie.cycle.events.parser.server.ServerTimeSetEvent;

import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.StringUtils.substringBetween;

public class HandshakeParser implements Parser {
    @Override
    public String type() {
        return "LogHandshake";
    }

    @Override
    public ParserEvent parse(Line line) {
        if (line.startsWith("SendChallengeResponse")) {
            int secondsSinceStart = Integer.parseInt(substringBetween(line.getMsg(), "Timestamp: ", "."));
            return ServerTimeSetEvent.builder()
                    .time(line.getTime())
                    .createdAt(line.getTime().minus(secondsSinceStart, ChronoUnit.SECONDS))
                    .secondsSinceStart(secondsSinceStart)
                    .build();
        }
        return null;
    }
}
