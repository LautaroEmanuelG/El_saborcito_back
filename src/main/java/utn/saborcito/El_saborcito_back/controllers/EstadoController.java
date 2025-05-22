package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Estado;
import utn.saborcito.El_saborcito_back.services.EstadoService;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService service;

    @GetMapping
    public List<Estado> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Estado getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/nombre/{nombre}")
    public Estado getByNombre(@PathVariable String nombre) {
        return service.findByNombre(nombre);
    }

    @PostMapping
    public Estado create(@RequestBody Estado estado) {
        return service.save(estado);
    }

    @PutMapping("/{id}")
    public Estado update(@PathVariable Long id, @RequestBody Estado estado) {
        return service.update(id, estado);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
