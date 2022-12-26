package ru.aemmie.cycle;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import ru.aemmie.cycle.overlay.Overlay;

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

        SwingUtilities.invokeAndWait(Overlay::new);

        new Tailer(prospect.toFile(), new Listener(), 500).run();

    }

}