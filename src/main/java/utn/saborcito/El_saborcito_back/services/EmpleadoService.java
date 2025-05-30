package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.EmpleadoMapper;
import utn.saborcito.El_saborcito_back.mappers.UsuarioMapper;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final UsuarioMapper usuarioMapper;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository; // Inyectar UsuarioRepository
    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HorarioAtencionService horarioAtencionService;

    public List<EmpleadoDTO> findAll() {
        return empleadoRepository.findAll()
                .stream()
                .map(empleadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EmpleadoDTO findById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado no encontrado con ID: " + id));
        return empleadoMapper.toDTO(empleado);
    }

    public Empleado findEntityById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado no encontrado con ID: " + id));
    }

    public EmpleadoDTO save(EmpleadoDTO dto) {
        Empleado empleado = empleadoMapper.toEntity(dto);

        // Validar y asignar Sucursal
        if (dto.getSucursal() != null && dto.getSucursal().getId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursal().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Sucursal asignada no válida. ID: " + dto.getSucursal().getId()));
            empleado.setSucursal(sucursal);
        } else {
            empleado.setSucursal(null); // Permitir empleado sin sucursal asignada
        }

        // Validar y asignar Usuario
        if (dto.getUsuario() != null && dto.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuario().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Usuario asignado no válido. ID: " + dto.getUsuario().getId()));

            // Validar rol del usuario
            if (usuario.getRol() == Rol.CLIENTE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El empleado debe tener un usuario con un rol de empleado (no cliente).");
            }
            empleado.setUsuario(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado debe tener un usuario asignado.");
        }

        // Asignar fecha de ingreso si no está presente
        if (empleado.getFechaIngreso() == null) {
            empleado.setFechaIngreso(java.time.LocalDate.now());
        }

        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return empleadoMapper.toDTO(savedEmpleado);
    }

    public EmpleadoDTO update(Long id, EmpleadoDTO dto) {
        Empleado empleadoExistente = findEntityById(id);

        // Actualizar campos simples del empleado
        empleadoExistente.setLegajo(dto.getLegajo());
        if (dto.getFechaIngreso() != null) { // Solo actualizar si se provee
            empleadoExistente.setFechaIngreso(dto.getFechaIngreso());
        }

        // Actualizar Sucursal
        if (dto.getSucursal() != null && dto.getSucursal().getId() != null) {
            if (empleadoExistente.getSucursal() == null
                    || !empleadoExistente.getSucursal().getId().equals(dto.getSucursal().getId())) {
                Sucursal nuevaSucursal = sucursalRepository.findById(dto.getSucursal().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Sucursal asignada no válida. ID: " + dto.getSucursal().getId()));
                empleadoExistente.setSucursal(nuevaSucursal);
            }
        } else {
            empleadoExistente.setSucursal(null); // Permitir desasignar sucursal
        }

        // Actualizar Usuario (solo ciertos campos, el ID del usuario no debería
        // cambiar)
        if (dto.getUsuario() != null && empleadoExistente.getUsuario() != null) {
            if (!dto.getUsuario().getId().equals(empleadoExistente.getUsuario().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No se puede cambiar el usuario asociado a un empleado. Cree un nuevo empleado en su lugar.");
            }
            // Aquí podrías permitir la actualización de datos del UsuarioDTO si fuera
            // necesario,
            // pero generalmente la entidad Usuario se maneja por su propio servicio.
            // Por ahora, asumimos que el UsuarioDTO en la solicitud de actualización de
            // empleado
            // es principalmente para referencia o para validar el ID.
            // Si se quisiera actualizar el usuario, se debería hacer con cuidado:
            // Usuario usuarioExistenteEnEmpleado = empleadoExistente.getUsuario();
            // UsuarioDTO usuarioDtoEntrante = dto.getUsuario();
            // usuarioExistenteEnEmpleado.setNombre(usuarioDtoEntrante.getNombre()); // etc.
            // Y luego validar el rol nuevamente si es necesario.
        }

        Empleado updatedEmpleado = empleadoRepository.save(empleadoExistente);
        return empleadoMapper.toDTO(updatedEmpleado);
    }

    public void delete(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    //Validaciones de email y contraseña
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

    public EmpleadoDTO registrarEmpleado(RegistroEmpleadoDTO dto) {
        // Validar contraseñas coincidan
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        // Validar formato de email
        if (!isValidEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
        }

        // Validar que no exista otro usuario con ese email
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        // Validar contraseña provisoria
        if (!isValidPassword(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }

        // Crear Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol()); // Asigna el rol seleccionado
        usuario.setEstado(true); // Por defecto activo
        usuario.setFechaRegistro(LocalDateTime.now());

        // Guardar usuario primero
        usuario = usuarioRepository.save(usuario);

        // Crear Empleado
        Empleado empleado = new Empleado();
        empleado.setId(usuario.getId()); // ID compartido
        empleado.setLegajo(generarLegajo(dto.getNombre(), dto.getApellido()));
        empleado.setFechaIngreso(LocalDate.now());
        empleado.setUsuario(usuario);

        // Opcional: asignar sucursal si corresponde
        if (dto.getSucursalId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
            empleado.setSucursal(sucursal);
        }

        // Guardar empleado
        empleado = empleadoRepository.save(empleado);

        return empleadoMapper.toDTO(empleado);
    }

    private String generarLegajo(String nombre, String apellido) {
        return nombre.substring(0, 2).toUpperCase() +
                apellido.substring(0, 2).toUpperCase() +
                System.currentTimeMillis() % 10000;
    }

    public void validarHorarioLaboral(Empleado empleado) {
        if (empleado.getSucursal() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no tiene una sucursal asignada.");
        }

        List<HorarioAtencionDTO> horarios = horarioAtencionService.getHorariosDeSucursal(empleado.getSucursal().getId());

        if (!horarioAtencionService.estaEnHorarioLaboral(horarios)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Fuera del horario laboral. Acceso denegado.");
        }
    }

    public AuthEmpleadoResponseDTO loginEmpleado(LoginRequest dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        if (!usuario.getRol().equals(Rol.ADMIN) &&
                !usuario.getRol().equals(Rol.CAJERO) &&
                !usuario.getRol().equals(Rol.COCINERO) &&
                !usuario.getRol().equals(Rol.DELIVERY)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este usuario no es un empleado válido");
        }

        if (!usuario.getEstado()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El empleado está dado de baja");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        Empleado empleado = empleadoRepository.findById(usuario.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        // Validar horario laboral
        validarHorarioLaboral(empleado);

        boolean esPrimeraVez = usuario.getFechaUltimaModificacion() == null;

        return AuthEmpleadoResponseDTO.builder()
                .mensaje(esPrimeraVez ? "Debe cambiar su contraseña" : "Inicio de sesión exitoso")
                .cambioRequerido(esPrimeraVez)
                .usuario(usuarioMapper.toDTO(usuario))
                .build();
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

    public EmpleadoDTO actualizarDatos(Long empleadoId, ActualizarDatosEmpleadoDTO dto) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        Usuario usuario = empleado.getUsuario();

        // Validar y actualizar email si corresponde
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (!isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }

            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
            }

            usuario.setEmail(dto.getEmail());
        }

        // Validar y actualizar teléfono si corresponde
        if (dto.getTelefono() != null) {
            usuario.setTelefono(dto.getTelefono());
        }
        // Actualizar otros campos básicos
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());

        // Actualizar rol del empleado
        if (dto.getRol() != null && !dto.getRol().equals(usuario.getRol())) {
            usuario.setRol(dto.getRol());
        }

        // Validar y actualizar domicilio si corresponde
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

        // Actualizar sucursal si corresponde
        if (dto.getSucursalId() != null && !dto.getSucursalId().equals(usuario.getId())) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
            empleado.setSucursal(sucursal);
        }

        // Validar y actualizar contraseña si corresponde
        if (dto.getNuevaContraseña() != null && !dto.getNuevaContraseña().isBlank()) {
            if (!dto.getNuevaContraseña().equals(dto.getConfirmarContraseña())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
            }

            if (!isValidPassword(dto.getNuevaContraseña())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
            }

            if (dto.getContraseñaActual() == null || !passwordEncoder.matches(dto.getContraseñaActual(), usuario.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
            }

            usuario.setPassword(passwordEncoder.encode(dto.getNuevaContraseña()));
        }

        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);

        return empleadoMapper.toDTO(empleadoRepository.save(empleado));
    }

    public void toggleEstado(Long empleadoId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        Usuario usuario = empleado.getUsuario();
        usuario.setEstado(!usuario.getEstado());
        usuarioRepository.save(usuario);
    }

}
