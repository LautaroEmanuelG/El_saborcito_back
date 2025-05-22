package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.services.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public List<Categoria> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Categoria getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Categoria create(@RequestBody Categoria categoria) {
        return service.save(categoria);
    }

    @PutMapping("/{id}")
    public Categoria update(@PathVariable Long id, @RequestBody Categoria categoria) {
        return service.update(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}