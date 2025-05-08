package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Pais;
import utn.saborcito.El_saborcito_back.services.PaisService;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
public class PaisController {
    private final PaisService service;

    public PaisController(PaisService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pais> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Pais getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Pais create(@RequestBody Pais pais) {
        return service.create(pais);
    }

    @PutMapping("/{id}")
    public Pais update(@PathVariable Long id, @RequestBody Pais pais) {
        return service.update(id, pais);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}