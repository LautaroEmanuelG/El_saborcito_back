package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.services.ArticuloManufacturadoService;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturados")
@RequiredArgsConstructor
public class ArticuloManufacturadoController {

    private final ArticuloManufacturadoService service;

    @GetMapping
    public List<ArticuloManufacturadoDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ArticuloManufacturadoDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ArticuloManufacturadoDTO create(@RequestBody ArticuloManufacturadoDTO articuloDTO) {
        return service.save(articuloDTO);
    }

    @PutMapping("/{id}")
    public ArticuloManufacturadoDTO update(@PathVariable Long id, @RequestBody ArticuloManufacturadoDTO articuloDTO) {
        return service.update(id, articuloDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}