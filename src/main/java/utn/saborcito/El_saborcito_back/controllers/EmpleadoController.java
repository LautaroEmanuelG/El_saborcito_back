package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.services.EmpleadoService;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService service;

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getById(@PathVariable Long id) {
        EmpleadoDTO empleadoDTO = service.findById(id);
        if (empleadoDTO != null) {
            return ResponseEntity.ok(empleadoDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> create(@RequestBody EmpleadoDTO empleadoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(empleadoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> update(@PathVariable Long id, @RequestBody EmpleadoDTO empleadoDTO) {
        EmpleadoDTO updatedEmpleado = service.update(id, empleadoDTO);
        if (updatedEmpleado != null) {
            return ResponseEntity.ok(updatedEmpleado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/registrar")
    public ResponseEntity<EmpleadoDTO> registrar(@RequestBody RegistroEmpleadoDTO dto) {
        EmpleadoDTO empleado = service.registrarEmpleado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthEmpleadoResponseDTO> login(@RequestBody LoginRequest dto) {
        AuthEmpleadoResponseDTO response = service.loginEmpleado(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cambiar-password")
    public ResponseEntity<Void> cambiarPass(@PathVariable Long id,@RequestBody CambiarPasswordDTO dto) {
        service.cambiarPassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> actualizarDatos(@PathVariable Long id, @RequestBody ActualizarDatosEmpleadoDTO dto) {
        EmpleadoDTO empleado = service.actualizarDatos(id, dto);
        return ResponseEntity.ok(empleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleado(@PathVariable Long id, @RequestBody ActualizarDatosEmpleadoDTO dto) {
        EmpleadoDTO updatedEmpleado = service.update(id, dto);
        return ResponseEntity.ok(updatedEmpleado);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleEmpleado(@PathVariable Long id) {
        service.toggleEstado(id);
        return ResponseEntity.noContent().build();
    }
}
