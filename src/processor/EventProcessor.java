package processor;

import exception.EventValidationException;
import io.FileEventLogger;
import model.Event;
import statistics.StatisticCollector;
import validation.ValidationUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventProcessor {
    private final BlockingQueue<Event> blockingQueue = new LinkedBlockingQueue<>();

    public void submitEvent(Event event) {
        blockingQueue.add(event);
    }

    public void startProcessing(StatisticCollector statisticCollector, FileEventLogger fileEventLogger) throws InterruptedException {
        while (true) {
            Event event = blockingQueue.take();

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
}
