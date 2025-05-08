package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.services.ArticuloManufacturadoDetalleService;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-manufacturados")
@RequiredArgsConstructor
public class ArticuloManufacturadoDetalleController {

    private final ArticuloManufacturadoDetalleService service;

    @GetMapping
    public List<ArticuloManufacturadoDetalle> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ArticuloManufacturadoDetalle getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ArticuloManufacturadoDetalle create(@RequestBody ArticuloManufacturadoDetalle detalle) {
        return service.save(detalle);
    }

    @PutMapping("/{id}")
    public ArticuloManufacturadoDetalle update(@PathVariable Long id, @RequestBody ArticuloManufacturadoDetalle detalle) {
        return service.update(id, detalle);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}