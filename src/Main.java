import io.FileEventLogger;
import io.ReportGenerator;
import processor.EventProducer;
import processor.EventProcessor;
import statistics.AggregatedStatistic;
import statistics.StatisticCollector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String LOGS_DIR = "logs";
    private static final String REPORT_PATH = LOGS_DIR + "/report.txt";

    public static void main(String[] args) throws InterruptedException, IOException {
        int producerCount = 3;
        int eventsPerProducer = 20;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--threads":
                case "-t":
                    if (i + 1 < args.length) {
                        try {
                            producerCount = Integer.parseInt(args[++i]);
                            if (producerCount <= 0) {
                                System.err.println("Error: threads count must be positive. Using default: 3");
                                producerCount = 3;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Error: invalid number for threads. Using default: 3");
                        }
                    } else {
                        System.err.println("Error: --threads requires a value. Using default: 3");
                    }
                    break;

                case "--eventsPerThread":
                case "-e":
                    if (i + 1 < args.length) {
                        try {
                            eventsPerProducer = Integer.parseInt(args[++i]);
                            if (eventsPerProducer <= 0) {
                                System.err.println("Error: eventsPerThread must be positive. Using default: 20");
                                eventsPerProducer = 20;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Error: invalid number for eventsPerThread. Using default: 20");
                        }
                    } else {
                        System.err.println("Error: --eventsPerThread requires a value. Using default: 20");
                    }
                    break;

                case "--help":
                case "-h":
                    printHelp();
                    return;

                default:
                    System.err.println("Unknown argument: " + args[i]);
                    System.err.println("Use --help for usage information");
                    return;
            }
        }

        int totalExpectedEvents = producerCount * eventsPerProducer;

        System.out.println("=== Configuration ===");
        System.out.println("Producer threads: " + producerCount);
        System.out.println("Events per producer: " + eventsPerProducer);
        System.out.println("Total events to generate: " + totalExpectedEvents);
        System.out.println();

        Files.createDirectories(Path.of(LOGS_DIR));
        resetLogFiles();

        StatisticCollector statisticCollector = new StatisticCollector();
        FileEventLogger fileEventLogger = new FileEventLogger();
        EventProcessor eventProcessor = new EventProcessor();
        ReportGenerator reportGenerator = new ReportGenerator();

        Thread consumerThread = new Thread(() -> {
            try {
                eventProcessor.startProcessing(statisticCollector, fileEventLogger);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "event-consumer");
        consumerThread.start();

        Thread[] producerThreads = new Thread[producerCount];
        for (int i = 0; i < producerCount; i++) {
            producerThreads[i] = new Thread(
                    new EventProducer(eventProcessor, eventsPerProducer),
                    "event-producer-" + i
            );
            producerThreads[i].start();
        }

        for (Thread producerThread : producerThreads) {
            producerThread.join();
        }

        eventProcessor.requestShutdown();
        consumerThread.join();

        AggregatedStatistic snapshot = statisticCollector.getSnapshot();

        System.out.println("=== Processing Complete ===");
        reportGenerator.printToConsole(snapshot, producerCount, totalExpectedEvents);
        reportGenerator.generate(snapshot, REPORT_PATH);

        System.out.println();
        System.out.println("Report saved to: " + REPORT_PATH);
        System.out.println("Event logs saved to: " + LOGS_DIR + "/success_events.log and " + LOGS_DIR + "/failed_events.log");
    }

    private static void printHelp() {
        System.out.println("=== Event Processing Pipeline ===");
        System.out.println("Usage: java Main [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --threads, -t <N>           Number of producer threads (default: 3)");
        System.out.println("  --eventsPerThread, -e <N>   Number of events per producer (default: 20)");
        System.out.println("  --help, -h                  Show this help message");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java Main -t 4 -e 100");
        System.out.println("  java Main --threads 5 --eventsPerThread 50");
    }

    private static void resetLogFiles() throws IOException {
        Files.deleteIfExists(Path.of(LOGS_DIR, "success_events.log"));
        Files.deleteIfExists(Path.of(LOGS_DIR, "failed_events.log"));
        Files.deleteIfExists(Path.of(REPORT_PATH));
    }
}
