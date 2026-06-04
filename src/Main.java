import io.FileEventLogger;
import io.ReportGenerator;
import statistics.StatisticCollector;
import validation.ValidationUtils;
import model.Event;
import model.EventType;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Event event1 = new Event(123, EventType.LOG, "mobile", Map.of("USER_ID", "1, 2, 3"), 1);
        Event event2 = new Event(EventType.METRIC, "web", new HashMap<>(2, 2), 2);
        Event event3 = new Event(EventType.LOG, "desktop", Map.of("1", "2"), 5);
        Event event4 = new Event(EventType.LOG, "desktop", Map.of("1", "1"), 10);
        StatisticCollector statisticCollector = new StatisticCollector();
        ReportGenerator reportGenerator = new ReportGenerator();

        ValidationUtils.validate(event3);
        ValidationUtils.validate(event4);

        FileEventLogger fileEventLogger = new FileEventLogger();

        fileEventLogger.logSuccess(event1);
        fileEventLogger.logSuccess(event2);

        fileEventLogger.logFailed(event3);
        fileEventLogger.logFailed(event4);
    }
}