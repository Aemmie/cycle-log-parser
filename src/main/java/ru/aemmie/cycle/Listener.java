package ru.aemmie.cycle;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.reflections.Reflections;
import ru.aemmie.cycle.basic.Event;
import ru.aemmie.cycle.basic.GameProcessor;
import ru.aemmie.cycle.basic.Line;
import ru.aemmie.cycle.basic.Parser;
import ru.aemmie.cycle.processors.MainProcessor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
class Listener implements TailerListener {

    private final Map<String, Parser> parsers = new HashMap<>();


    @SneakyThrows
    public Listener() {
        Reflections reflections = new Reflections("ru.aemmie.cycle");
        reflections.getSubTypesOf(Parser.class).stream().map(Listener::getInstance).forEach(p -> parsers.put(p.type(), p));
        Utils.eventBus.register(new MainProcessor(reflections.getSubTypesOf(GameProcessor.class)));
    }

    @SneakyThrows
    private static <T> T getInstance(Class<T> cl) {
        return cl.getConstructor().newInstance();
    }

    @Override
    public void handle(String string) {
        if (!string.startsWith("[202")) {
            return;
        }

        var line = new Line(string);

        Parser parser = parsers.get(line.getType());
        if (parser != null) {
            Event event = parser.parse(line);
            if (event != null) {
                Utils.eventBus.post(event);
            }
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
