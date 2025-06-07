package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.mappers.EmpleadoMapper;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;
    private final PasswordEncoder passwordEncoder;
    private final HorarioAtencionService horarioAtencionService;
    private final UsuarioService usuarioService;

    // --- Obtener todos los empleados ---
    public List<EmpleadoDTO> findAll() {
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toDTO)
                .toList();
    }

    // --- Obtener por ID ---
    public EmpleadoDTO findById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
        return empleadoMapper.toDTO(empleado);
    }

    // --- Registro manual ---
    public EmpleadoDTO registrarEmpleado(RegistroEmpleadoDTO dto) {
        // Validaciones unificadas
        usuarioService.validarEmailUnico(dto.getEmail());
        if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        if (!isValidPassword(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }
        if (!isValidEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
        }
        // Crear Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());//tengo dudas con este
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);

        // Procesar domicilios
        List<Domicilio> domicilios = new ArrayList<>();
        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            for (DomicilioDTO domicilioDTO : dto.getDomicilios()) {
                if (domicilioDTO.getCalle() != null && domicilioDTO.getLocalidad() != null &&
                        domicilioDTO.getLocalidad().getId() != null) {
                    Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));
                    Domicilio domicilio = new Domicilio();
                    domicilio.setCalle(domicilioDTO.getCalle());
                    domicilio.setNumero(domicilioDTO.getNumero());
                    domicilio.setCp(domicilioDTO.getCp());
                    domicilio.setLocalidad(localidad);
                    domicilio.setUsuario(usuario);
                    domicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    domicilios.add(domicilio);
                }
            }
            usuarioService.validarUnSoloDomicilioPrincipal(domicilios);
            usuarioService.validarAlMenosUnPrincipal(domicilios);
        }
        usuario.setDomicilios(domicilios);
        usuario = usuarioRepository.save(usuario);
        // Crear Empleado
        Empleado empleado = new Empleado();
        empleado.setId(usuario.getId());
        empleado.setLegajo(generarLegajo(dto.getNombre(), dto.getApellido()));
        empleado.setFechaIngreso(LocalDate.now());
        if (dto.getSucursalId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
            empleado.setSucursal(sucursal);
        }
        empleado = empleadoRepository.save(empleado);
        return empleadoMapper.toDTO(empleado);
    }

    // --- Actualizar datos del empleado (autogestión) ---
    public EmpleadoDTO updateEmpleado(Long empleadoId, ActualizarDatosEmpleadoDTO dto) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        Usuario usuario = empleado;
        // Validar y actualizar email
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            usuarioService.validarEmailUnico(dto.getEmail());
            if (!isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }
            usuario.setEmail(dto.getEmail());
        }
        // Actualizar nombre, apellido y teléfono si se proporcionan
        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());
        // Actualizar o crear nuevos domicilios
        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            List<Domicilio> domiciliosExistentes = usuario.getDomicilios();

            for (DomicilioDTO domicilioDTO : dto.getDomicilios()) {
                if (domicilioDTO.getCalle() == null || domicilioDTO.getLocalidad() == null ||
                        domicilioDTO.getLocalidad().getId() == null) {
                    continue; // Saltar si faltan datos necesarios
                }

                Optional<Domicilio> domicilioExistenteOpt = domiciliosExistentes.stream()
                        .filter(d -> d.getId() != null && d.getId().equals(domicilioDTO.getId()))
                        .findFirst();

                if (domicilioExistenteOpt.isPresent()) {
                    Domicilio domicilio = domicilioExistenteOpt.get();
                    domicilio.setCalle(domicilioDTO.getCalle());
                    domicilio.setNumero(domicilioDTO.getNumero());
                    domicilio.setCp(domicilioDTO.getCp());
                    Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));
                    domicilio.setLocalidad(localidad);
                    domicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    domicilioRepository.save(domicilio);
                } else {
                    Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));
                    Domicilio nuevoDomicilio = new Domicilio();
                    nuevoDomicilio.setCalle(domicilioDTO.getCalle());
                    nuevoDomicilio.setNumero(domicilioDTO.getNumero());
                    nuevoDomicilio.setCp(domicilioDTO.getCp());
                    nuevoDomicilio.setLocalidad(localidad);
                    nuevoDomicilio.setUsuario(usuario);
                    nuevoDomicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    domiciliosExistentes.add(nuevoDomicilio);
                }
            }
            usuario.setDomicilios(domiciliosExistentes);
        }
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        empleadoRepository.save(empleado);

        return empleadoMapper.toDTO(empleado);
    }

    // --- Actualizar datos del empleado (admin) ---
    public EmpleadoDTO updateEmpleadoAdmin(Long empleadoId, ActualizarEmpleadoAdminDTO dto) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        Usuario usuario = empleado;
        // Validar y actualizar email
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            usuarioService.validarEmailUnico(dto.getEmail());
            if (!isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }
            usuario.setEmail(dto.getEmail());
        }
        // Actualizar nombre, apellido y teléfono si se proporcionan
        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());
        // Rol
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol()); //HU7
        }
        // Estado
        if (dto.getEstado() != null) {
            usuario.setEstado(dto.getEstado());
        }
        // Sucursal
        if (dto.getSucursalId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
            empleado.setSucursal(sucursal);
        }
        // Actualizar o crear domicilios
        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            List<Domicilio> domiciliosExistentes = usuario.getDomicilios();
            for (DomicilioDTO domicilioDTO : dto.getDomicilios()) {
                if (domicilioDTO.getCalle() == null || domicilioDTO.getLocalidad() == null ||
                        domicilioDTO.getLocalidad().getId() == null) {
                    continue;
                }
                Optional<Domicilio> domicilioExistenteOpt = domiciliosExistentes.stream()
                        .filter(d -> d.getId() != null && d.getId().equals(domicilioDTO.getId()))
                        .findFirst();
                if (domicilioExistenteOpt.isPresent()) {
                    Domicilio domicilio = domicilioExistenteOpt.get();
                    domicilio.setCalle(domicilioDTO.getCalle());
                    domicilio.setNumero(domicilioDTO.getNumero());
                    domicilio.setCp(domicilioDTO.getCp());
                    Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));
                    domicilio.setLocalidad(localidad);
                    domicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    domicilioRepository.save(domicilio);
                } else {
                    Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));
                    Domicilio nuevoDomicilio = new Domicilio();
                    nuevoDomicilio.setCalle(domicilioDTO.getCalle());
                    nuevoDomicilio.setNumero(domicilioDTO.getNumero());
                    nuevoDomicilio.setCp(domicilioDTO.getCp());
                    nuevoDomicilio.setLocalidad(localidad);
                    nuevoDomicilio.setUsuario(usuario);
                    nuevoDomicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    usuario.getDomicilios().add(nuevoDomicilio);
                }
            }
            usuarioService.validarUnSoloDomicilioPrincipal(usuario.getDomicilios());
            usuarioService.validarAlMenosUnPrincipal(usuario.getDomicilios());
        }
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        empleadoRepository.save(empleado);
        return empleadoMapper.toDTO(empleado);
    }

    // --- Login (manual o Auth0) ---
    public AuthEmpleadoResponseDTO loginEmpleado(LoginRequest dto) {
        UsuarioDTO usuario = usuarioService.login(dto);
        Empleado empleado = empleadoRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Este usuario no es un empleado válido"));
        boolean esPrimeraVez = usuario.getFechaUltimaModificacion() == null;
        return AuthEmpleadoResponseDTO.builder()
                .mensaje(esPrimeraVez ? "Debe cambiar su contraseña" : "Inicio de sesión exitoso")
                .cambioRequerido(esPrimeraVez)
                .empleado(empleadoMapper.toDTO(empleado))
                .build();
    }

    // --- Activar/desactivar empleado (HU8) ---
    public void toggleEstado(Long empleadoId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        empleado.setEstado(!empleado.getEstado());
        usuarioRepository.save(empleado);
    }

    // --- Cambio de contraseña (HU6) ---
    public void cambiarPassword(Long usuarioId, CambiarPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        if (!isValidPassword(dto.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }
        usuarioService.cambiarContrasena(usuarioId, dto.getNewPassword());
    }

    // --- VALIDACIÓN: Horario laboral ---
    public void validarHorarioLaboral(Empleado empleado) {
        if (empleado.getSucursal() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no tiene una sucursal asignada.");
        }
        List<HorarioAtencionDTO> horarios =
                horarioAtencionService.getHorariosDeSucursal(empleado.getSucursal().getId());
        if (!horarioAtencionService.estaEnHorarioLaboral(horarios)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fuera del horario laboral. Acceso denegado.");
        }
    }

    // --- Métodos privados de validación ---
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

    private String generarLegajo(String nombre, String apellido) {
        return nombre.substring(0, 2).toUpperCase() +
                apellido.substring(0, 2).toUpperCase() +
                System.currentTimeMillis() % 10000;
    }

    // ✅ Buscar empleado por auth0Id
    public Optional<EmpleadoDTO> findByAuth0Id(String auth0Id) {
        return usuarioService.findByAuth0Id(auth0Id)
                .flatMap(usuarioDTO -> {
                    Optional<Empleado> emp = empleadoRepository.findById(usuarioDTO.getId());
                    return emp.map(empleadoMapper::toDTO);
                });
    }

    // ✅ Crear o actualizar empleado desde Auth0
    public EmpleadoDTO sincronizarAuth0Usuario(Auth0UserDTO dto) {
        Optional<EmpleadoDTO> existente = findByAuth0Id(dto.getSub());
        if (existente.isPresent()) {
            return actualizarDatosAuth0(existente.get(), dto);
        } else {
            return crearDesdeAuth0(dto);
        }
    }

    private EmpleadoDTO crearDesdeAuth0(Auth0UserDTO dto) {
        RegistroEmpleadoDTO nuevo = new RegistroEmpleadoDTO();
        nuevo.setEmail(dto.getEmail());
        nuevo.setNombre(dto.getGivenName());
        nuevo.setApellido(dto.getFamilyName());
        nuevo.setRol(Rol.CLIENTE); // Asignar rol por defecto, se puede cambiar según la lógica de negocio
        nuevo.setAuth0Id(dto.getSub());
        // No hay contraseña en Auth0
        nuevo.setPassword(null);
        nuevo.setConfirmarPassword(null);
        // Guardado como si fuera manual pero sin pass
        return crearEmpleadoDesdeDTOAuth0(nuevo);
    }

    private EmpleadoDTO actualizarDatosAuth0(EmpleadoDTO dtoLocal, Auth0UserDTO dto) {
        boolean actualizado = false;
        if (!dtoLocal.getEmail().equals(dto.getEmail())) {
            dtoLocal.setEmail(dto.getEmail());
            actualizado = true;
        }
        if (!dtoLocal.getNombre().equals(dto.getGivenName())) {
            dtoLocal.setNombre(dto.getGivenName());
            actualizado = true;
        }
        if (!dtoLocal.getApellido().equals(dto.getFamilyName())) {
            dtoLocal.setApellido(dto.getFamilyName());
            actualizado = true;
        }
        if (actualizado) {
            Empleado entity = empleadoMapper.toEntity(dtoLocal);
            return empleadoMapper.toDTO(empleadoRepository.save(entity));
        }
        return dtoLocal;
    }

    // Método auxiliar adaptado para Auth0 sin password
    private EmpleadoDTO crearEmpleadoDesdeDTOAuth0(RegistroEmpleadoDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setAuth0Id(dto.getAuth0Id());
        usuario = usuarioRepository.save(usuario);
        Empleado empleado = new Empleado();
        empleado.setId(usuario.getId());
        empleado.setLegajo(generarLegajo(dto.getNombre(), dto.getApellido()));
        empleado.setFechaIngreso(LocalDate.now());
        return empleadoMapper.toDTO(empleadoRepository.save(empleado));
    }
}
