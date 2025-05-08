package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.services.ArticuloInsumoService;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class ArticuloInsumoController {
    private final ArticuloInsumoService service;

    @GetMapping
    public List<ArticuloInsumo> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public ArticuloInsumo getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public ArticuloInsumo create(@RequestBody ArticuloInsumo a) { return service.save(a); }
    @PutMapping("/{id}") public ArticuloInsumo update(@PathVariable Long id, @RequestBody ArticuloInsumo a) { return service.update(id, a); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
