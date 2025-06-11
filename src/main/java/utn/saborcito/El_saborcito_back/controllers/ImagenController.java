package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.services.CloudinaryService;
import utn.saborcito.El_saborcito_back.services.ImagenService;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService service;
    private final CloudinaryService cloudinaryService;

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

    @PostMapping("/upload")
    public Imagen uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file);
            Imagen imagen = new Imagen();
            imagen.setUrl(url);
            return service.save(imagen);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al subir la imagen: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Imagen update(@PathVariable Long id, @RequestBody Imagen imagen) {
        return service.update(id, imagen);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @DeleteMapping("/cloudinary")
    public ResponseEntity<String> deleteFromCloudinary(@RequestParam String publicId) {
        String result = cloudinaryService.deleteImage(publicId);
        if ("ok".equals(result)) {
            return ResponseEntity.ok("Imagen eliminada correctamente de Cloudinary");
        } else {
            return ResponseEntity.status(400).body("No se pudo eliminar la imagen de Cloudinary");
        }
    }
}