package utn.saborcito.El_saborcito_back.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 🔧 Servicio unificado para extraer información de roles desde cualquier tipo
 * de JWT
 * 
 * Este servicio maneja:
 * - Tokens JWT de Auth0 (con custom claims)
 * - Tokens JWT personalizados (generados por JwtUtil)
 * - Extracción segura de roles desde el contexto de seguridad
 */
@Service
public class JwtRoleExtractorService {

    @Autowired
    private JwtUtil jwtUtil;

    // 📝 Posibles nombres de claims donde pueden estar los roles
    private static final List<String> ROLE_CLAIM_NAMES = List.of(
            "https://schemas.quickstarts.com/roles", // Auth0 default
            "https://saborcito.com/roles", // Custom namespace
            "roles", // Simple claim
            "rol" // Tu claim personalizado
    );

    /**
     * 🎯 Extrae el rol del usuario autenticado actual
     * 
     * @return El rol del usuario o null si no se puede determinar
     */
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        // 🔍 Si es un JWT de OAuth2 (Auth0)
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return extractRoleFromAuth0Jwt(jwtAuth.getToken());
        }

        // 🔍 Si es autenticación tradicional con nuestro JWT personalizado
        if (authentication.getPrincipal() instanceof String email) {
            return extractRoleFromCustomJwt(email);
        }

        return null;
    }

    /**
     * 📧 Extrae el email del usuario autenticado actual
     * 
     * @return El email del usuario o null si no se puede determinar
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        // 🔍 Si es un JWT de OAuth2 (Auth0)
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();

            // Intentar obtener email del claim
            String email = jwt.getClaimAsString("email");
            if (email != null && !email.isEmpty()) {
                return email;
            }

            // Fallback al subject si no hay email
            return jwt.getSubject();
        }

        // 🔍 Si es autenticación tradicional
        if (authentication.getPrincipal() instanceof String email) {
            return email;
        }

        return null;
    }

    /**
     * 🎯 Extrae el rol desde un JWT de Auth0
     */
    private String extractRoleFromAuth0Jwt(Jwt jwt) {
        // 🔍 Buscar en diferentes claims posibles
        for (String claimName : ROLE_CLAIM_NAMES) {
            Object rolesClaim = jwt.getClaim(claimName);

            if (rolesClaim != null) {
                return extractSingleRole(rolesClaim);
            }
        }

        return null;
    }

    /**
     * 🎯 Extrae el rol desde nuestro JWT personalizado
     */
    private String extractRoleFromCustomJwt(String email) {
        try {
            // 🔍 Buscar el token en las cabeceras de la request actual
            String token = getCurrentRequestToken();

            if (token != null && jwtUtil.validateToken(token, email)) {
                return jwtUtil.extractRol(token);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al extraer rol del JWT personalizado: " + e.getMessage());
        }

        return null;
    }

    /**
     * 🔄 Convierte el claim de roles a un solo rol (primer rol si es lista)
     */
    private String extractSingleRole(Object rolesClaim) {
        try {
            // 📝 Si es una lista, tomar el primer rol
            if (rolesClaim instanceof List<?> rolesList && !rolesList.isEmpty()) {
                Object firstRole = rolesList.get(0);
                if (firstRole instanceof String) {
                    return ((String) firstRole).toUpperCase();
                }
            }

            // 📝 Si es un string directo
            if (rolesClaim instanceof String singleRole) {
                return singleRole.toUpperCase();
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error al procesar rol: " + e.getMessage());
        }

        return null;
    }

    /**
     * 🔍 Obtiene el token JWT de la request actual
     * (Implementación simplificada - en producción podrías usar
     * RequestContextHolder)
     */
    private String getCurrentRequestToken() {
        // Esta es una implementación básica
        // En un escenario real, podrías usar RequestContextHolder para obtener la
        // request actual
        return null;
    }

    /**
     * 🎯 Método de utilidad para extraer rol desde un token JWT string
     * 
     * @param token Token JWT como string
     * @return El rol extraído o null
     */
    public String extractRoleFromTokenString(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        try {
            // 🔍 Intentar con nuestro JWT personalizado primero
            String email = jwtUtil.extractEmail(token);
            if (email != null && jwtUtil.validateToken(token, email)) {
                return jwtUtil.extractRol(token);
            }
        } catch (Exception e) {
            // Si falla, podría ser un token de Auth0, pero necesitaríamos decodificarlo
            System.err.println("⚠️ No se pudo extraer rol del token: " + e.getMessage());
        }

        return null;
    }

    /**
     * 📊 Información completa del usuario actual
     */
    public Map<String, String> getCurrentUserInfo() {
        String email = getCurrentUserEmail();
        String rol = getCurrentUserRole();

        return Map.of(
                "email", email != null ? email : "unknown",
                "rol", rol != null ? rol : "unknown");
    }
}
