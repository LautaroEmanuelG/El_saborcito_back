package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.services.ArticuloInsumoService;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class ArticuloInsumoController {
    private final ArticuloInsumoService articuloInsumoService;

    @GetMapping
    public ResponseEntity<List<ArticuloInsumoDTO>> getAll() {
        return ResponseEntity.ok(articuloInsumoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloInsumoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articuloInsumoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArticuloInsumoDTO> create(@RequestBody ArticuloInsumoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articuloInsumoService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloInsumoDTO> update(@PathVariable Long id, @RequestBody ArticuloInsumoDTO dto) {
        return ResponseEntity.ok(articuloInsumoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articuloInsumoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
