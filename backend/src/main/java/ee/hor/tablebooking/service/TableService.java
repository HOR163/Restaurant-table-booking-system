package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.TableDto;
import ee.hor.tablebooking.entity.TableEntity;
import ee.hor.tablebooking.mapper.TableMapper;
import ee.hor.tablebooking.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final TableMapper tableMapper;

    public TableDto getTable(UUID id) {
        // FIXME: throw error if not found
        TableEntity tableEntity = tableRepository.findById(id).orElse(null);

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
        tableRepository.deleteById(id);
    }
}
