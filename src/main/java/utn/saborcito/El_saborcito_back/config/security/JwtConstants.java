package utn.saborcito.El_saborcito_back.config.security;

import java.util.List;

/**
 * 🔧 Constantes comunes para la gestión de JWT
 * 
 * Centraliza todas las constantes relacionadas con JWT para evitar duplicación
 * y facilitar el mantenimiento.
 */
public final class JwtConstants {

    // 📝 Nombres de claims donde pueden estar los roles
    public static final List<String> ROLE_CLAIM_NAMES = List.of(
            "https://schemas.quickstarts.com/roles", // Auth0 default
            "https://saborcito.com/roles", // Custom namespace
            "roles", // Simple claim
            "rol" // Tu claim personalizado
    );

    // 🔍 Headers HTTP
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // 📋 Atributos de request para pasar información entre filtros
    public static final String TOKEN_TYPE_ATTRIBUTE = "jwt.token.type";
    public static final String TOKEN_RAW_ATTRIBUTE = "jwt.token.raw";

    // 🎯 Claims comunes
    public static final String EMAIL_CLAIM = "email";
    public static final String ROLE_CLAIM = "rol";
    public static final String ROLES_CLAIM = "roles";

    // Constructor privado para evitar instanciación
    private JwtConstants() {
        throw new UnsupportedOperationException("Esta es una clase de constantes");
    }
}
