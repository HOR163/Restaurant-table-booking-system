package ee.hor.tablebooking.mapper;

import ee.hor.tablebooking.dto.AttributeDto;
import ee.hor.tablebooking.entity.AttributeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface AttributeMapper {
    AttributeDto mapToDto(AttributeEntity attributeEntity);

    @Mapping(target = "tables", ignore = true)
    @Mapping(target = "id", ignore = true)
    AttributeEntity mapToEntity(AttributeDto attributeDto);

    default UUID toId(AttributeEntity attributeEntity) {
        return attributeEntity != null ? attributeEntity.getId() : null;
    }
}
