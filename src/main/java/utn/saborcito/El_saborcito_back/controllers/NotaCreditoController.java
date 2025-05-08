package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.NotaCredito;
import utn.saborcito.El_saborcito_back.services.NotaCreditoService;

import java.util.List;

@RestController
@RequestMapping("/api/notas-credito")
@RequiredArgsConstructor
public class NotaCreditoController {
    private final NotaCreditoService service;

    @GetMapping
    public List<NotaCredito> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public NotaCredito getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public NotaCredito create(@RequestBody NotaCredito n) { return service.save(n); }
    @PutMapping("/{id}") public NotaCredito update(@PathVariable Long id, @RequestBody NotaCredito n) { return service.update(id, n); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
