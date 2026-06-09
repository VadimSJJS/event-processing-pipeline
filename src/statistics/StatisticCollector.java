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
        incrementTypeCounter(event.getType());
        incrementSourceCounter(event.getSource());
    }

    public void recordFailure(Event event) {
        totalFailed.incrementAndGet();
        incrementTypeCounter(event.getType());
        incrementSourceCounter(event.getSource());
    }

    private void incrementTypeCounter(EventType eventType) {
        eventsByType.computeIfAbsent(eventType, key -> new AtomicLong(0)).incrementAndGet();
    }

    private void incrementSourceCounter(String source) {
        eventsBySource.computeIfAbsent(source, key -> new AtomicInteger(0)).incrementAndGet();
    }

    public AggregatedStatistic getSnapshot() {
        long currentTotalProcessed = totalProcessed.get();
        long currentTotalFailed = totalFailed.get();
        Map<EventType, Long> currentEventsByType = new HashMap<>();
        Map<String, Integer> currentEventsBySource = new HashMap<>();

        for (Map.Entry<EventType, AtomicLong> entry : eventsByType.entrySet()) {
            currentEventsByType.put(entry.getKey(), entry.getValue().get());
        }

        for (Map.Entry<String, AtomicInteger> entry : eventsBySource.entrySet()) {
            currentEventsBySource.put(entry.getKey(), entry.getValue().get());
        }

        return new AggregatedStatistic(
                currentTotalProcessed,
                currentTotalFailed,
                currentEventsByType,
                currentEventsBySource
        );
    }
}
