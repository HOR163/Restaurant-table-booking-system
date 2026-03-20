package ee.hor.tablebooking.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TableDto {
    private UUID id;
    private UUID restaurantId;
    private Short seatsAmount;
    private Short tableNumber;
    private List<UUID> attributes;
}
