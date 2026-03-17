package ee.hor.tablebooking.excpetion;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
