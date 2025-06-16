package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.CloudinaryResponseDto;
import utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImagenService {

    private final ImagenRepository repo;
    private final CloudinaryService cloudinaryService;

    // Métodos originales del servicio
    public List<Imagen> findAll() {
        return repo.findAll();
    }

    public Imagen findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada"));
    }

    public Imagen save(Imagen imagen) {
        return repo.save(imagen);
    }

    public Imagen update(Long id, Imagen dto) {
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada");
        }
        repo.deleteById(id);
    }

    // Nuevos métodos para integración con Cloudinary

    /**
     * Sube una imagen a Cloudinary y crea la entidad Imagen
     */
    public ImagenUploadResponseDto uploadImagen(MultipartFile file) {
        try {
            // Subir imagen a Cloudinary
            CloudinaryResponseDto cloudinaryResponse = cloudinaryService.uploadImage(file);
            
            // Crear entidad Imagen
            Imagen imagen = Imagen.builder()
                .url(cloudinaryResponse.getSecureUrl())
                .build();
            
            // Guardar en base de datos
            Imagen imagenGuardada = repo.save(imagen);
            
            log.info("Imagen creada exitosamente con ID: {}", imagenGuardada.getId());
            
            return ImagenUploadResponseDto.builder()
                .imagenId(imagenGuardada.getId())
                .url(imagenGuardada.getUrl())
                .publicId(cloudinaryResponse.getPublicId())
                .message("Imagen subida exitosamente")
                .success(true)
                .build();
                
        } catch (IOException e) {
            log.error("Error al subir imagen: {}", e.getMessage(), e);
            return ImagenUploadResponseDto.builder()
                .message("Error al subir imagen: " + e.getMessage())
                .success(false)
                .build();
        } catch (Exception e) {
            log.error("Error inesperado al procesar imagen: {}", e.getMessage(), e);
            return ImagenUploadResponseDto.builder()
                .message("Error inesperado al procesar imagen")
                .success(false)
                .build();
        }
    }

    /**
     * Actualiza una imagen existente
     */
    public ImagenUploadResponseDto updateImagen(Long imagenId, MultipartFile file) {
        try {
            Optional<Imagen> imagenOpt = repo.findById(imagenId);
            
            if (imagenOpt.isEmpty()) {
                return ImagenUploadResponseDto.builder()
                    .message("Imagen no encontrada")
                    .success(false)
                    .build();
            }
            
            Imagen imagen = imagenOpt.get();
            
            // Subir nueva imagen a Cloudinary
            CloudinaryResponseDto cloudinaryResponse = cloudinaryService.uploadImage(file);
            
            // Actualizar URL
            imagen.setUrl(cloudinaryResponse.getSecureUrl());
            
            // Guardar cambios
            Imagen imagenActualizada = repo.save(imagen);
            
            log.info("Imagen actualizada exitosamente con ID: {}", imagenActualizada.getId());
            
            return ImagenUploadResponseDto.builder()
                .imagenId(imagenActualizada.getId())
                .url(imagenActualizada.getUrl())
                .publicId(cloudinaryResponse.getPublicId())
                .message("Imagen actualizada exitosamente")
                .success(true)
                .build();
                
        } catch (IOException e) {
            log.error("Error al actualizar imagen: {}", e.getMessage(), e);
            return ImagenUploadResponseDto.builder()
                .message("Error al actualizar imagen: " + e.getMessage())
                .success(false)
                .build();
        } catch (Exception e) {
            log.error("Error inesperado al actualizar imagen: {}", e.getMessage(), e);
            return ImagenUploadResponseDto.builder()
                .message("Error inesperado al actualizar imagen")
                .success(false)
                .build();
        }
    }

    /**
     * Crea una entidad Imagen directamente con una URL (para casos donde ya tienes la URL)
     */
    public Imagen createImagenFromUrl(String url) {
        Imagen imagen = Imagen.builder()
            .url(url)
            .build();
        
        return repo.save(imagen);
    }
}