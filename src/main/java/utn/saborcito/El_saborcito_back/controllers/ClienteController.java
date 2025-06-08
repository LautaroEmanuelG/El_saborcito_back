package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.mappers.ClienteMapper;
import utn.saborcito.El_saborcito_back.services.ClienteService;
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;
    private final ClienteMapper clienteMapper;
    private final UsuarioService usuarioService;

    // --- HU6: Obtener todos los clientes (administrador) ---
    @GetMapping
    public List<ClienteDTO> getAll() {
        return service.findAll().stream()
                .map(clienteMapper::toDTO)
                .toList();
    }

    // --- HU6: Obtener cliente por ID (administrador o detalle) ---
    @GetMapping("/{id}")
    public ClienteDTO getById(@PathVariable Long id) {
        return clienteMapper.toDTO(service.findById(id));
    }

    // --- HU03: Actualización de datos del cliente ---
    @PutMapping("/{id}/actualizar-datos")
    public ResponseEntity<ClienteDTO> actualizarDatosCliente(
            @PathVariable Long id,
            @RequestBody ActualizarDatosClienteDTO dto) {
        ClienteDTO clienteActualizado = service.updateCliente(id, dto);
        return ResponseEntity.ok(clienteActualizado);
    }

    // --- HU07: Baja lógica del cliente (desde Admin) ---

    @PatchMapping("/admin/{id}/baja")
    public ResponseEntity<Void> bajaLogicaCliente(@PathVariable Long id) {
        service.bajaLogicaCliente(id);
        return ResponseEntity.noContent().build();
    }

    // --- HU07: Alta lógica del cliente (desde Admin) ---
    @PatchMapping("/admin/{id}/alta")
    public ResponseEntity<Void> altaCliente(@PathVariable Long id) {
        service.altaCliente(id);
        return ResponseEntity.noContent().build();
    }
}