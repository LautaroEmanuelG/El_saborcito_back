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

    @GetMapping("/categoria/{categoriaId}")
    public List<ArticuloManufacturadoDTO> getAllByCategoria(@PathVariable Long categoriaId) {
        return service.findAllByCategoriaId(categoriaId);
    }

    // Endpoints para manejar elementos eliminados
    @GetMapping("/deleted")
    public List<ArticuloManufacturadoDTO> getAllDeleted() {
        return service.findAllDeleted();
    }

    @GetMapping("/deleted/{id}")
    public ArticuloManufacturadoDTO getOneDeleted(@PathVariable Long id) {
        return service.findByIdDeleted(id);
    }

    @PostMapping("/deleted/{id}/restore")
    public ArticuloManufacturadoDTO restore(@PathVariable Long id) {
        return service.restoreDeleted(id);
    }

    @DeleteMapping("/deleted/{id}")
    public void permanentDelete(@PathVariable Long id) {
        service.permanentDelete(id);
    }

    /**
     * Endpoint para consultar si un artículo manufacturado puede fabricarse (tiene
     * insumos suficientes).
     * Ejemplo: GET /api/manufacturados/{id}/can-be-manufactured
     */
    @GetMapping("/{id}/can-be-manufactured")
    public boolean canBeManufactured(@PathVariable Long id) {
        return service.canBeManufactured(id);
    }
}