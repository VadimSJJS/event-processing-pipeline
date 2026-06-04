package validation;

import exception.EventValidationException;
import model.Event;

import java.util.Arrays;
import java.util.List;

public class ValidationUtils {
    public static void validate(Event event) throws EventValidationException {
        List<EventValidator> validators = Arrays.asList(
                new TimestampValidator(),
                new PayloadValidator(),
                new PriorityValidator()
        );

        for (EventValidator eventValidator : validators) {
            eventValidator.isValid(event);
        }
    }
}
