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
import utn.saborcito.El_saborcito_back.services.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;
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

    // ✅ HU02 - Login cliente con Auth0
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

        if (!usuario.getRol().equals(Rol.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
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

    // ✅ HU04 - Registro manual de empleado
    @PostMapping("/empleados/registro")
    public ResponseEntity<AuthEmpleadoResponseDTO> registrarEmpleado(@RequestBody RegistroEmpleadoDTO dto) {
        EmpleadoDTO empleado = empleadoService.registrarEmpleadoFlexible(dto);
        String token = jwtUtil.generateToken(empleado.getEmail(), empleado.getRol().name());

        AuthEmpleadoResponseDTO response = AuthEmpleadoResponseDTO.builder()
                .mensaje("Registro exitoso")
                .empleado(empleado)
                .token(token)
                .cambioRequerido(false)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ HU05 - Login empleado manual
    @PostMapping("/empleados/login/manual")
    public ResponseEntity<AuthEmpleadoResponseDTO> loginEmpleadoManual(@RequestBody LoginRequest dto) {
        AuthEmpleadoResponseDTO response = empleadoService.loginEmpleadoManual(dto);

        Rol rol = response.getEmpleado().getRol();
        if (rol != Rol.COCINERO && rol != Rol.CAJERO && rol != Rol.DELIVERY && rol != Rol.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol de empleado no válido");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDTO> loginAdmin(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(usuarioService.loginAdmin(dto));
    } // ✅ Extrae el rol contenido en el JWT enviado en la cabecera Authorization

    @GetMapping("/auth/rol")
    public ResponseEntity<?> getRolFromToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Falta token o formato incorrecto. Debe enviar: Bearer <token>"));
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            String rol = jwtUtil.extractRol(token);

            if (!jwtUtil.validateToken(token, email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token expirado o inválido"));
            }

            return ResponseEntity.ok(Map.of(
                    "rol", rol,
                    "email", email,
                    "mensaje", "Rol extraído desde JWT personalizado",
                    "source", "custom"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Error al procesar el token: " + e.getMessage()));
        }
    } // 🆕 Información completa del usuario autenticado

    @GetMapping("/auth/user-info")
    public ResponseEntity<?> getCurrentUserInfo() {
        try {
            // Implementación simplificada sin servicio extractor
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Endpoint funcionando - implementar según contexto de seguridad",
                    "timestamp", System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Error al obtener información del usuario: " + e.getMessage()));
        }
    }

    // 🔍 Endpoint de debugging para analizar tokens
    @GetMapping("/auth/debug-token")
    public ResponseEntity<?> debugToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Header debe empezar con 'Bearer '"));
        }

        try {
            String token = authHeader.substring(7).trim();
            String email = jwtUtil.extractEmail(token);
            String rol = jwtUtil.extractRol(token);

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "rol", rol,
                    "tokenPreview", token.substring(0, Math.min(50, token.length())) + "...",
                    "tokenLength", token.length(),
                    "timestamp", System.currentTimeMillis()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al analizar token: " + e.getMessage()));
        }
    }

    // 🧪 Test básico
    @GetMapping("/auth/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "mensaje", "Endpoint funcionando correctamente",
                "timestamp", System.currentTimeMillis(),
                "server", "El Saborcito Backend"));
    }

    // 🔧 Generar token de prueba con expiración personalizada
    @PostMapping("/auth/generate-test-token")
    public ResponseEntity<?> generateTestToken(@RequestBody Map<String, Object> request) {
        try {
            String email = (String) request.getOrDefault("email", "lgonzalez.sag98@gmail.com");
            String rol = (String) request.getOrDefault("rol", "CLIENTE");
            Integer hours = (Integer) request.getOrDefault("hours", 1);

            long expirationMs = hours * 60 * 60 * 1000L;
            String token = jwtUtil.generateTokenWithCustomExpiration(email, rol, expirationMs);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", email,
                    "rol", rol,
                    "expiresInHours", hours,
                    "mensaje", "Token generado con expiración de " + hours + " hora(s)"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error generando token: " + e.getMessage()));
        }
    }
}