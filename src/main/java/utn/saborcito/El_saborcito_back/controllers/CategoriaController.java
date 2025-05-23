package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.services.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public List<CategoriaDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public CategoriaDTO create(@RequestBody CategoriaDTO categoriaDTO) {
        return service.save(categoriaDTO);
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        return service.update(id, categoriaDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}