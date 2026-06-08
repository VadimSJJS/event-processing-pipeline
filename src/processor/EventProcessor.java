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
    private volatile boolean running = true;
    private Thread consumerThread;


    public void submitEvent(Event event) {
        blockingQueue.add(event);
    }

    public void startProcessing(StatisticCollector statisticCollector, FileEventLogger fileEventLogger) throws InterruptedException {
        consumerThread = Thread.currentThread();

        while (running) {
            try {

                Event event = blockingQueue.take();

                try {
                    ValidationUtils.validate(event);

                    statisticCollector.recordSuccess(event);
                    fileEventLogger.logSuccess(event);
                } catch (EventValidationException e) {
                    statisticCollector.recordFailure(event);
                    fileEventLogger.logFailed(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        running = false;
        if (consumerThread != null) {
            consumerThread.interrupt();
        }
    }
}
