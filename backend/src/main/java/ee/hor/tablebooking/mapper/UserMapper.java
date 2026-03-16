package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.UserDto;
import ee.hor.tablebooking.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserDto mapToDto(UserEntity attributeEntity);

    // TODO: Change mapping from UserDto to SignUpDto
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    UserEntity mapToEntity(UserDto attributeDto);
}
