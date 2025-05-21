package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.repositories.EmpleadoRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final SucursalRepository sucursalRepository;

    public List<Empleado> findAll() {
        return repo.findAll();
    }

    public Empleado findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado no encontrado con ID: " + id));
    }

    public Empleado save(Empleado empleado) {
        // Si el ID de la sucursal es 0 o nulo, se asume que el empleado no está
        // asignado a una sucursal.
        if (empleado.getSucursal() != null
                && (empleado.getSucursal().getId_Sucursal() == null || empleado.getSucursal().getId_Sucursal() == 0)) {
            empleado.setSucursal(null);
        } else if (empleado.getSucursal() != null && empleado.getSucursal().getId_Sucursal() != null) {
            // Validar que la sucursal exista si se proporciona un ID
            Sucursal sucursal = sucursalRepository.findById(empleado.getSucursal().getId_Sucursal())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Sucursal asignada no válida. ID: " + empleado.getSucursal().getId_Sucursal()));
            empleado.setSucursal(sucursal);
        }

        // La lógica de negocio concerniente a campos como `sucursal_id` vs `Sucursal
        // sucursal`
        // o un campo booleano `sucursalAsignada` debe ser resuelta en el modelo
        // Empleado.
        // Este servicio asume que el modelo Empleado utiliza un objeto `Sucursal
        // sucursal`.

        // Validaciones adicionales, como el rol del usuario asociado al empleado.
        if (empleado.getUsuario() == null
                || empleado.getUsuario().getRol() == utn.saborcito.El_saborcito_back.enums.Rol.CLIENTE) {
            // Considerar lanzar una excepción si el usuario no es válido o no tiene el rol
            // adecuado.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El empleado debe tener un usuario válido con un rol de empleado (no cliente).");
        }
        // Aquí se podrían añadir más validaciones, por ejemplo, si existe un enum
        // específico para roles de empleado:
        // List<Rol> rolesEmpleado = Arrays.asList(Rol.ADMIN, Rol.COCINERO, Rol.CAJERO,
        // Rol.DELIVERY);
        // if (empleado.getUsuario() == null ||
        // !rolesEmpleado.contains(empleado.getUsuario().getRol())) {
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario
        // asociado al empleado no tiene un rol válido.");
        // }

        return repo.save(empleado);
    }

    public Empleado update(Long id, Empleado empleadoActualizado) {
        Empleado empleadoExistente = findById(id);

        // Actualizar campos del Usuario asociado
        if (empleadoActualizado.getUsuario() != null && empleadoExistente.getUsuario() != null) {
            Usuario usuarioActualizado = empleadoActualizado.getUsuario();
            Usuario usuarioExistente = empleadoExistente.getUsuario();
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellido(usuarioActualizado.getApellido());
            usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
            // CORRECCIÓN: Usar getEstado() y setEstado() para el campo 'estado' de Usuario
            if (usuarioActualizado.getEstado() != null) { // Solo actualizar si se provee un nuevo estado
                usuarioExistente.setEstado(usuarioActualizado.getEstado());
            }
            // La actualización del rol del usuario debe manejarse con cuidado.
            // No se permite cambiar el rol desde aquí para evitar inconsistencias.
            // Si se necesita cambiar el rol, debe hacerse a través de UsuarioService y
            // validar las implicaciones.
        }

        // Manejo de la asignación de sucursal con validación
        if (empleadoActualizado.getSucursal() != null &&
                (empleadoActualizado.getSucursal().getId_Sucursal() == null || empleadoActualizado.getSucursal().getId_Sucursal() == 0)) {
            empleadoExistente.setSucursal(null); // Desasigna si el ID es 0 o nulo
        } else if (empleadoActualizado.getSucursal() != null && empleadoActualizado.getSucursal().getId_Sucursal() != null) {
            // Validar que la sucursal exista
            Sucursal sucursal = sucursalRepository.findById(empleadoActualizado.getSucursal().getId_Sucursal())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Sucursal asignada no válida. ID: " + empleadoActualizado.getSucursal().getId_Sucursal()));
            empleadoExistente.setSucursal(sucursal);
        } else {
            empleadoExistente.setSucursal(null); // Si no se proporciona sucursal en la actualización, se desasigna.
        }
        return repo.save(empleadoExistente);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Empleado no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }
}
