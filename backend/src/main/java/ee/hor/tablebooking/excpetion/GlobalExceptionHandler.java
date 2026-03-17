package ee.hor.tablebooking.excpetion;

import ee.hor.tablebooking.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getStatus(), e.getMessage(), Instant.now());
        return ResponseEntity.status(e.getStatus()).body(responseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleApplicationException(Exception e) {
        log.error("Unexpected error", e);
        ErrorResponseDto responseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
}
