package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.AttributeDto;
import ee.hor.tablebooking.entity.AttributeEntity;
import ee.hor.tablebooking.mapper.AttributeMapper;
import ee.hor.tablebooking.repository.AttributeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    public AttributeDto getAttribute(UUID attributeId) {
        // TODO: throw an error if attribute does not exist
        AttributeEntity attributeEntity = attributeRepository.findById(attributeId).orElse(null);

        return attributeMapper.mapToDto(attributeEntity);
    }

    public AttributeDto addAttribute(AttributeDto attributeDto) {
        AttributeEntity attributeEntity = attributeMapper.mapToEntity(attributeDto);

        AttributeEntity newAttributeEntity = attributeRepository.save(attributeEntity);
        return attributeMapper.mapToDto(newAttributeEntity);
    }

    public void deleteAttribute(UUID attributeId) {
        // TODO: throw an error if attribute doesn't exist OR the attribute is used by tables
        attributeRepository.deleteById(attributeId);
    }
}
