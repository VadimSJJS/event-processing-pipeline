package io;

import model.EventType;
import statistics.AggregatedStatistic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

// класс записывает в файл всю статистику по событиям
public class ReportGenerator {
    private final String pathToReport = "logs/report.txt";

    public void generate(AggregatedStatistic aggregatedStatistic, String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            StringBuilder sb = new StringBuilder("=== Event Processing Report ===\n");
            long totalProcessed = aggregatedStatistic.getTotalProcessed();
            long totalFailed = aggregatedStatistic.getTotalFailed();
            Map<EventType, Long> eventsByType = aggregatedStatistic.getEventsByType();

            sb.append("Total processed: ").append(totalProcessed).append("\n");
            sb.append("Total failed: ").append(totalFailed).append("\n\n");
            sb.append("Events by type:\n");

            for (Map.Entry<EventType, Long> entry : eventsByType.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            fileWriter.write(String.valueOf(sb));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
