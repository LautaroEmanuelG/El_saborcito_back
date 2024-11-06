package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "contraseña") String contraseña
    ) {
        Optional<Usuario> usuario = usuarioService.login(email, contraseña);
        if (usuario.isPresent()) {
            return ResponseEntity.ok("Login Exitoso. Nombre usuario: " + usuario.get().getNombre() + " Rol: " + usuario.get().getRol());
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas.");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }
}
