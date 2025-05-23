package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDetalleDTO;
import utn.saborcito.El_saborcito_back.services.ArticuloManufacturadoDetalleService;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-manufacturados")
@RequiredArgsConstructor
public class ArticuloManufacturadoDetalleController {

    private final ArticuloManufacturadoDetalleService service;

    @GetMapping
    public ResponseEntity<List<ArticuloManufacturadoDetalleDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloManufacturadoDetalleDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Se necesita el ID del ArticuloManufacturado padre para crear un detalle.
    // Se pasa como path variable, asumiendo una ruta como
    // /api/detalles-manufacturados/manufacturado/{manufacturadoId}
    // O podría ser un parámetro en el DTO o un query param, según diseño de API.
    @PostMapping("/manufacturado/{manufacturadoId}")
    public ResponseEntity<ArticuloManufacturadoDetalleDTO> create(
            @PathVariable Long manufacturadoId,
            @RequestBody ArticuloManufacturadoDetalleDTO detalleDTO) {
        ArticuloManufacturadoDetalleDTO savedDetalle = service.save(detalleDTO, manufacturadoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDetalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloManufacturadoDetalleDTO> update(
            @PathVariable Long id,
            @RequestBody ArticuloManufacturadoDetalleDTO detalleDTO) {
        ArticuloManufacturadoDetalleDTO updatedDetalle = service.update(id, detalleDTO);
        return ResponseEntity.ok(updatedDetalle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}