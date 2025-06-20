package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto;
import utn.saborcito.El_saborcito_back.services.ArticuloManufacturadoService;
import utn.saborcito.El_saborcito_back.services.ProduccionAnalisisService;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturados")
@RequiredArgsConstructor
@Slf4j
public class ArticuloManufacturadoController {

    private final ArticuloManufacturadoService service;
    private final ProduccionAnalisisService produccionAnalisisService;

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
     * 🔍 Endpoint para consultar si un artículo manufacturado puede fabricarse
     * Usa el servicio unificado de análisis de producción
     */
    @GetMapping("/{id}/can-be-manufactured")
    public boolean canBeManufactured(@PathVariable Long id) {
        return produccionAnalisisService.puedeProducirseArticuloManufacturado(id);
    }

    // 🔴 NUEVOS ENDPOINTS PARA VALIDACIÓN DE DUPLICADOS

    /**
     * 🔍 Endpoint para validar denominación (solo artículos activos)
     */
    @GetMapping("/validate-denominacion")
    public ResponseEntity<Boolean> validateDenominacion(
            @RequestParam String denominacion,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = service.existsByDenominacion(denominacion, excludeId);
        return ResponseEntity.ok(exists);
    }

    /**
     * 🔍 Endpoint para validar denominación (incluyendo artículos eliminados)
     */
    @GetMapping("/validate-denominacion-all")
    public ResponseEntity<Boolean> validateDenominacionIncludingDeleted(
            @RequestParam String denominacion,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = service.existsByDenominacionIncludingDeleted(denominacion, excludeId);
        return ResponseEntity.ok(exists);
    }

    // 🌟 Endpoint para subir imagen a artículo manufacturado
    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenUploadResponseDto> uploadImagenArticuloManufacturado(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ImagenUploadResponseDto.builder()
                                .message("El archivo no puede estar vacío")
                                .success(false)
                                .build());
            }

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(ImagenUploadResponseDto.builder()
                                .message("El archivo debe ser una imagen")
                                .success(false)
                                .build());
            }

            // Subir imagen y asociar al artículo
            ImagenUploadResponseDto response = service.uploadAndAssignImagen(id, file);

            if (response.getSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            log.error("Error al procesar imagen para artículo manufacturado {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImagenUploadResponseDto.builder()
                            .message("Error interno del servidor: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }
}