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
import utn.saborcito.El_saborcito_back.mappers.RegistroEmpleadoMapper;
import utn.saborcito.El_saborcito_back.mappers.RegistroEmpleadoMapperImpl;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// ✅ EmpleadoService refactorizado con registro flexible y limpieza de duplicados
@Service
@Transactional
@RequiredArgsConstructor
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final RegistroEmpleadoMapper registroEmpleadoMapper;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final HorarioAtencionService horarioAtencionService;
    private final UsuarioService usuarioService;

    public List<EmpleadoDTO> findAll() {
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toDTO)
                .toList();
    }

    public EmpleadoDTO findById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
        return empleadoMapper.toDTO(empleado);
    }//deberia cambiar los dos metodos anteriores para que se parezcan a clienteservice?

    public Optional<EmpleadoDTO> findByAuth0Id(String auth0Id) {
        return usuarioService.findByAuth0Id(auth0Id)
                .map(usuarioDTO -> {
                    Empleado emp = empleadoRepository.findById(usuarioDTO.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con Auth0 ID: " + auth0Id));
                    return empleadoMapper.toDTO(emp);
                });
    }
    // ✅ Registro flexible (manual o Auth0)
    public EmpleadoDTO registrarEmpleadoFlexible(RegistroEmpleadoDTO dto) {
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

        return crearEmpleado(dto);
    }

    private EmpleadoDTO crearEmpleado(RegistroEmpleadoDTO dto) {
       Empleado emp = registroEmpleadoMapper.toEntity(dto);
        if (dto.getEsAuth0()) {
            emp.setAuth0Id(dto.getAuth0Id());
        } else {
            emp.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        //EL rol lo pone el admin puede ser COCINERO,DELIVERY,CAJERO
        if (dto.getRol() == null || !Arrays.asList(Rol.CAJERO, Rol.DELIVERY, Rol.COCINERO).contains(dto.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido");
        }else{
            emp.setRol(dto.getRol());
        }
        emp.setEstado(true);
        emp.setFechaRegistro(LocalDateTime.now());
        emp.setFechaUltimaModificacion(LocalDateTime.now());
        emp.setFechaIngreso(LocalDate.now());
        emp.setLegajo(generarLegajo(dto.getNombre(), dto.getApellido()));
        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            usuarioService.procesarYValidarDomicilios(emp, dto.getDomicilios());
            emp.setFechaUltimaModificacion(LocalDateTime.now());
        }
        if (dto.getSucursal() != null && dto.getSucursal().getId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursal().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
            emp.setSucursal(sucursal);
        }
        emp = empleadoRepository.save(emp);
        return empleadoMapper.toDTO(emp);
    }

    public EmpleadoDTO updateEmpleado(Long empleadoId, ActualizarDatosEmpleadoDTO dto) {
        Empleado empleadoExistente = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        Usuario usuario = empleadoExistente;

        if(dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if(dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if(dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());

        if(dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            usuarioService.validarEmailUnico(dto.getEmail());
            if (!usuarioService.isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }
            usuarioService.validarEmailUnico(dto.getEmail());
            usuario.setEmail(dto.getEmail());
        }
        if(dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()){
            usuarioService.procesarYValidarDomicilios(empleadoExistente, dto.getDomicilios()); //o usuario
        }
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        empleadoRepository.save(empleadoExistente);
        return empleadoMapper.toDTO(empleadoExistente);
    }

    public EmpleadoDTO updateEmpleadoAdmin(Long empleadoId, ActualizarEmpleadoAdminDTO dto) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        Usuario usuario = empleado;

        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            usuarioService.validarEmailUnico(dto.getEmail());
            if (!usuarioService.isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());
        if (dto.getRol() != null) usuario.setRol(dto.getRol());
        if (dto.getEstado() != null) usuario.setEstado(dto.getEstado());

        if (dto.getSucursalId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
            empleado.setSucursal(sucursal);
        }

        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            usuarioService.procesarYValidarDomicilios(usuario, dto.getDomicilios());
        }

        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        empleadoRepository.save(empleado);
        return empleadoMapper.toDTO(empleado);
    }

    public AuthEmpleadoResponseDTO validarEmpleadoAuth0(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        if (!usuario.getEstado()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empleado dado de baja");
        }

        Empleado empleado = empleadoRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No es un empleado válido"));

        validarHorarioLaboral(empleado);

        boolean esPrimeraVez = usuario.getFechaUltimaModificacion() == null;

        return AuthEmpleadoResponseDTO.builder()
                .mensaje(esPrimeraVez ? "Debe cambiar su contraseña" : "Inicio de sesión exitoso")
                .cambioRequerido(esPrimeraVez)
                .empleado(empleadoMapper.toDTO(empleado))
                .build();
    }

    public void toggleEstado(Long empleadoId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        empleado.setEstado(!empleado.getEstado());
        usuarioRepository.save(empleado);
    }

    public void cambiarPassword(Long usuarioId, CambiarPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        if (!usuarioService.isValidPassword(dto.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }
        usuarioService.cambiarContrasena(usuarioId, dto.getNewPassword());
    }

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

    private String generarLegajo(String nombre, String apellido) {
        if (nombre == null || apellido == null) {
            throw new IllegalArgumentException("Nombre y apellido no pueden ser nulos");
        }
        return nombre.substring(0, 2).toUpperCase() +
                apellido.substring(0, 2).toUpperCase() +
                System.currentTimeMillis() % 10000;
    }


    public UsuarioDTO loginEmpleadoManual(LoginRequest dto){
        return usuarioService.loginClienteManual(dto);
    }
    public UsuarioDTO loginEmpleadoAuth0(LoginRequest dto) {
        return usuarioService.loginClienteAuth0(dto.getEmail());
    }
    public void bajaLogicaEmpleado(Long id){
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
        if (!empleado.getEstado()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado ya está dado de baja");
        }
        empleado.setEstado(false);
        usuarioService.bajaLogicaUsuario(empleado.getId());
    }

    public void altaCliente(Long id){
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
        usuarioService.altaUsuario(empleado.getId());
    }
}
