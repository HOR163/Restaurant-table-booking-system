package ee.hor.tablebooking.excpetion;

import org.springframework.http.HttpStatus;

public class EntityInUseException extends ApplicationException {
    public EntityInUseException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
