package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.services.ArticuloService;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
@RequiredArgsConstructor
public class ArticuloController {

    private final ArticuloService service;

    @GetMapping
    public List<ArticuloDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Articulo getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Articulo create(@RequestBody Articulo articulo) {
        return service.save(articulo);
    }

    @PutMapping("/{id}")
    public Articulo update(@PathVariable Long id, @RequestBody Articulo articulo) {
        return service.update(id, articulo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Endpoints para manejar elementos eliminados
    @GetMapping("/deleted")
    public List<ArticuloDTO> getAllDeleted() {
        return service.findAllDeleted();
    }

    @GetMapping("/deleted/{id}")
    public Articulo getOneDeleted(@PathVariable Long id) {
        return service.findByIdDeleted(id);
    }

    @PostMapping("/deleted/{id}/restore")
    public void restore(@PathVariable Long id) {
        service.restoreDeleted(id);
    }

    @DeleteMapping("/deleted/{id}")
    public void permanentDelete(@PathVariable Long id) {
        service.permanentDelete(id);
    }
}