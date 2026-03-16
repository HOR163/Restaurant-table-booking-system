package ee.hor.tablebooking.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RestaurantDto {
    private UUID id;
    private UUID ownerId;
    private String name;
    private List<UUID> tables;
}
