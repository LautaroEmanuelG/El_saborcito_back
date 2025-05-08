package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @GetMapping
    public List<Usuario> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public Usuario getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) { return service.save(usuario); }
    @PutMapping("/{id}") public Usuario update(@PathVariable Long id, @RequestBody Usuario usuario) { return service.update(id, usuario); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
