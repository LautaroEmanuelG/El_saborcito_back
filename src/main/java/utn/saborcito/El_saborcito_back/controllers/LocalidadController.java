package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.services.LocalidadService;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController {
    private final LocalidadService service;

    public LocalidadController(LocalidadService service) {
        this.service = service;
    }

    @GetMapping
    public List<Localidad> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Localidad getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Localidad create(@RequestBody Localidad localidad) {
        return service.create(localidad);
    }

    @PutMapping("/{id}")
    public Localidad update(@PathVariable Long id, @RequestBody Localidad localidad) {
        return service.update(id, localidad);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}