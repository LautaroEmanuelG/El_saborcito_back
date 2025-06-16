package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO;
import utn.saborcito.El_saborcito_back.services.ImagenService;
import utn.saborcito.El_saborcito_back.services.PromocionService;
import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
@Slf4j
public class PromocionController {
    private final PromocionService service;
    private final ImagenService imagenService;

    @GetMapping
    public List<PromocionDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/deleted")
    public List<PromocionDTO> getDeleted() {
        return service.findDeleted();
    }

    @PostMapping("/deleted/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restore(@PathVariable Long id) {
        service.restore(id);
    }

    @GetMapping("/{id}")
    public PromocionDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromocionDTO create(@RequestBody PromocionDTO promocionDTO) {
        return service.save(promocionDTO);
    }

    @PutMapping("/{id}")
    public PromocionDTO update(@PathVariable Long id, @RequestBody PromocionDTO promocionDTO) {
        return service.update(id, promocionDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/vigentes/{sucursalId}")
    public List<PromocionDTO> getPromocionesVigentes(@PathVariable Long sucursalId) {
        return service.findPromocionesVigentes(sucursalId);
    }

    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenUploadResponseDto> uploadImagenPromocion(
        @PathVariable Long id,
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

            // Subir imagen a Cloudinary
            ImagenUploadResponseDto uploadResponse = imagenService.uploadImagen(file);
            
            if (!uploadResponse.getSuccess()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(uploadResponse);
            }

            // Asociar imagen a la promoción
            service.updateImagenPromocion(id, uploadResponse.getImagenId());

            uploadResponse.setMessage("Imagen asociada exitosamente a la promoción");
            return ResponseEntity.ok(uploadResponse);

        } catch (Exception e) {
            log.error("Error al procesar imagen para promoción {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ImagenUploadResponseDto.builder()
                    .message("Error interno del servidor")
                    .success(false)
                    .build());
        }
    }
}