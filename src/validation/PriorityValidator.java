package validation;

import exception.EventValidationException;
import model.Event;

public class PriorityValidator implements EventValidator {
    @Override
    public boolean isValid(Event event) {
        long eventPriority = event.getPriority();
        if (eventPriority >= 1 && eventPriority <= 10) {
            return true;
        }

        throw new EventValidationException("Приоритет должен находится в диапазоне от 1 до 10");
    }
}
