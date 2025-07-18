package utn.saborcito.El_saborcito_back.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.HorarioAtencionDTO;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;
import utn.saborcito.El_saborcito_back.services.HorarioAtencionService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static utn.saborcito.El_saborcito_back.config.security.JwtConstants.*;

/**
 * 🔧 Filtro optimizado para manejo de timeout por inactividad
 * 
 * Gestiona timeouts diferenciados por tipo de usuario y valida horarios
 * laborales.
 * Utiliza constantes centralizadas y está optimizado para el nuevo sistema JWT.
 */
@Component
@RequiredArgsConstructor
public class InactivityTimeoutFilter implements Filter {

    private static final long CLIENT_TIMEOUT = 45 * 60 * 1000; // 45 minutos
    private static final long EMPLOYEE_TIMEOUT = 30 * 60 * 1000; // 30 minutos

    private final UsuarioRepository usuarioRepository;
    private final HorarioAtencionService horarioAtencionService;
    private static final Map<String, Long> ACTIVIDAD_USUARIO = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                DecodedJWT jwt = JWT.decode(token);
                String email = jwt.getSubject();

                Usuario usuario = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                long now = System.currentTimeMillis() / 1000;
                Long ultimaActividad = ACTIVIDAD_USUARIO.get(email);

                long timeout = 0;

                if (usuario instanceof Empleado empleado) {
                    timeout = EMPLOYEE_TIMEOUT;

                    // Validar horario laboral si tiene sucursal asignada
                    if (empleado.getSucursal() != null) {
                        List<HorarioAtencionDTO> horarios = horarioAtencionService
                                .getHorariosDeSucursal(empleado.getSucursal().getId());

                        if (!horarioAtencionService.estaEnHorarioLaboral(horarios)) {
                            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Fuera del horario laboral");
                            return;
                        }
                    }

                } else if (usuario.getRol() == Rol.CLIENTE) {
                    timeout = CLIENT_TIMEOUT;

                } else if (usuario.getRol() == Rol.ADMIN ||
                        usuario.getRol() == Rol.CAJERO ||
                        usuario.getRol() == Rol.DELIVERY ||
                        usuario.getRol() == Rol.COCINERO) {
                    timeout = EMPLOYEE_TIMEOUT;
                }

                // Verificar timeout por inactividad
                if (ultimaActividad != null && (now - ultimaActividad) > timeout) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión expirada por inactividad");
                    return;
                }

                // Actualiza la última actividad
                ACTIVIDAD_USUARIO.put(email, now);

            } catch (JWTDecodeException e) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido");
                return;
            } catch (UsernameNotFoundException e) {
                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }

            // Continuar con la cadena de filtros
            chain.doFilter(request, response);

        } else {
            // Si no hay token, continuar sin validar
            chain.doFilter(request, response);
        }
    }
}