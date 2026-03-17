package ee.hor.tablebooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private Integer status;
    private String message;
    private Instant timestamp;
}
