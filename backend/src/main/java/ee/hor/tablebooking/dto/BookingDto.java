package ee.hor.tablebooking.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class BookingDto {
    private UUID id;
    private UUID tableId;
    private UUID userId;
    private UUID restaurantId;
    private OffsetDateTime startTime;
    private Boolean pending;
    private OffsetDateTime pendingEndTime;
}
