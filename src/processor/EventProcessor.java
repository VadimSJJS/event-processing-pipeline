package processor;

import exception.EventValidationException;
import io.FileEventLogger;
import model.Event;
import statistics.StatisticCollector;
import validation.ValidationUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventProcessor {
    private final BlockingQueue<Event> blockingQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public void submitEvent(Event event) {
        blockingQueue.add(event);
    }

    public void startProcessing(StatisticCollector statisticCollector, FileEventLogger fileEventLogger)
            throws InterruptedException {
        while (running || !blockingQueue.isEmpty()) {
            Event event = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
            if (event == null) {
                continue;
            }
            processEvent(event, statisticCollector, fileEventLogger);
        }
    }

    public void requestShutdown() {
        running = false;
    }

    private void processEvent(Event event, StatisticCollector statisticCollector, FileEventLogger fileEventLogger) {
        try {
            ValidationUtils.validate(event);
            statisticCollector.recordSuccess(event);
            fileEventLogger.logSuccess(event);
        } catch (EventValidationException e) {
            statisticCollector.recordFailure(event);
            fileEventLogger.logFailed(event);
        }
    }
}
