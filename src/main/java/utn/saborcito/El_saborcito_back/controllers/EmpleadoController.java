package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.EmpleadoMapper;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.repositories.EmpleadoRepository;
import utn.saborcito.El_saborcito_back.services.EmpleadoService;
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService service;

    // --- HU8: Listado completo de empleados ---
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAll() {
        EmpleadoDTO empleadoDTO = service.findAll().get(0);
        if (empleadoDTO != null) {
            return ResponseEntity.ok(service.findAll());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // --- HU8: Obtener empleado por ID ---
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getById(@PathVariable Long id) {
        EmpleadoDTO empleadoDTO = service.findById(id);
        if (empleadoDTO != null) {
        return ResponseEntity.ok(empleadoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // --- HU8 Obtener los roles de un empleado ---
    @GetMapping("/admin/roles")
    public ResponseEntity<List<Rol>> getRolesDisponibles() {
        List<Rol> roles = Arrays.stream(Rol.values())
                .filter(rol -> rol != Rol.ADMIN && rol != Rol.CLIENTE)
                .toList();

        return ResponseEntity.ok(roles);
    }

    // --- HU06: Actualización general de datos del empleado ---
    // PATCH: Edición parcial por el empleado (autogestión)
    @PatchMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleado(
            @PathVariable Long id,
            @RequestBody ActualizarDatosEmpleadoDTO dto) {
        EmpleadoDTO updated = service.updateEmpleado(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT: Edición completa por el admin (rol, estado, sucursal, etc)
    @PutMapping("/admin/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleadoAdmin(
            @PathVariable Long id,
            @RequestBody ActualizarEmpleadoAdminDTO dto) {
        EmpleadoDTO updated = service.updateEmpleadoAdmin(id, dto);
        return ResponseEntity.ok(updated);
    }

    // --- HU06: Cambio de contraseña desde el empleado ---
    @PutMapping("/{id}/cambiar-password")
    public ResponseEntity<Void> cambiarPasswordEmpleado(
            @PathVariable Long id,
            @RequestBody CambiarPasswordDTO dto) {
        service.cambiarPassword(id, dto);
        return ResponseEntity.noContent().build();
    }
    // --- HU08: Baja lógica del empleado (desde Admin) ---
    @PatchMapping("/admin/{id}/baja")
    public ResponseEntity<Map<String, String>> bajaLogicaEmpleado(@PathVariable Long id) {
        service.bajaLogicaEmpleado(id);
        return ResponseEntity.ok(Map.of("mensaje", "Empleado dado de baja correctamente"));
    }

    // --- HU08: Alta lógica del empleado (desde Admin) ---
    @PatchMapping("/admin/{id}/alta")
    public ResponseEntity<Map<String, String>> altaEmpleado(@PathVariable Long id) {
        service.altaEmpleado(id);
        return ResponseEntity.ok(Map.of("mensaje", "Empleado dado de alta correctamente"));
    }

}
