//package utn.saborcito.El_saborcito_back.config.security;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import utn.saborcito.El_saborcito_back.enums.Rol;
//import utn.saborcito.El_saborcito_back.models.Usuario;
//import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;
//
//@Component
//public class InactivityTimeoutFilter implements Filter {
//    private static final long CLIENT_TIMEOUT = 45 * 60; // 45 minutos
//    private static final long EMPLOYEE_TIMEOUT = 30 * 60; // 30 minutos
//    private final UsuarioRepository usuarioRepository;
//
//    public InactivityTimeoutFilter(UsuarioRepository usuarioRepository) {
//        this.usuarioRepository = usuarioRepository;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        String authHeader = httpRequest.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                DecodedJWT jwt = JWT.decode(token);
//                String email = jwt.getSubject(); // El 'sub' es el email o auth0Id
//                Usuario usuario = usuarioRepository.findByEmail(email)
//                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//                long issuedAt = jwt.getIssuedAt().getTime() / 1000;
//                long now = System.currentTimeMillis() / 1000;
//                long timeout = 0;
//                if (usuario.getRol() == Rol.CLIENTE) {
//                    timeout = CLIENT_TIMEOUT;
//                } else if (usuario.getRol() == Rol.ADMIN ||
//                        usuario.getRol() == Rol.CAJERO ||
//                        usuario.getRol() == Rol.COCINERO ||
//                        usuario.getRol() == Rol.DELIVERY) {
//                    timeout = EMPLOYEE_TIMEOUT;
//                }
//                if (timeout > 0 && (now - issuedAt) > timeout) {
//                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión expirada por inactividad");
//                    return;
//                }
//            } catch (JWTDecodeException e) {
//                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido");
//                return;
//            } catch (UsernameNotFoundException e) {
//                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
//                return;
//            }
//        }
//        chain.doFilter(request, response);
//    }
//}
