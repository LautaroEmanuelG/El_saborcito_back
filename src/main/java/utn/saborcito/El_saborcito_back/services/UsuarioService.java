package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosClienteDTO;
import utn.saborcito.El_saborcito_back.dto.RegistroClienteDTO;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.UsuarioMapper;
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
public class UsuarioService {
    private final UsuarioRepository repo;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LocalidadRepository localidadRepository;
    private final DomicilioRepository domicilioRepository;
    private final ClienteRepository clienteRepository;

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

    public Cliente registrarCliente(RegistroClienteDTO dto) {
        // Validaciones
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!isValidEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
        }

        if (!isValidPassword(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }

        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        // Guardar usuario primero
        usuario = repo.save(usuario);

        // Crear domicilio si corresponde
        Domicilio domicilio = null;
        if (dto.getCalle() != null && dto.getLocalidadId() != null) {
            Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

            domicilio = new Domicilio();
            domicilio.setCalle(dto.getCalle());
            domicilio.setNumero(dto.getNumero());
            domicilio.setCp(dto.getCp());
            domicilio.setLocalidad(localidad);
            domicilio.setUsuario(usuario);

            domicilio = domicilioRepository.save(domicilio);
        }

        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setId(usuario.getId()); // ID compartido
        cliente.setUsuario(usuario);
        cliente.setDomicilio(domicilio);

        return clienteRepository.save(cliente);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public UsuarioDTO login(String email, String password) {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña inválidos"));

        if (!usuario.getEstado()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario dado de baja");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña inválidos");
        }

        return usuarioMapper.toDTO(usuario);
    }

    //tendria que ir en cliente service, pero lo dejo aca por ahora
    public UsuarioDTO actualizarDatosCliente(Long usuarioId, ActualizarDatosClienteDTO dto) {
        Usuario usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!usuario.getRol().equals(Rol.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo los clientes pueden realizar esta acción");
        }

        // Validar si cambia la contraseña
        if (dto.getNuevaContraseña() != null && !dto.getNuevaContraseña().isBlank()) {
            if (!dto.getNuevaContraseña().equals(dto.getConfirmarContraseña())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
            }

            if (!passwordEncoder.matches(dto.getContraseñaActual(), usuario.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
            }

            usuario.setPassword(passwordEncoder.encode(dto.getNuevaContraseña()));
        }

        // Actualizar datos básicos
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());

        // Actualizar o crear domicilio si corresponde
        if (dto.getCalle() != null && dto.getLocalidadId() != null) {
            Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

            Domicilio domicilio = Optional.ofNullable(usuario.getDomicilio()).orElse(new Domicilio());
            domicilio.setCalle(dto.getCalle());
            domicilio.setNumero(dto.getNumero());
            domicilio.setCp(dto.getCp());
            domicilio.setLocalidad(localidad);
            domicilio.setUsuario(usuario);

            domicilioRepository.save(domicilio);
            usuario.setDomicilio(domicilio);
        }

        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuario = repo.save(usuario);

        return usuarioMapper.toDTO(usuario);
    }
    // Métodos antiguos para compatibilidad, pero ahora no se usan en el controlador
    // public Usuario save(Usuario usuario) { ... }
    // public Usuario update(Long id, Usuario usuario) { ... }
}
