package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.services.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    // Listar solo categorías activas (no eliminadas)
    @GetMapping
    public List<CategoriaDTO> getAll() {
        return service.findAll();
    }

    // Listar solo categorías eliminadas
    @GetMapping("/deleted")
    public List<CategoriaDTO> getAllDeleted() {
        return service.findAllDeleted();
    }

    @GetMapping("/{id}")
    public CategoriaDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public CategoriaDTO create(@RequestBody CategoriaDTO categoriaDTO) {
        return service.save(categoriaDTO);
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        return service.update(id, categoriaDTO);
    }

    // Baja lógica (soft delete)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Restaurar categoría eliminada
    @PostMapping("/deleted/{id}/restore")
    public void restore(@PathVariable Long id) {
        service.restore(id);
    }
}