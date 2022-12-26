package ru.aemmie.cycle.basic;

import ru.aemmie.cycle.events.parser.ParserEvent;

public interface Parser {

    String type();

    ParserEvent parse(Line line);

}
