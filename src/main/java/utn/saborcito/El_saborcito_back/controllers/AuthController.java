package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.config.security.JwtUtil;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.services.ClienteService;
import utn.saborcito.El_saborcito_back.services.EmpleadoService;
import utn.saborcito.El_saborcito_back.services.HorarioAtencionService;
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;
    private final HorarioAtencionService horarioAtencionService;
    private final JwtUtil jwtUtil;

    // ✅ HU01 - Registro manual de cliente (o desde Auth0)
    @PostMapping("/clientes/registro")
    public ResponseEntity<AuthResponseDTO> registrarCliente(@RequestBody RegistroClienteDTO dto) {
        ClienteDTO cliente = clienteService.registrarClienteFlexible(dto);
        String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getRol().name());

        AuthResponseDTO response = AuthResponseDTO.builder()
                .message("Registro exitoso")
                .token(token)
                .usuario(cliente)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ HU02 - Login cliente con Auth0 (solo valida que exista y esté activo)
    @PostMapping("/clientes/login/auth0")
    public ResponseEntity<AuthResponseDTO> loginClienteAuth0(@RequestBody LoginRequest dto) {
        UsuarioDTO usuario = clienteService.loginClienteAuth0(dto);
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .message("Login con Auth0 exitoso")
                .token(token)
                .usuario(usuario)
                .build());
    }

    @PostMapping("/clientes/login/manual")
    public ResponseEntity<AuthResponseDTO> loginClienteManual(@RequestBody LoginRequest dto) {
        UsuarioDTO usuario = clienteService.loginClienteManual(dto);
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
        if (!usuario.getRol().equals(Rol.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }
        return ResponseEntity.ok(AuthResponseDTO.builder()
                .message("Login manual exitoso")
                .token(token)
                .usuario(usuario)
                .build());
    }

    // ✅ HU01/HU02 - Registro o sincronización desde Auth0
    @PostMapping("/clientes/auth0")
    public ResponseEntity<ClienteDTO> registrarOActualizarDesdeAuth0Cliente(@RequestBody RegistroAuth0DTO dto) {
        ClienteDTO cliente = clienteService.sincronizarAuth0Usuario(dto);
        return ResponseEntity.ok(cliente);
    }

    // ✅ HU04 - Registro manual de empleado (o desde Auth0)
    @PostMapping("/empleados/registro")
    public ResponseEntity<AuthEmpleadoResponseDTO> registrarEmpleado(@RequestBody RegistroEmpleadoDTO dto) {
        EmpleadoDTO empleado = empleadoService.registrarEmpleadoFlexible(dto);
        String token = jwtUtil.generateToken(empleado.getEmail(), empleado.getRol().name());

        AuthEmpleadoResponseDTO response = AuthEmpleadoResponseDTO.builder()
                .mensaje("Registro exitoso")
                .empleado(empleado)
                .token(token)
                .cambioRequerido(false) // o true si es contraseña provisoria
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ HU05 - Login empleado (solo valida que exista y esté activo)
    // Login manual
    @PostMapping("/empleados/login/manual")
    public ResponseEntity<AuthEmpleadoResponseDTO> loginEmpleadoManual(@RequestBody LoginRequest dto) {
        AuthEmpleadoResponseDTO response = empleadoService.loginEmpleadoManual(dto);

        // Validar que el rol del empleado sea uno de los permitidos
        Rol rol = response.getEmpleado().getRol();
        if (rol != Rol.COCINERO && rol != Rol.CAJERO && rol != Rol.DELIVERY && rol != Rol.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol de empleado no válido");
        }

        return ResponseEntity.ok(response);
    }
    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDTO> loginAdmin(@RequestBody LoginRequest dto) {
        AuthResponseDTO response = usuarioService.loginAdmin(dto);
        return ResponseEntity.ok(response);
    }

}