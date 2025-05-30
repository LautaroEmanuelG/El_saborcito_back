package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosClienteDTO;
import utn.saborcito.El_saborcito_back.dto.CambiarPasswordDTO;
import utn.saborcito.El_saborcito_back.dto.ClienteDTO;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.ClienteMapper;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.ClienteRepository;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repo;
    private final ClienteMapper clienteMapper;
    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

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

        // CORRECCIÓN 21: Validar que no se permita setear manualmente historialPedidos.
        if (cliente.getHistorialPedidos() != null && !cliente.getHistorialPedidos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El historial de pedidos no se puede asignar manualmente al crear un cliente.");
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

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public void cambiarPassword(Long usuarioId, CambiarPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!isValidPassword(dto.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    public ClienteDTO actualizarDatosCliente(Long clienteId, ActualizarDatosClienteDTO dto) {
        Cliente cliente = repo.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Usuario usuario = cliente.getUsuario();

        // Campos modificables por el administrador
        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());

        // Validar email y actualizar si es diferente
        if (!dto.getEmail().equals(usuario.getEmail())) {
            if (!isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }

            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
            }

            usuario.setEmail(dto.getEmail());
        }

        // Actualizar dirección si corresponde
        if (dto.getCalle() != null && dto.getLocalidadId() != null) {
            Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

            Domicilio domicilio = Optional.ofNullable(usuario.getDomicilio()).orElse(new Domicilio());
            domicilio.setCalle(dto.getCalle());
            domicilio.setNumero(dto.getNumero());
            domicilio.setCp(dto.getCp());
            domicilio.setLocalidad(localidad);
            domicilio.setUsuario(usuario);

            domicilio = domicilioRepository.save(domicilio);
            usuario.setDomicilio(domicilio);
        }

        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return clienteMapper.toDTO(repo.save(cliente));
    }

    public void toggleEstado(Long clienteId) {
        Cliente cliente = repo.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        Usuario usuario = cliente.getUsuario();
        usuario.setEstado(!usuario.getEstado());
        usuarioRepository.save(usuario);
    }
}
