package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repo;

    public List<Usuario> findAll() {
        return repo.findAll();
    }

    public Usuario findById(Long id) {
        return repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public Usuario save(Usuario usuario) {
        return repo.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: Usuario no encontrado con ID: " + id);
        }
        usuario.setId(id);
        return repo.save(usuario);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Usuario no encontrado con ID: " + id);
        }
        repo.deleteById(id);
        // Considerar devolver un mensaje aquí si la lógica del controlador no lo hace.
    }

}
