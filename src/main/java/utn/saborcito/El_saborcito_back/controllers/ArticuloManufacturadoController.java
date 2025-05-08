package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.services.ArticuloManufacturadoService;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturados")
@RequiredArgsConstructor
public class ArticuloManufacturadoController {

    private final ArticuloManufacturadoService service;

    @GetMapping
    public List<ArticuloManufacturado> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ArticuloManufacturado getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ArticuloManufacturado create(@RequestBody ArticuloManufacturado articulo) {
        return service.save(articulo);
    }

    @PutMapping("/{id}")
    public ArticuloManufacturado update(@PathVariable Long id, @RequestBody ArticuloManufacturado articulo) {
        return service.update(id, articulo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}