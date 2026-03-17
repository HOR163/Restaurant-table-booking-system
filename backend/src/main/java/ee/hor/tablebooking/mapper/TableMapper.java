package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.TableDto;
import ee.hor.tablebooking.entity.TableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        uses = {AttributeMapper.class}
)
public interface TableMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    TableDto mapToDto(TableEntity tableEntity);

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "restaurantId", target = "restaurant.id")
    TableEntity mapToEntity(TableDto tableDto);

    default UUID toId(TableEntity tableEntity) {
        return tableEntity != null ? tableEntity.getId() : null;
    }

    List<TableDto> mapToDto(Collection<TableEntity> tables);
}
