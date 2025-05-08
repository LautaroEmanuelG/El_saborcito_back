package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.services.DomicilioService;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
public class DomicilioController {
    private final DomicilioService service;

    public DomicilioController(DomicilioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Domicilio> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Domicilio getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Domicilio create(@RequestBody Domicilio domicilio) {
        return service.create(domicilio);
    }

    @PutMapping("/{id}")
    public Domicilio update(@PathVariable Long id, @RequestBody Domicilio domicilio) {
        return service.update(id, domicilio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}