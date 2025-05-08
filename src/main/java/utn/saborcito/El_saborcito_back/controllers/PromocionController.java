package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.services.PromocionService;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {
    private final PromocionService service;

    @GetMapping
    public List<Promocion> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public Promocion getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Promocion create(@RequestBody Promocion p) { return service.save(p); }
    @PutMapping("/{id}") public Promocion update(@PathVariable Long id, @RequestBody Promocion p) { return service.update(id, p); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
