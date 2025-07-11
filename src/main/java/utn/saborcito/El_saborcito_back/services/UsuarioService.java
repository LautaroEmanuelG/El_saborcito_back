package utn.saborcito.El_saborcito_back.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.config.security.JwtUtil;
import utn.saborcito.El_saborcito_back.dto.AuthResponseDTO;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.dto.LoginRequest;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.UsuarioMapper;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repo;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LocalidadRepository localidadRepository;
    private final JwtUtil jwtUtil;

    public List<UsuarioDTO> findAll() {
        return repo.findAll().stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    public UsuarioDTO findById(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id));
        return usuarioMapper.toDTO(usuario);
    }

    // ✅ NUEVO: Buscar por Auth0 ID
    public Optional<UsuarioDTO> findByAuth0Id(String auth0Id) {
        return repo.findByAuth0Id(auth0Id)
                .map(usuarioMapper::toDTO);
    }
    public void vincularAuth0Id(Long usuarioId, String auth0Id) {
        Usuario usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setAuth0Id(auth0Id);
        repo.save(usuario);
    }

    // ✅ NUEVO: Buscar por email
    public Optional<UsuarioDTO> findByEmail(String email) {
        return repo.findByEmail(email)
                .map(usuarioMapper::toDTO);
    }

    // ✅ NUEVO: Validar email único (para HU1 y HU4)
    public void validarEmailUnico(String email) {
        if (repo.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un usuario registrado con este email");
        }
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

    // ✅ OK: Baja lógica (para HU7 y HU8)
    public void bajaLogicaUsuario(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id));
        usuario.setEstado(false);
        repo.save(usuario);
    }

    // ✅ OK: Alta lógica (para HU7 y HU8)
    public void altaUsuario(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id));
        usuario.setEstado(true);
        repo.save(usuario);
    }

//    // ✅ MEJORADO: Login que maneja Auth0 y manual
//    public UsuarioDTO login(LoginRequest loginRequest) {
//        if (loginRequest.getEsAuth0Login()) {
//            return loginAuth0(loginRequest.getEmail());
//        } else {
//            return loginManual(loginRequest);
//        }
//    }

//    // ✅ NUEVO: Login manual (HU2 y HU5)
//    private UsuarioDTO loginManual(LoginRequest loginRequest) {
//        Usuario usuario = repo.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
//                        "Email o contraseña inválidos"));
//        // ✅ HU2: Validar estado
//        if (!usuario.getEstado()) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario dado de baja");
//        }
//        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña inválidos");
//        }
//        return usuarioMapper.toDTO(usuario);
//    }

    // ✅ NUEVO: Login Auth0 (HU1 y HU2)
    public UsuarioDTO loginManual(LoginRequest dto) {
        Usuario usuario = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));

        if (!usuario.getEstado()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario dado de baja");
        }

        if (dto.getPassword() == null || !passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }

        return usuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO loginAuth0(String email, String auth0Id) {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no registrado"));

        if (!usuario.getEstado()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario dado de baja");
        }

        if (usuario.getAuth0Id() == null || usuario.getAuth0Id().isEmpty()) {
            // Si el usuario no tiene auth0Id, lo vinculamos
            usuario.setAuth0Id(auth0Id);
            repo.save(usuario);
        } else if (!usuario.getAuth0Id().equals(auth0Id)) {
            // Conflicto: mismo email, diferente Auth0 ID
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado con otro método");
        }

        return usuarioMapper.toDTO(usuario);
    }

    public AuthResponseDTO loginAdmin(LoginRequest dto) {
        log.info("Admin", dto.getEmail());
        // 1. Buscar al usuario por email
        Usuario usuario = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));
        log.info("Usuario encontrado - ID: {}, Rol: {}, Estado: {}",
                usuario.getId(), usuario.getRol(), usuario.getEstado());
        // 2. Validar que sea ADMIN
        if (usuario.getRol() != Rol.ADMIN) {
            log.error("Intento de login como admin fallido - Rol incorrecto: {}", usuario.getRol());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        // 3. Validar estado
        if (!usuario.getEstado()) {
            log.error("Intento de login como admin fallido - Usuario inactivo");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario deshabilitado");
        }

        // 4. Validar contraseña
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
           log.info("Intento de login como admin fallido - Contraseña incorrecta", dto.getPassword());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }

        // 5. Generar token JWT
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
        log.info("Token generado exitosamente para usuario: {}", usuario.getEmail());

        return AuthResponseDTO.builder()
                .message("Inicio de sesión exitoso")
                .token(token)
                .usuario(usuarioMapper.toDTO(usuario))
                .build();
    }

    // ✅ NUEVO: Validar contraseña (para HU3 y HU6)
    public boolean validarContrasenaActual(Long usuarioId, String contraseñaActual) {
        Usuario usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
        return passwordEncoder.matches(contraseñaActual, usuario.getPassword());
    }

    // ✅ NUEVO: Cambiar contraseña (para HU3 y HU6)
    public void cambiarContrasena(Long usuarioId, String nuevaContrasena) {
        Usuario usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        // ✅ Si es empleado y primer login, lo marcamos como completado
        if (usuario instanceof Empleado empleado && Boolean.TRUE.equals(empleado.getPrimerLogin())) {
            empleado.setPrimerLogin(false);
        }
        repo.save(usuario);
    }

    // --- Métodos privados de validación (sin cambios) ---
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
    public void validarAuth0IdUnico(String auth0Id) {
        if (auth0Id != null && !auth0Id.isEmpty() && repo.existsByAuth0Id(auth0Id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un usuario registrado con este auth0Id");
        }
    }
}
