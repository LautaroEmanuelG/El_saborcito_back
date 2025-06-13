package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.dto.LoginRequest;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.mappers.UsuarioMapper;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repo;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LocalidadRepository localidadRepository;

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
    public UsuarioDTO loginClienteManual(LoginRequest dto) {
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

    public UsuarioDTO loginClienteAuth0(String email) {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no registrado"));

        if (!usuario.getEstado()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario dado de baja");
        }

        if (usuario.getAuth0Id() == null || usuario.getAuth0Id().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este usuario no se autentica con Auth0");
        }

        return usuarioMapper.toDTO(usuario);
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
        repo.save(usuario);
    }

    //Cuenta cuántos domicilios tienen el campo principal en true
    public void validarUnSoloDomicilioPrincipal(List<Domicilio> domicilios) {
        long principales = domicilios.stream()
                .filter(d -> Boolean.TRUE.equals(d.getPrincipal()))
                .count();

        if (principales > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Solo puede haber un domicilio principal por usuario.");
        }
    }

    //Valida que haya al menos un domicilio marcado como principal
    public void validarAlMenosUnPrincipal(List<Domicilio> domicilios) {
        boolean tienePrincipal = domicilios.stream()
                .anyMatch(d -> Boolean.TRUE.equals(d.getPrincipal()));

        if (!tienePrincipal) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe haber al menos un domicilio marcado como principal.");
        }
    }


    public void procesarYValidarDomicilios(Usuario usuario, List<DomicilioDTO> domiciliosDTOs) {
        if (domiciliosDTOs == null || domiciliosDTOs.isEmpty()) {
            return;
        }

        // Cargar domicilios existentes (si hay)
        Map<Long, Domicilio> domiciliosExistentesMap = new HashMap<>();
        if (usuario.getDomicilios() != null) {
            for (Domicilio d : usuario.getDomicilios()) {
                if (d.getId() != null) {
                    domiciliosExistentesMap.put(d.getId(), d);
                }
            }
        }

        // ⚠️ Lista temporal para validar antes de modificar el usuario
        List<Domicilio> listaProcesada = new ArrayList<>();

        for (DomicilioDTO dto : domiciliosDTOs) {
            Domicilio domicilio;

            if (dto.getId() != null && domiciliosExistentesMap.containsKey(dto.getId())) {
                domicilio = domiciliosExistentesMap.get(dto.getId());
            } else {
                domicilio = new Domicilio();
                domicilio.setUsuario(usuario);
            }

            Localidad localidad = localidadRepository.findById(dto.getLocalidad().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

            domicilio.setCalle(dto.getCalle());
            domicilio.setNumero(dto.getNumero());
            domicilio.setCp(dto.getCp());
            domicilio.setLocalidad(localidad);
            domicilio.setPrincipal(dto.getPrincipal() != null && dto.getPrincipal());

            listaProcesada.add(domicilio);
        }

        // ✅ Validaciones solo una vez sobre la lista final simulada
        validarUnSoloDomicilioPrincipal(listaProcesada);
        validarAlMenosUnPrincipal(listaProcesada);

        // 🟢 Todo válido: ahora sí, modificar al usuario
        usuario.setDomicilios(listaProcesada);
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
}
