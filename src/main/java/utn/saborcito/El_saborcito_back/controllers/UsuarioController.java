package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) {
        return service.save(usuarioDTO);
    }

    @PutMapping("/{id}")
    public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return service.update(id, usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
