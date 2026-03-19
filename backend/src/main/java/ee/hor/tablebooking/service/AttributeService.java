package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.AttributeDto;
import ee.hor.tablebooking.entity.AttributeEntity;
import ee.hor.tablebooking.excpetion.EntityInUseException;
import ee.hor.tablebooking.excpetion.ResourceNotFoundException;
import ee.hor.tablebooking.mapper.AttributeMapper;
import ee.hor.tablebooking.repository.AttributeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    public AttributeDto getAttribute(UUID attributeId) {
        AttributeEntity attributeEntity = attributeRepository.findById(attributeId).orElseThrow(
                () -> new ResourceNotFoundException("Attribute with given id does not exist")
        );
        return attributeMapper.mapToDto(attributeEntity);
    }

    public List<AttributeDto> getAllAttributes() {
        List<AttributeEntity> attributes = attributeRepository.findAll();
        return attributeMapper.mapToDto(attributes);
    }

    public AttributeDto addAttribute(AttributeDto attributeDto) {
        AttributeEntity attributeEntity = attributeMapper.mapToEntity(attributeDto);

        AttributeEntity newAttributeEntity = attributeRepository.save(attributeEntity);
        return attributeMapper.mapToDto(newAttributeEntity);
    }

    public void deleteAttribute(UUID attributeId) {
        if (!attributeRepository.existsById(attributeId)) {
            throw new ResourceNotFoundException("Attribute with given id does not exist");
        }
        try {
            attributeRepository.deleteById(attributeId);
        } catch (DataIntegrityViolationException _) {
            throw new EntityInUseException("Attribute is being used by other objects, remove them before proceeding with deletion");
        }
    }
}
