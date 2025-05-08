package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.services.UnidadMedidaService;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
public class UnidadMedidaController {

    private final UnidadMedidaService service;

    @GetMapping
    public List<UnidadMedida> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UnidadMedida getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public UnidadMedida create(@RequestBody UnidadMedida unidad) {
        return service.save(unidad);
    }

    @PutMapping("/{id}")
    public UnidadMedida update(@PathVariable Long id, @RequestBody UnidadMedida unidad) {
        return service.update(id, unidad);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}