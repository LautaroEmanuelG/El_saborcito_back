package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.UnidadMedidaDTO;
import utn.saborcito.El_saborcito_back.services.UnidadMedidaService;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
public class UnidadMedidaController {

    private final UnidadMedidaService service;

    // Obtener todas las activas
    @GetMapping
    public List<UnidadMedidaDTO> getAll() {
        return service.findAll();
    }

    // Obtener todas incluyendo eliminadas (para el frontend)
    @GetMapping("/all")
    public List<UnidadMedidaDTO> getAllIncludingDeleted() {
        return service.findAllIncludingDeleted();
    }

    @GetMapping("/{id}")
    public UnidadMedidaDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<UnidadMedidaDTO> create(@RequestBody UnidadMedidaDTO dto) {
        UnidadMedidaDTO created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public UnidadMedidaDTO update(@PathVariable Long id, @RequestBody UnidadMedidaDTO dto) {
        return service.update(id, dto);
    }

    // Baja lógica
    @PatchMapping("/{id}/baja")
    public ResponseEntity<Void> bajaLogica(@PathVariable Long id) {
        service.bajaLogicaUnidadMedida(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar
    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Long id) {
        service.restaurarUnidadMedida(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminación física (opcional)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}