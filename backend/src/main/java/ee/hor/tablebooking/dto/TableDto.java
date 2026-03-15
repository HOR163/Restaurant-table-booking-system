package ee.hor.tablebooking.dto;

import java.util.List;
import java.util.UUID;

public class TableDto {
    private UUID id;
    private Short seatsAmount;
    private List<AttributeDto> attributes;
}
