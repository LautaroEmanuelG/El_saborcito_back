package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Provincia;
import utn.saborcito.El_saborcito_back.services.ProvinciaService;

import java.util.List;

@RestController
@RequestMapping("/api/provincias")
public class ProvinciaController {
    private final ProvinciaService service;

    public ProvinciaController(ProvinciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Provincia> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Provincia getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Provincia create(@RequestBody Provincia provincia) {
        return service.create(provincia);
    }

    @PutMapping("/{id}")
    public Provincia update(@PathVariable Long id, @RequestBody Provincia provincia) {
        return service.update(id, provincia);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}