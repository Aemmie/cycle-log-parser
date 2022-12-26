package ru.aemmie.cycle.basic;

import lombok.Getter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
public class Line {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss:SSS").withZone(ZoneOffset.UTC);
    private final String full;
    private final String type;
    private final String msg;
    private Instant time;

    public Line(String line) {
        full = line;
        var ind = line.indexOf(':', 30);
        if (ind == -1) {
            type = "";
            msg = line;
        } else {
            type = line.substring(30, ind);
            msg = line.substring(ind + 2);
        }
    }

    public Instant getTime() {
        if (time == null) {
            time = Instant.from(format.parse(full.substring(1, 24)));
        }
        return time;
    }

    public boolean type(String type) {
        return Objects.equals(type, this.type);
    }

    public boolean startsWith(String msg) {
        return this.msg.startsWith(msg);
    }
}
