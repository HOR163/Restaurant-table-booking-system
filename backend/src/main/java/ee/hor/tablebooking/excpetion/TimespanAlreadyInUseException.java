package ee.hor.tablebooking.excpetion;

import org.springframework.http.HttpStatus;

public class TimespanAlreadyInUseException extends ApplicationException {
    public TimespanAlreadyInUseException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
