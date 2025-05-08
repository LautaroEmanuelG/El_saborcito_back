package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.services.SucursalService;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {
    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sucursal> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Sucursal getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Sucursal create(@RequestBody Sucursal sucursal) {
        return service.create(sucursal);
    }

    @PutMapping("/{id}")
    public Sucursal update(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        return service.update(id, sucursal);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
