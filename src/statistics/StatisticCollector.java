package statistics;

import model.Event;
import model.EventType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticCollector {
    private final AtomicLong totalProcessed = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);
    private final ConcurrentHashMap<EventType, AtomicLong> eventsByType = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> eventsBySource = new ConcurrentHashMap<>();

    public void recordSuccess(Event event) {
        totalProcessed.incrementAndGet();

        EventType eventType = event.getType();
        eventsByType.computeIfAbsent(eventType, e -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordFailure(Event event) {
        totalFailed.incrementAndGet();

        EventType eventType = event.getType();
        eventsByType.computeIfAbsent(eventType, e -> new AtomicLong(0)).incrementAndGet();
    }

    public AggregatedStatistic getSnapshot() {
        long currentTotalProcessed = totalProcessed.get();
        long currentTotalFailed = totalFailed.get();
        Map<EventType, Long> currentEventsByType = new HashMap<>();

        for (Map.Entry<EventType, AtomicLong> entry : eventsByType.entrySet()) {
            EventType key = entry.getKey();
            Long value = entry.getValue().get();

            currentEventsByType.put(key, value);
        }

        return new AggregatedStatistic(currentTotalProcessed, currentTotalFailed, currentEventsByType);
    } 
}
