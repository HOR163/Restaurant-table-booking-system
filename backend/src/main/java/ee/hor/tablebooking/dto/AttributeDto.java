package ee.hor.tablebooking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AttributeDto {
    private UUID id;
    private String name;
}
