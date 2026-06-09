package io;

import model.Event;
import model.EventType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileEventLogger {
    private final Path pathToSuccessEvents = Path.of("logs/success_events.log");
    private final Path pathToFailedEvents = Path.of("logs/failed_events.log");

    public void logSuccess(Event event) {
        writeLineToFile(event, pathToSuccessEvents);
    }

    public void logFailed(Event event) {
        writeLineToFile(event, pathToFailedEvents);
    }

    private void writeLineToFile(Event event, Path path) {
        try {
            Files.createDirectories(path.getParent());
            String line = formatEventLine(event);
            Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write event log: " + e.getMessage());
        }
    }

    private String formatEventLine(Event event) {
        String id = event.getId();
        long timestamp = event.getTimestamp();
        EventType eventType = event.getType();
        String source = event.getSource();
        String payload = event.getPayload().toString();
        int priority = event.getPriority();

        return id + " | " + timestamp + " | " + eventType + " | " + source + " | " + payload + " | " + priority + System.lineSeparator();
    }
}
