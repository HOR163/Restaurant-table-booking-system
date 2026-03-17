package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.RestaurantDto;
import ee.hor.tablebooking.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        uses = {TableMapper.class}
)
public interface RestaurantMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    RestaurantDto mapToDto(RestaurantEntity restaurantEntity);

    @Mapping(target = "tables", ignore = true)
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(target = "id", ignore = true)
    RestaurantEntity mapToEntity(RestaurantDto restaurantDto);

    List<RestaurantDto> mapToDto(Collection<RestaurantEntity> restaurantEntity);
}
