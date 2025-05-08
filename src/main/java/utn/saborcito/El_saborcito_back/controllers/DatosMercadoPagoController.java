package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
import utn.saborcito.El_saborcito_back.services.DatosMercadoPagoService;

import java.util.List;

@RestController
@RequestMapping("/api/datos-mercadopago")
@RequiredArgsConstructor
public class DatosMercadoPagoController {
    private final DatosMercadoPagoService service;

    @GetMapping
    public List<DatosMercadoPago> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public DatosMercadoPago getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public DatosMercadoPago create(@RequestBody DatosMercadoPago dmp) { return service.save(dmp); }
    @PutMapping("/{id}") public DatosMercadoPago update(@PathVariable Long id, @RequestBody DatosMercadoPago dmp) { return service.update(id, dmp); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
