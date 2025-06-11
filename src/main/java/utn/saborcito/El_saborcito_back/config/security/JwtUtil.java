package utn.saborcito.El_saborcito_back.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        try {
            // Intentamos decodificar en Base64 primero
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            // Si falla la decodificación, usamos la clave directamente codificada en Base64
            byte[] keyBytes = Base64.getEncoder().encode(secretKeyString.getBytes());
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    // Generar token con email y rol
    public String generateToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Extraer cualquier claim del token (helper)
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody());
    }

    // Extraer email
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer rol
    public String extractRol(String token) {
        return extractClaim(token, claims -> (String) claims.get("rol"));
    }

    // Valida si el token es válido para ese usuario y no expirado
    public boolean validateToken(String token, String expectedEmail) {
        final String email = extractEmail(token);
        return email.equals(expectedEmail) && !isTokenExpired(token);
    }

    // Verifica si el token está expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}