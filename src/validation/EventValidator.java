package validation;

import model.Event;

public interface EventValidator {
    boolean isValid(Event event);
}
