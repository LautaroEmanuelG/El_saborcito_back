package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.services.ImagenService;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
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
}