package ru.aemmie.cycle.parsers;

import java.time.Instant;

public interface Parser {
    void parse(Instant time, String type, String text);
}
