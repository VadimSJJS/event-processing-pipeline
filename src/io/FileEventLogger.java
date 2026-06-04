package io;

import model.Event;
import model.EventType;

import java.io.FileWriter;
import java.io.IOException;

// класс записывает сами события в файл
public class FileEventLogger {
    private final String pathToSuccessEvents = "logs/success_events.log";
    private final String pathToFailedEvents = "logs/failed_events.log";

    public void logSuccess(Event event) {
        writeLineToFile(event, pathToSuccessEvents);
    }

    public void logFailed(Event event/*, Throwable error*/ ) {
        writeLineToFile(event, pathToFailedEvents);
    }

    private void writeLineToFile(Event event, String path) {
        try (FileWriter fileWriter = new FileWriter(path, true)) {
            String id = event.getId();
            long timestamp = event.getTimestamp();
            EventType eventType = event.getType();
            String source = event.getSource();

            int priority = event.getPriority();
            String line = id + " | " + timestamp + " | " + eventType + " | " + source + " | "  + " | " + priority + "\n";

            fileWriter.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
