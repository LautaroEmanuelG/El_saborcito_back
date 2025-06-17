package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto;
import utn.saborcito.El_saborcito_back.services.ArticuloInsumoService;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
@Slf4j
public class ArticuloInsumoController {
    private final ArticuloInsumoService articuloInsumoService;

    @GetMapping
    public ResponseEntity<List<ArticuloInsumoDTO>> getAll() {
        return ResponseEntity.ok(articuloInsumoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloInsumoDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloInsumoService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ArticuloInsumoDTO> create(@RequestBody ArticuloInsumoDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(articuloInsumoService.save(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloInsumoDTO> update(@PathVariable Long id, @RequestBody ArticuloInsumoDTO dto) {
        try {
            return ResponseEntity.ok(articuloInsumoService.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            articuloInsumoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/es-para-elaborar")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllEsParaElaborar() {
        return ResponseEntity.ok(articuloInsumoService.findAllByEsParaElaborarTrue());
    }

    @GetMapping("/no-es-para-elaborar")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllNoEsParaElaborar() {
        return ResponseEntity.ok(articuloInsumoService.findAllByEsParaElaborarFalse());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(articuloInsumoService.findAllByCategoriaId(categoriaId));
    }

    // Endpoints para manejar elementos eliminados
    @GetMapping("/deleted")
    public ResponseEntity<List<ArticuloInsumoDTO>> getAllDeleted() {
        return ResponseEntity.ok(articuloInsumoService.findAllDeleted());
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ArticuloInsumoDTO> getOneDeleted(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloInsumoService.findByIdDeleted(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/can-be-sold")
    public ResponseEntity<Boolean> canBeSold(@PathVariable Long id) {
        try {
            boolean puedeVenderse = articuloInsumoService.puedeVenderse(id);
            return ResponseEntity.ok(puedeVenderse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/deleted/{id}/restore")
    public ResponseEntity<ArticuloInsumoDTO> restore(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloInsumoService.restoreDeleted(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<Void> permanentDelete(@PathVariable Long id) {
        try {
            articuloInsumoService.permanentDelete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 🌟 Nuevo endpoint para subir imagen a artículo insumo
    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenUploadResponseDto> uploadImagenArticuloInsumo(
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
            ImagenUploadResponseDto response = articuloInsumoService.uploadAndAssignImagen(id, file);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al procesar imagen para artículo insumo {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImagenUploadResponseDto.builder()
                            .message("Error interno del servidor: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }
}
