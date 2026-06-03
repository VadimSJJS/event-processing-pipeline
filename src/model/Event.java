package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    private final String id;
    private final long timestamp; // кол-во пройденных миллисекунд (отчет с 1970 г.)
    private final EventType type;
    private final String source; // источник события, пример: web, mobile, backend и т.д
    private final Map<String, String> payload; // основное содержимое события
    private final int priority;

    public Event(EventType type, String source, Map<String, String> payload, int priority) {
        id = UUID.randomUUID().toString();
        timestamp = System.currentTimeMillis();
        this.type = type;
        this.source = source;
        this.payload = new HashMap<>(payload);
        this.priority = priority;
    }

    // timestamp сами задаем для тестирования
    public Event(long timestamp, EventType type, String source, Map<String, String> payload, int priority) {
        id = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.type = type;
        this.source = source;
        this.payload = new HashMap<>(payload);
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public EventType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public Map<String, String> getPayload() {
        return Collections.unmodifiableMap(payload);
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", source='" + source + '\'' +
                ", payload=" + payload +
                ", priority=" + priority +
                '}';
    }
}
