package validation;

import exception.EventValidationException;
import model.Event;

import java.util.Map;

public class PayloadValidator implements EventValidator {

    @Override
    public boolean isValid(Event event) {
        Map<String, String> eventPayload = event.getPayload();

        if (!eventPayload.isEmpty()) {
            return true;
        }

        throw new EventValidationException("eventPayload не может быть пустым или null");
    }
}
