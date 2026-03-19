package ee.hor.tablebooking.controller;

import ee.hor.tablebooking.dto.AttributeDto;
import ee.hor.tablebooking.service.AttributeService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDto> getAttribute(@PathVariable UUID id) {
        return ResponseEntity.ok(attributeService.getAttribute(id));
    }

    @GetMapping
    public ResponseEntity<List<AttributeDto>> getAllAttributes() {
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }

    @PostMapping
    public ResponseEntity<AttributeDto> addAttribute(@RequestBody AttributeDto attributeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attributeService.addAttribute(attributeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable UUID id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
