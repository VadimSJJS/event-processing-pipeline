package validation;

import exception.EventValidationException;
import model.Event;

public class TimestampValidator implements EventValidator  {
    @Override
    public boolean isValid(Event event) {
        if (event.getTimestamp() <= System.currentTimeMillis()) {
            return true;
        }

        throw new EventValidationException("Передаваемое время не может быть больше чем текущее");
    }
}
