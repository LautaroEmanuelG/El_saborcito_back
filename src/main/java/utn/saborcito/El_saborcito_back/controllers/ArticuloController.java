package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.dto.ProduccionAnalisisDTO;
import utn.saborcito.El_saborcito_back.dto.ProduccionAnalisisRequestDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.services.ArticuloService;
import utn.saborcito.El_saborcito_back.services.ProduccionAnalisisService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articulos")
@RequiredArgsConstructor
public class ArticuloController {

    private final ArticuloService service;
    private final ProduccionAnalisisService produccionAnalisisService;

    @GetMapping
    public List<ArticuloDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Articulo getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Articulo create(@RequestBody Articulo articulo) {
        return service.save(articulo);
    }

    @PutMapping("/{id}")
    public Articulo update(@PathVariable Long id, @RequestBody Articulo articulo) {
        return service.update(id, articulo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Endpoints para manejar elementos eliminados
    @GetMapping("/deleted")
    public List<ArticuloDTO> getAllDeleted() {
        return service.findAllDeleted();
    }

    @GetMapping("/deleted/{id}")
    public Articulo getOneDeleted(@PathVariable Long id) {
        return service.findByIdDeleted(id);
    }

    @PostMapping("/deleted/{id}/restore")
    public void restore(@PathVariable Long id) {
        service.restoreDeleted(id);
    }

    @DeleteMapping("/deleted/{id}")
    public void permanentDelete(@PathVariable Long id) {
        service.permanentDelete(id);
    }

    /**
     * 🎯 Endpoint para analizar si es posible producir un ticket completo
     * Este endpoint recibe un array de IDs de artículos con sus cantidades y
     * analiza:
     * 1. Para artículos manufacturados: si hay insumos suficientes para producirlos
     * 2. Para artículos insumo (bebidas, etc.): si hay stock suficiente
     *
     * @param requestDTO Objeto con la lista de artículos y cantidades a analizar
     * @return Objeto con el resultado del análisis de producción
     */
    @PostMapping("/analizar-produccion")
    public ProduccionAnalisisDTO analizarProduccion(@RequestBody ProduccionAnalisisRequestDTO requestDTO) {
        // Convertir la lista de objetos ArticuloCantidad a un mapa de ID -> cantidad
        Map<Long, Integer> articulosMap = new HashMap<>();

        for (ProduccionAnalisisRequestDTO.ArticuloCantidad articulo : requestDTO.getArticulos()) {
            // Si ya existe el ID en el mapa, sumar las cantidades (para artículos
            // repetidos)
            articulosMap.merge(articulo.getArticuloId(), articulo.getCantidad(), Integer::sum);
        }

        // Llamar al servicio unificado que hace el análisis real
        return produccionAnalisisService.analizarProduccionCompleta(articulosMap);
    }
}