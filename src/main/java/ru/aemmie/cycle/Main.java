package ru.aemmie.cycle;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import ru.aemmie.cycle.overlay.Overlay;
import ru.aemmie.cycle.parsers.Listener;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        Path prospect = Paths.get(System.getenv("LOCALAPPDATA"), "Prospect", "Saved", "Logs", "Prospect.log");
        log.info("Game logs path: " + prospect);

        if (Files.exists(prospect)) {
            log.info("Log file found!");
        } else {
            log.error("File doesn't exist!");
            System.exit(-1);
        }

        log.info("Starting log parser...");

        Integer width = args.length == 2 ? Integer.parseInt(args[0]) : null;
        Integer height = args.length == 2 ? Integer.parseInt(args[1]) : null;

        SwingUtilities.invokeAndWait(() -> new Overlay(width, height));

        new Tailer(prospect.toFile(), new Listener(), 250).run();
    }

}