package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.BookingDto;
import ee.hor.tablebooking.entity.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface BookingMapper {
    @Mapping(target = "tableId", source = "table.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target="restaurantId", source = "table.restaurant.id")
    BookingDto mapToDto(BookingEntity bookingEntity);

    @Mapping(target="table.id", source="tableId")
    @Mapping(target="user.id", source="userId")
    BookingEntity mapToEntity(BookingDto bookingDto);
}
