package processor;

import model.Event;
import model.EventType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EventProducer implements Runnable {
    private final EventProcessor eventProcessor;
    private final int eventCount;

    public EventProducer(EventProcessor eventProcessor, int eventCount) {
        this.eventProcessor = eventProcessor;
        this.eventCount = eventCount;
    }

    public void run() {
        for (int i = 0; i < eventCount; i++) {
            Event event = generateRandomEvent();
            eventProcessor.submitEvent(event);
        }
    }

    public Event generateRandomEvent() {
        EventType eventType = EventType.values()[ThreadLocalRandom.current().nextInt(EventType.values().length)];
        String[] sources = new String[] {"web", "mobile", "backend"};
        String source = sources[ThreadLocalRandom.current().nextInt(sources.length)];
        Map<String, String> payload = Map.of("key" + ThreadLocalRandom.current().nextInt(1, 10), "value" + ThreadLocalRandom.current().nextInt(1, 10));
        int priority = ThreadLocalRandom.current().nextInt(1, 10);
        Event event = new Event(eventType, source, payload, priority);

        return event;
    }
}
