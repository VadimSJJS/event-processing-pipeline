package statistics;

import model.EventType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AggregatedStatistic {
    private final long totalProcessed;
    private final long totalFailed;
    private final Map<EventType, Long> eventsByType;
    private final Map<String, Integer> eventsBySource;

    public AggregatedStatistic(long totalProcessed,
                               long totalFailed,
                               Map<EventType, Long> eventsByType,
                               Map<String, Integer> eventsBySource) {
        this.totalProcessed = totalProcessed;
        this.totalFailed = totalFailed;
        this.eventsByType = new HashMap<>(eventsByType);
        this.eventsBySource = new HashMap<>(eventsBySource);
    }

    public long getTotalProcessed() {
        return totalProcessed;
    }

    public long getTotalFailed() {
        return totalFailed;
    }

    public Map<EventType, Long> getEventsByType() {
        return Collections.unmodifiableMap(eventsByType);
    }

    public Map<String, Integer> getEventsBySource() {
        return Collections.unmodifiableMap(eventsBySource);
    }

    public long getTotalEvents() {
        return totalProcessed + totalFailed;
    }
}
