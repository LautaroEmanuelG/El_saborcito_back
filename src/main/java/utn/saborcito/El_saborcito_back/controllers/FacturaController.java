package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.services.FacturaService;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {
    private final FacturaService service;

    @GetMapping
    public List<Factura> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public Factura getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Factura create(@RequestBody Factura f) { return service.save(f); }
    @PutMapping("/{id}") public Factura update(@PathVariable Long id, @RequestBody Factura f) { return service.update(id, f); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
