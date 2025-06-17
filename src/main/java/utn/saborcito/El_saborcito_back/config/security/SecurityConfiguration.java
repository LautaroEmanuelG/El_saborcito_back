package utn.saborcito.El_saborcito_back.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;
import utn.saborcito.El_saborcito_back.services.HorarioAtencionService;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
        @Value("${auth0.audience}")
        private String audience;
        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
        private String issuer;
        @Value("${web.cors.allowed-origins}")
        private String corsAllowedOrigins;
        @Value("${spring.websecurity.debug:true}")
        boolean webSecurityDebug;

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final UsuarioRepository usuarioRepository;
        private final HorarioAtencionService horarioService;

        @Bean
        public InactivityTimeoutFilter inactivityTimeoutFilter(UsuarioRepository repo,
                        HorarioAtencionService horarioService) {
                return new InactivityTimeoutFilter(repo, horarioService);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .cors(withDefaults())
                                .addFilterBefore(inactivityTimeoutFilter(usuarioRepository, horarioService),
                                                BearerTokenAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                // Health check endpoints (públicos para Render)
                                                .requestMatchers("/healthz", "/ping", "/health", "/api/health/**")
                                                .permitAll()

                                                // Endpoints públicos
                                                .requestMatchers("/api/clientes/registro", "/api/auth/**",
                                                                "/api/articulos/**", "/api/insumos/**")
                                                .permitAll()

                                                // Endpoints de autenticación
                                                .requestMatchers("/api/auth/**").permitAll()

                                                // Endpoints de cliente (solo para clientes autenticados)
                                                .requestMatchers(
                                                                "/api/pedidos/**",
                                                                "/api/clientes/{id}/**")
                                                .hasRole("CLIENTE")

                                                // Endpoints de cocinero
                                                .requestMatchers(
                                                                "/api/pedidos/cocina/**",
                                                                "/api/articulos/manufacturados/**")
                                                .hasRole("COCINERO")

                                                // Endpoints de delivery
                                                .requestMatchers(
                                                                "/api/pedidos/entrega/**")
                                                .hasRole("DELIVERY")

                                                // Endpoints de cajero
                                                .requestMatchers(
                                                                "/api/pagos/**",
                                                                "/api/facturas/**")
                                                .hasRole("CAJERO")

                                                // Endpoints de administrador
                                                .requestMatchers(
                                                                "/api/empleados/**",
                                                                "/api/clientes/admin/**",
                                                                "/api/insumos/admin/**",
                                                                "/api/articulos/admin/**")
                                                .hasRole("ADMIN")

                                                // Cualquier otra petición requiere autenticación
                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.decoder(jwtDecoder())));
                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins));
                configuration.setAllowedMethods(
                                Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
                configuration.setMaxAge(3600L);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public JwtDecoder jwtDecoder() {
                NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuer).build();
                OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
                OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
                OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer,
                                audienceValidator);
                jwtDecoder.setJwtValidator(withAudience);
                return jwtDecoder;
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.debug(webSecurityDebug);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}