package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repo;
    private final ClienteMapper clienteMapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    // --- HU06: Obtener todos los clientes (administrador) ---
    public List<Cliente> findAll() {
        return repo.findAll();
    }

    // --- HU06: Obtener cliente por ID (administrador o detalle) ---
    public Cliente findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id));
    }

    // ✅ NUEVO: Buscar por Auth0 ID - para sincronización (HU1, HU2)
    public Optional<ClienteDTO> findByAuth0Id(String auth0Id) {
        return usuarioService.findByAuth0Id(auth0Id)
                .map(usuarioDTO -> {
                    Cliente cliente = repo.findById(usuarioDTO.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
                    return clienteMapper.toDTO(cliente);
                });
    }

    // --- HU01: Registro flexible de cliente --
    // --- Registro fusionado (manual o Auth0) ---
    public ClienteDTO registrarClienteFlexible(RegistroClienteDTO dto) {
        usuarioService.validarEmailUnico(dto.getEmail());

        if (!usuarioService.isValidEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
        }

        if (!dto.getEsAuth0()) {
            if (dto.getPassword() == null || dto.getConfirmarPassword() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña obligatoria para registro manual");
            }

            if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
            }

            if (!usuarioService.isValidPassword(dto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
            }
        }

        return crearCliente(dto);
    }

    private ClienteDTO crearCliente(RegistroClienteDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        if (dto.getEsAuth0()) {
            usuario.setAuth0Id(dto.getAuth0Id());
        } else {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        usuario = usuarioRepository.save(usuario);

        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            usuarioService.procesarYValidarDomicilios(usuario, dto.getDomicilios());
        }

        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setId(usuario.getId());
        cliente.setDomicilios(usuario.getDomicilios());
        cliente.setHistorialPedidos(new ArrayList<>());
        return clienteMapper.toDTO(repo.save(cliente));
    }


    // --- HU03: Actualización de datos del cliente ---
    public ClienteDTO updateCliente(Long id, ActualizarDatosClienteDTO dto) {
        Cliente clienteExistente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        Usuario usuario = clienteExistente;

        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());

        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (!usuarioService.isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }
            usuarioService.validarEmailUnico(dto.getEmail());
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            usuarioService.procesarYValidarDomicilios(usuario, dto.getDomicilios());
        }

        if (dto.getNuevaContraseña() != null && !dto.getNuevaContraseña().isBlank()) {
            validarCambioContrasena(usuario.getId(), dto);
            usuarioService.cambiarContrasena(usuario.getId(), dto.getNuevaContraseña());
        } else {
            usuario.setFechaUltimaModificacion(LocalDateTime.now());
            usuarioRepository.save(usuario);
        }

        return clienteMapper.toDTO(clienteExistente);
    }

    // ✅ NUEVO: Validación de cambio de contraseña (HU3)
    private void validarCambioContrasena(Long usuarioId, ActualizarDatosClienteDTO dto) {
        if (!dto.getNuevaContraseña().equals(dto.getConfirmarContraseña())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!usuarioService.isValidPassword(dto.getNuevaContraseña())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }

        if (!usuarioService.validarContrasenaActual(usuarioId, dto.getContraseñaActual())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La contraseña actual es incorrecta");
        }
    }

    // --- HU02: Login delegado a UsuarioService ---
//    public UsuarioDTO loginCliente(LoginRequest loginRequest) {
//        UsuarioDTO usuario = usuarioService.login(loginRequest);
//
//        // ✅ HU2: Validar que sea cliente
//        if (!usuario.getRol().equals(Rol.CLIENTE)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//                    "Solo los clientes pueden iniciar sesión aquí");
//        }
//
//        // ✅ HU2: Verificar estado activo (ya manejado en UsuarioService)
//        return usuario;
//    }
    public UsuarioDTO loginCliente(LoginRequest loginRequest){
        return usuarioService.loginAuth0(loginRequest.getEmail());
    }
    // --- HU07: Baja lógica del cliente ---
    public void bajaLogicaCliente(Long id) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + id));
        usuarioService.bajaLogicaUsuario(cliente.getId());
    }

    // --- HU07: Alta lógica del cliente ---
    public void altaCliente(Long id) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + id));
        usuarioService.altaUsuario(cliente.getId());
    }

    // --- Sincronización desde Auth0 ---
    public ClienteDTO sincronizarAuth0Usuario(RegistroAuth0DTO auth0User) {
        Optional<ClienteDTO> clienteExistente = findByAuth0Id(auth0User.getSub());

        if (clienteExistente.isPresent()) {
            return actualizarDatosAuth0(clienteExistente.get(), auth0User);
        } else {
            RegistroClienteDTO dto = new RegistroClienteDTO();
            dto.setNombre(auth0User.getGivenName());
            dto.setApellido(auth0User.getFamilyName());
            dto.setEmail(auth0User.getEmail());
            dto.setTelefono(null);
            dto.setFechaNacimiento(null);
            dto.setDomicilios(auth0User.getDomicilios());
            dto.setEsAuth0(true);
            dto.setAuth0Id(auth0User.getSub());
            return registrarClienteFlexible(dto);
        }
    }

    private ClienteDTO actualizarDatosAuth0(ClienteDTO clienteDTO, RegistroAuth0DTO auth0User) {
        boolean datosActualizados = false;

        if (!clienteDTO.getNombre().equals(auth0User.getGivenName())) {
            clienteDTO.setNombre(auth0User.getGivenName());
            datosActualizados = true;
        }
        if (!clienteDTO.getApellido().equals(auth0User.getFamilyName())) {
            clienteDTO.setApellido(auth0User.getFamilyName());
            datosActualizados = true;
        }
        if (!clienteDTO.getEmail().equals(auth0User.getEmail())) {
            usuarioService.validarEmailUnico(auth0User.getEmail());
            clienteDTO.setEmail(auth0User.getEmail());
            datosActualizados = true;
        }
        if (datosActualizados) {
            Cliente clienteEntity = clienteMapper.toEntity(clienteDTO);
            repo.save(clienteEntity);
        }
        return clienteDTO;
    }
}