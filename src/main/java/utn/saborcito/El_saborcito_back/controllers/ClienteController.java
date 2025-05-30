package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.*;
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
    private final ClienteMapper mapper;
    private final UsuarioService usuarioService;

    @GetMapping
    public List<ClienteDTO> getAll() {
        return service.findAll().stream().map(mapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ClienteDTO getById(@PathVariable Long id) {
        return mapper.toDTO(service.findById(id));
    }

    @PostMapping
    public ClienteDTO create(@RequestBody ClienteDTO dto) {
        Cliente cliente = mapper.toEntity(dto);
        return mapper.toDTO(service.save(cliente));
    }

    @PutMapping("/{id}")
    public ClienteDTO update(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        Cliente cliente = mapper.toEntity(dto);
        return mapper.toDTO(service.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/registro")
    public ResponseEntity<Cliente> registrar(@RequestBody RegistroClienteDTO dto) {
        Cliente cliente = usuarioService.registrarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
    @PutMapping("/clientes/{id}")
    public ResponseEntity<UsuarioDTO> actualizarDatosCliente(@PathVariable Long id, @RequestBody ActualizarDatosClienteDTO dto) {
        UsuarioDTO usuario = usuarioService.actualizarDatosCliente(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ClienteDTO> actualizarDatos(@PathVariable Long id, @RequestBody ActualizarDatosClienteDTO adminDto) {
        ClienteDTO cliente = service.update(id, adminDto);
        return ResponseEntity.ok(cliente);
    }

    @PatchMapping("/admin/{id}/toggle")
    public ResponseEntity<Void> toggleEstado(@PathVariable Long id) {
        service.toggleEstado(id);
        return ResponseEntity.noContent().build();
    }
}
}
