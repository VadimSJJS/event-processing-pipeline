import validation.ValidationUtils;
import model.Event;
import model.EventType;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Event event1 = new Event(123, EventType.LOG, "mobile", Map.of("USER_ID", "1, 2, 3"), 1);
        Event event2 = new Event(EventType.METRIC, "web", new HashMap<>(2, 2), 2);
        Event event3 = new Event(EventType.LOG, "desktop", Map.of("1", "2"), 2);
        Event event4 = new Event(EventType.LOG, "desktop", Map.of("1", "1"), 11);

        ValidationUtils.validate(event3);
        ValidationUtils.validate(event4);
    }
}