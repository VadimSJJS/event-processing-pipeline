package io;

import model.EventType;
import statistics.AggregatedStatistic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
public class ReportGenerator {
    public void generate(AggregatedStatistic aggregatedStatistic, String path) {
        try {
            Path reportPath = Path.of(path);
            Files.createDirectories(reportPath.getParent());
            Files.writeString(reportPath, buildReport(aggregatedStatistic, null, null));
        } catch (IOException e) {
            System.err.println("Failed to write report: " + e.getMessage());
        }
    }

    public void printToConsole(AggregatedStatistic aggregatedStatistic, int producerCount, int totalExpectedEvents) {
        System.out.print(buildReport(aggregatedStatistic, producerCount, totalExpectedEvents));
    }

    private String buildReport(AggregatedStatistic aggregatedStatistic, Integer producerCount, Integer totalExpectedEvents) {
        StringBuilder sb = new StringBuilder("=== Event Processing Report ===\n");

        if (totalExpectedEvents != null) {
            sb.append("Total events generated: ").append(totalExpectedEvents).append("\n");
        }

        sb.append("Total processed successfully: ").append(aggregatedStatistic.getTotalProcessed()).append("\n");
        sb.append("Total failed validation: ").append(aggregatedStatistic.getTotalFailed()).append("\n");
        sb.append("Total handled: ").append(aggregatedStatistic.getTotalEvents()).append("\n\n");

        sb.append("Events by type:\n");
        aggregatedStatistic.getEventsByType().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sb.append("  ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n"));

        sb.append("\nEvents by source:\n");
        aggregatedStatistic.getEventsBySource().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sb.append("  ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n"));

        sb.append("\nTop sources by activity:\n");
        aggregatedStatistic.getEventsBySource().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(3)
                .forEach(entry -> sb.append("  ")
                        .append(entry.getKey())
                        .append(" (")
                        .append(entry.getValue())
                        .append(")\n"));

        if (producerCount != null) {
            sb.append("\nProcessed concurrently by ")
                    .append(producerCount)
                    .append(" producer thread(s) and 1 consumer thread\n");
        }

        return sb.toString();
    }
}
