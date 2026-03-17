package ee.hor.tablebooking.controller;

import ee.hor.tablebooking.dto.TableDto;
import ee.hor.tablebooking.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("tables")
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;

    @GetMapping("/{id}")
    public ResponseEntity<TableDto> getTable(@PathVariable UUID id) {
        return ResponseEntity.ok(tableService.getTable(id));
    }

    @PostMapping
    public ResponseEntity<TableDto> addTable(@RequestBody TableDto tableDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.addTable(tableDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
