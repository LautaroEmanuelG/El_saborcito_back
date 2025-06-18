package utn.saborcito.El_saborcito_back.config.auth0;

/**
 * 📝 Configuración y documentación para Auth0 Actions/Rules
 * 
 * Esta clase contiene la documentación y ejemplos de código para configurar
 * Auth0 Actions que añaden roles como custom claims en los tokens JWT.
 * 
 * 🔧 INSTRUCCIONES PARA CONFIGURAR AUTH0:
 * 
 * 1. Ve a tu dashboard de Auth0 → Actions → Flows
 * 2. Selecciona el flow "Login"
 * 3. Crea una nueva Action con el código JavaScript que se encuentra abajo
 * 4. Agrega la Action al flow de Login
 * 
 * 🎯 OBJETIVO:
 * Los tokens JWT que Auth0 genere incluirán los roles del usuario como custom
 * claims,
 * permitiendo que nuestro backend Spring Boot los pueda leer y usar para
 * autorización.
 */
public class Auth0ActionsDocumentation {

    /**
     * 🚀 Auth0 Post-Login Action para añadir roles a los tokens
     * 
     * Copia y pega este código JavaScript en una nueva Action en Auth0:
     * 
     * ==================== CÓDIGO PARA AUTH0 ACTION ====================
     * 
     * exports.onExecutePostLogin = async (event, api) => {
     * // 📋 Obtener los roles del usuario desde Auth0
     * const roles = event.authorization?.roles || [];
     * 
     * // 🎯 Añadir roles como custom claim al ID Token
     * if (roles.length > 0) {
     * api.idToken.setCustomClaim('https://saborcito.com/roles', roles);
     * api.idToken.setCustomClaim('rol', roles[0]); // Para compatibilidad
     * }
     * 
     * // 🎯 Añadir roles como custom claim al Access Token
     * if (roles.length > 0) {
     * api.accessToken.setCustomClaim('https://saborcito.com/roles', roles);
     * api.accessToken.setCustomClaim('rol', roles[0]); // Para compatibilidad
     * }
     * 
     * // 📧 Asegurar que el email esté disponible
     * if (event.user.email) {
     * api.idToken.setCustomClaim('email', event.user.email);
     * api.accessToken.setCustomClaim('email', event.user.email);
     * }
     * };
     * 
     * ========================== FIN DEL CÓDIGO ==========================
     * 
     * 📝 EXPLICACIÓN:
     * - `event.authorization.roles`: Contiene los roles asignados al usuario en
     * Auth0
     * - `api.idToken.setCustomClaim()`: Añade custom claims al ID Token
     * - `api.accessToken.setCustomClaim()`: Añade custom claims al Access Token
     * - Se usa namespace `https://saborcito.com/roles` para evitar conflictos
     * - También se añade claim simple `rol` para compatibilidad con el código
     * existente
     */

    /**
     * 🔧 CONFIGURACIÓN DE ROLES EN AUTH0:
     * 
     * 1. Ve a User Management → Roles
     * 2. Crea los siguientes roles:
     * - ADMIN
     * - CLIENTE
     * - COCINERO
     * - CAJERO
     * - DELIVERY
     * 
     * 3. Ve a User Management → Users
     * 4. Selecciona un usuario y asígnale uno o más roles
     * 
     * 5. Asegúrate de que la configuración RBAC esté habilitada:
     * - Ve a Applications → APIs → Tu API
     * - En Settings, habilita "Enable RBAC" y "Add Permissions in the Access Token"
     */

    /**
     * 🧪 TESTING:
     * 
     * Una vez configurado, cuando un usuario se autentique, el JWT contendrá:
     * 
     * {
     * "iss": "https://tu-tenant.auth0.com/",
     * "sub": "auth0|user_id",
     * "aud": "tu-api-identifier",
     * "email": "usuario@example.com",
     * "https://saborcito.com/roles": ["ADMIN"],
     * "rol": "ADMIN",
     * "iat": 1672531200,
     * "exp": 1672617600
     * }
     */

    /**
     * 🔍 VERIFICACIÓN:
     * 
     * Puedes verificar que los tokens incluyen los roles usando:
     * 1. jwt.io para decodificar manualmente un token
     * 2. El endpoint GET /api/auth/rol de tu aplicación
     * 3. El endpoint GET /api/auth/user-info para información completa
     */

    // 📚 Referencias útiles
    public static final String AUTH0_ACTIONS_DOCS = "https://auth0.com/docs/customize/actions";
    public static final String AUTH0_CUSTOM_CLAIMS_DOCS = "https://auth0.com/docs/secure/tokens/json-web-tokens/create-custom-claims";
    public static final String AUTH0_RBAC_DOCS = "https://auth0.com/docs/manage-users/access-control/rbac";
}
