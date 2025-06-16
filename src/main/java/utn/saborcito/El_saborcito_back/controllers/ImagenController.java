package utn.saborcito.El_saborcito_back.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.services.ImagenService;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestión de Imágenes", description = "Endpoints para gestionar imágenes y uploads con Cloudinary")
public class ImagenController {

    private final ImagenService service;

    @GetMapping
    public List<Imagen> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Imagen getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Imagen create(@RequestBody Imagen imagen) {
        return service.save(imagen);
    }

    @PutMapping("/{id}")
    public Imagen update(@PathVariable Long id, @RequestBody Imagen imagen) {
        return service.update(id, imagen);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Nuevos endpoints para Cloudinary

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "📸 Subir imagen",
        description = "Sube una imagen a Cloudinary y crea la entidad en la base de datos",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Imagen subida exitosamente",
                content = @Content(schema = @Schema(implementation = ImagenUploadResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Archivo inválido o error en la subida"
            )
        }
    )
    public ResponseEntity<ImagenUploadResponseDto> uploadImagen(
        @Parameter(description = "Archivo de imagen a subir", required = true)
        @RequestParam("file") MultipartFile file
    ) {
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

            ImagenUploadResponseDto response = service.uploadImagen(file);
            
            if (response.getSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            log.error("Error al procesar upload de imagen: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ImagenUploadResponseDto.builder()
                    .message("Error interno del servidor")
                    .success(false)
                    .build());
        }
    }

    @PutMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "🔄 Actualizar imagen",
        description = "Actualiza una imagen existente subiendo un nuevo archivo a Cloudinary",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Imagen actualizada exitosamente",
                content = @Content(schema = @Schema(implementation = ImagenUploadResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Imagen no encontrada"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Archivo inválido o error en la actualización"
            )
        }
    )
    public ResponseEntity<ImagenUploadResponseDto> updateImagen(
        @Parameter(description = "ID de la imagen a actualizar", required = true)
        @PathVariable Long id,
        @Parameter(description = "Nuevo archivo de imagen", required = true)
        @RequestParam("file") MultipartFile file
    ) {
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

            ImagenUploadResponseDto response = service.updateImagen(id, file);
            
            if (response.getSuccess()) {
                return ResponseEntity.ok(response);
            } else if (response.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            log.error("Error al actualizar imagen {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ImagenUploadResponseDto.builder()
                    .message("Error interno del servidor")
                    .success(false)
                    .build());
        }
    }
}