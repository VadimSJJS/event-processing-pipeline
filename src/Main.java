import io.FileEventLogger;
import io.ReportGenerator;
import processor.EventProcessor;
import statistics.StatisticCollector;
import validation.ValidationUtils;
import model.Event;
import model.EventType;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Event event1 = new Event(123, EventType.LOG, "mobile", Map.of("USER_ID", "1, 2, 3"), 1);
//        Event event2 = new Event(EventType.METRIC, "web", new HashMap<>(2, 2), 2);
//        Event event3 = new Event(EventType.LOG, "desktop", Map.of("1", "2"), 5);
//        Event event4 = new Event(EventType.LOG, "desktop", Map.of("1", "1"), 10);
        StatisticCollector statisticCollector = new StatisticCollector();
        ReportGenerator reportGenerator = new ReportGenerator();

//        ValidationUtils.validate(event3);
//        ValidationUtils.validate(event4);

        FileEventLogger fileEventLogger = new FileEventLogger();

//        fileEventLogger.logSuccess(event1);
//        fileEventLogger.logSuccess(event2);

//        fileEventLogger.logFailed(event3);
//        fileEventLogger.logFailed(event4);

        FileEventLogger eventLogger = new FileEventLogger();
//        StatisticCollector statisticCollector = new StatisticCollector();
        EventProcessor eventProcessor = new EventProcessor();

        Thread thread = new Thread(() -> {
            try {
                eventProcessor.startProcessing(statisticCollector, eventLogger);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
            }
        });
        thread.start();

        Map<String, String> payload = new HashMap<>();
        payload.put("test", "value");

        Event event1 = new Event(EventType.LOG, "web", payload, 5);
        Event event2 = new Event(EventType.METRIC, "backend", payload, 99);
        Event event3 = new Event(EventType.USER_ACTION, "mobile", payload, 3);

        eventProcessor.submitEvent(event1);
        eventProcessor.submitEvent(event2);
        eventProcessor.submitEvent(event3);

        Thread.sleep(2000);

        thread.interrupt();
        thread.join();

        System.out.println("Total processed: " + statisticCollector.getSnapshot().getTotalProcessed());
        System.out.println("Total failed: " + statisticCollector.getSnapshot().getTotalFailed());
        System.out.println("Events by type: " + statisticCollector.getSnapshot().getEventsByType());

        System.out.println("Проверка завершена. Смотри файлы в папке logs/");


    }
}