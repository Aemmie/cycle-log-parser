package ru.aemmie.cycle.parsers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Listener implements TailerListener {

    private final Pattern linePattern = Pattern.compile("\\[(\\d{4}\\.\\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}\\.\\d{2}:\\d{3})\\]\\[.{3}\\](\\w*): (.*)");
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss:SSS").withZone(ZoneOffset.UTC);

    private final List<Parser> parsers = List.of(
        new ActivitiesParser(),
        new PlayerParser(),
        new ServerParser()
    );

    @Override
    public void handle(String string) {
        try {
            Matcher matcher = linePattern.matcher(string);
            if (!matcher.matches()) {
                return;
            }

            var time = Instant.from(format.parse(matcher.group(1)));
            var type = matcher.group(2);
            var text = matcher.group(3);

            parsers.forEach(p -> p.parse(time, type, text));

        } catch (Exception e) {
            log.error("Parse exception: ", e);
        }
    }

    @Override
    public void init(Tailer tailer) {
    }

    @Override
    public void fileNotFound() {
        log.error("Log file not found");
    }

    @Override
    public void fileRotated() {
        log.info("File rotated");
    }

    @Override
    public void handle(Exception ex) {
        log.error("Exception? Meh.", ex);
    }

}
