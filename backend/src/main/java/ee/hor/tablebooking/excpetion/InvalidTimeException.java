package ee.hor.tablebooking.excpetion;

import org.springframework.http.HttpStatus;

public class InvalidTimeException extends ApplicationException {
    public InvalidTimeException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
