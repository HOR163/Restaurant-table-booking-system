package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.TableDto;
import ee.hor.tablebooking.entity.TableEntity;
import ee.hor.tablebooking.excpetion.EntityInUseException;
import ee.hor.tablebooking.excpetion.ResourceNotFoundException;
import ee.hor.tablebooking.mapper.TableMapper;
import ee.hor.tablebooking.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final TableMapper tableMapper;

    public TableDto getTable(UUID id) {
        TableEntity tableEntity = tableRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Table with given id does not exist")
        );

        return tableMapper.mapToDto(tableEntity);
    }

    public TableDto addTable(TableDto tableDto) {
        TableEntity tableEntity = tableMapper.mapToEntity(tableDto);

        TableEntity newTable = tableRepository.save(tableEntity);

        return tableMapper.mapToDto(newTable);
    }

    public List<TableDto> getTablesInRestaurant(UUID id) {
        List<TableEntity> tables = tableRepository.findAllByRestaurantId(id);

        return tableMapper.mapToDto(tables);
    }

    public void deleteTable(UUID id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table with given id does not exist");
        }

        try {
            tableRepository.deleteById(id);
        } catch (DataIntegrityViolationException _) {
            throw new EntityInUseException("Table is being used by other objects, remove them before proceeding with deletion");
        }
    }
}
