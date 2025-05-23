package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.EmpleadoDTO;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.EmpleadoMapper;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.repositories.EmpleadoRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository; // Asegúrate de que este repositorio exista y esté inyectado

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository; // Inyectar UsuarioRepository

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
}
