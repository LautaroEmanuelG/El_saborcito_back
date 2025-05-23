package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.mappers.UsuarioMapper;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repo;
    private final UsuarioMapper usuarioMapper;

    public List<UsuarioDTO> findAll() {
        return repo.findAll().stream().map(usuarioMapper::toDTO).toList();
    }

    public UsuarioDTO findById(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
        return usuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        return usuarioMapper.toDTO(repo.save(usuario));
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: Usuario no encontrado con ID: " + id);
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setId(id);
        return usuarioMapper.toDTO(repo.save(usuario));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Usuario no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    // Métodos antiguos para compatibilidad, pero ahora no se usan en el controlador
    // public Usuario save(Usuario usuario) { ... }
    // public Usuario update(Long id, Usuario usuario) { ... }
}
