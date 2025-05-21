package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.ClienteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repo;

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Cliente findById(Long id) {
        return repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + id));
    }

    public Cliente save(Cliente cliente) {
        if (cliente.getUsuario() == null || cliente.getUsuario().getRol() != Rol.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario asociado al cliente debe existir y tener el rol CLIENTE.");
        }
        return repo.save(cliente);
    }

    public Cliente update(Long id, Cliente clienteActualizado) {
        Cliente existingCliente = findById(id); // findById ya lanza la excepción si no se encuentra

        // Validar y actualizar el usuario asociado
        if (clienteActualizado.getUsuario() == null || clienteActualizado.getUsuario().getRol() != Rol.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario asociado al cliente debe existir y tener el rol CLIENTE.");
        }

        Usuario usuarioExistente = existingCliente.getUsuario();
        Usuario usuarioActualizado = clienteActualizado.getUsuario();

        if (usuarioExistente != null && usuarioActualizado != null) {
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellido(usuarioActualizado.getApellido());
            usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
            // CORRECCIÓN: Usar getEstado() y setEstado() para el campo 'estado' de Usuario
            if (usuarioActualizado.getEstado() != null) { // Solo actualizar si se provee un nuevo estado
                usuarioExistente.setEstado(usuarioActualizado.getEstado());
            }
            // La actualización del rol debe manejarse con cuidado y posiblemente en
            // UsuarioService
            // Por ahora, no se permite cambiar el rol del usuario desde ClienteService.
        }
        existingCliente.setUsuario(usuarioExistente);

        // El historial de pedidos se gestiona internamente.

        return repo.save(existingCliente);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Cliente no encontrado con ID: " + id);
        repo.deleteById(id);
    }
}
