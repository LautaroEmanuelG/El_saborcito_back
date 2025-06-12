package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.FacturaDTO;
import utn.saborcito.El_saborcito_back.services.FacturaService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {
    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> getAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> create(@RequestBody FacturaDTO dto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> update(@PathVariable Long id, @RequestBody FacturaDTO dto) {
        return ResponseEntity.ok(facturaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
