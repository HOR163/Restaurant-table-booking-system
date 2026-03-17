package ee.hor.tablebooking.excpetion;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final int status;

    public ApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status.value();
    }
}
