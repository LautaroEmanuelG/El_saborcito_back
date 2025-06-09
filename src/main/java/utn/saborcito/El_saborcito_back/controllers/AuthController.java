package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.services.ClienteService;
import utn.saborcito.El_saborcito_back.services.EmpleadoService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;

    // ✅ HU01 - Registro manual de cliente (o desde Auth0)
    @PostMapping("/clientes/registro")
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody RegistroClienteDTO dto) {
        ClienteDTO cliente = clienteService.registrarClienteFlexible(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // ✅ HU02 - Login cliente con Auth0 (solo valida que exista y esté activo)
    @PostMapping("/clientes/auth0/login")
    public ResponseEntity<UsuarioDTO> loginClienteAuth0(@RequestBody LoginRequest dto) {
        UsuarioDTO usuario = clienteService.loginCliente(dto);
        return ResponseEntity.ok(usuario);
    }

    // ✅ HU01/HU02 - Registro o sincronización desde Auth0
    @PostMapping("/clientes/auth0")
    public ResponseEntity<ClienteDTO> registrarOActualizarDesdeAuth0Cliente(@RequestBody RegistroAuth0DTO dto) {
        ClienteDTO cliente = clienteService.sincronizarAuth0Usuario(dto);
        return ResponseEntity.ok(cliente);
    }

    // ✅ HU04 - Registro manual de empleado (o desde Auth0)
    @PostMapping("/empleados/registro")
    public ResponseEntity<EmpleadoDTO> registrarEmpleado(@RequestBody RegistroEmpleadoDTO dto) {
        EmpleadoDTO empleado = empleadoService.registrarEmpleadoFlexible(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
    }

    // ✅ HU05 - Login empleado con Auth0 (solo valida que exista y esté activo)
    @PostMapping("/empleados/auth0/login")
    public ResponseEntity<AuthEmpleadoResponseDTO> loginEmpleadoAuth0(@RequestBody LoginRequest dto) {
        AuthEmpleadoResponseDTO response = empleadoService.validarEmpleadoAuth0(dto.getEmail());
        return ResponseEntity.ok(response);
    }

    // ✅ HU01/HU02 - Registro o sincronización desde Auth0
    @PostMapping("/empleados/auth0")
    public ResponseEntity<EmpleadoDTO> registrarOActualizarDesdeAuth0Empleado(@RequestBody RegistroEmpleadoAuth0DTO dto) {
        EmpleadoDTO empleado = empleadoService.sincronizarAuth0Usuario(dto);
        return ResponseEntity.ok(empleado);
    }
}