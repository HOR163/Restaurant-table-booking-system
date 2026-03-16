package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.RegisterUserDto;
import ee.hor.tablebooking.dto.UserDto;
import ee.hor.tablebooking.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserDto mapToDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    UserEntity mapToEntity(RegisterUserDto registerUserDto);
}
