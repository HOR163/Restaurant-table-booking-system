package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.RestaurantDto;
import ee.hor.tablebooking.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        uses = {TableMapper.class}
)
public interface RestaurantMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    RestaurantDto mapToDto(RestaurantEntity attributeEntity);

    @Mapping(target = "tables", ignore = true)
    @Mapping(target = "owner", ignore = true)
    RestaurantEntity mapToEntity(RestaurantDto attributeDto);
}
