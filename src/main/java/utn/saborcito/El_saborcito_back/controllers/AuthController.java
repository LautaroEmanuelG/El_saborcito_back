package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.services.ClienteService;
import utn.saborcito.El_saborcito_back.services.EmpleadoService;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {

    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;

    //CLIENTES
    // --- HU02: Login manual (solo para clientes locales) ---
    @PostMapping("/clientes/login")
    public ResponseEntity<UsuarioDTO> loginCliente(@RequestBody LoginRequest dto) {
        UsuarioDTO usuario = clienteService.loginCliente(dto);
        return ResponseEntity.ok(usuario);
    }
    // --- HU01: Registro de nuevo cliente (manual) ---
    @PostMapping("/clientes/registro")
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody RegistroClienteDTO dto) {
        ClienteDTO cliente = clienteService.registrarClienteManual(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
    // --- HU01/HU02: Registro o sincronización desde Auth0 ---
    @PostMapping("/clientes/auth0")
    public ResponseEntity<ClienteDTO> registrarOActualizarDesdeAuth0Cliente(@RequestBody RegistroAuth0DTO dto) {
        ClienteDTO cliente = clienteService.sincronizarAuth0Usuario(dto);
        return ResponseEntity.ok(cliente);
    }

    //EMPLEADOS
    // --- HU04: Registro de nuevo empleado (manual) ---
    @PostMapping("/empleados/registrar")
    public ResponseEntity<EmpleadoDTO> registrarEmpleado(@RequestBody RegistroEmpleadoDTO dto) {
        EmpleadoDTO empleado = empleadoService.registrarEmpleado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
    }

    // --- HU05: Login del empleado (manual o Auth0) ---
    @PostMapping("/empleados/login")
    public ResponseEntity<AuthEmpleadoResponseDTO> loginEmpleado(@RequestBody LoginRequest dto) {
        AuthEmpleadoResponseDTO response = empleadoService.loginEmpleado(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/empleados/auth0")
    public ResponseEntity<EmpleadoDTO> registrarOActualizarDesdeAuth0Empleado(@RequestBody RegistroEmpleadoAuth0DTO dto) {
        EmpleadoDTO empleado = empleadoService.sincronizarAuth0Usuario(dto);
        return ResponseEntity.ok(empleado);
    }
}


