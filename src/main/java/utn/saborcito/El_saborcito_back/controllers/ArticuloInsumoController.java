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
        try {
            return ResponseEntity.ok(articuloInsumoService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } 
    }

    @PostMapping
    public ResponseEntity<ArticuloInsumoDTO> create(@RequestBody ArticuloInsumoDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(articuloInsumoService.save(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloInsumoDTO> update(@PathVariable Long id, @RequestBody ArticuloInsumoDTO dto) {
        try {
            return ResponseEntity.ok(articuloInsumoService.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            articuloInsumoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/es-para-elaborar")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllEsParaElaborar() {
        return ResponseEntity.ok(articuloInsumoService.findAllByEsParaElaborarTrue());
    }

    @GetMapping("/no-es-para-elaborar")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllNoEsParaElaborar() {
        return ResponseEntity.ok(articuloInsumoService.findAllByEsParaElaborarFalse());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(articuloInsumoService.findAllByCategoriaId(categoriaId));
    }
}
