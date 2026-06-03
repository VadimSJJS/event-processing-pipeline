import model.Event;
import model.EventType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Event event1 = new Event(123, EventType.LOG, "mobile", Map.of("USER_ID", "1, 2, 3"), 1);
        Event event2 = new Event(EventType.METRIC, "web", new HashMap<>(2, 2), 2);

        System.out.println(event1);
        System.out.println(event2);

        System.out.println(event1.getPayload());
        Map<String, String> map = event1.getPayload();
        System.out.println(map);
        map.put("newKEY", "NEWVALUE");
        System.out.println(map);
        System.out.println(event1.getPayload());
    }
}