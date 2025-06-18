package utn.saborcito.El_saborcito_back.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        @Value("${web.cors.allowed-origins}")
        private String corsAllowedOrigins;

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                                .authorizeHttpRequests(auth -> auth
                                                // ----------------------------------------------------------------
                                                // 🔓 CONFIGURACIÓN DE ACCESO PÚBLICO TOTAL (SOLO PARA DESARROLLO)
                                                // ----------------------------------------------------------------
                                                // Descomentar la siguiente línea para permitir el acceso a TODOS los
                                                // endpoints sin autenticación.
                                                .requestMatchers("/**").permitAll()

                                // ----------------------------------------------------------------
                                // 🔐 EJEMPLO DE CONFIGURACIÓN DE SEGURIDAD POR ROLES
                                // ----------------------------------------------------------------
                                // Una vez que la autenticación funcione, comenta la línea
                                // .requestMatchers("/**").permitAll()
                                // y descomenta las siguientes líneas para aplicar seguridad real.
                                /*
                                 * // Endpoints públicos (login, registro, etc.)
                                 * .requestMatchers("/api/auth/**").permitAll()
                                 * .requestMatchers(
                                 * "/api/usuarios/clientes/registrar",
                                 * "/api/usuarios/empleados/registrar")
                                 * .permitAll()
                                 * 
                                 * // Endpoints solo para ADMIN
                                 * .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                 * 
                                 * // Endpoints para roles de empleado
                                 * .requestMatchers("/api/pedidos/**").hasAnyRole("ADMIN", "COCINERO", "CAJERO")
                                 * .requestMatchers("/api/facturas/**")
                                 * .requestMatchers("/api/insumos/**").hasAnyRole("ADMIN", "COCINERO")
                                 * 
                                 * // Endpoints para clientes
                                 * .requestMatchers("/api/clientes/mi-perfil/**").hasRole("CLIENTE")
                                 * 
                                 * // Cualquier otra petición requiere autenticación
                                 * .anyRequest().authenticated()
                                 */
                                )
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(corsAllowedOrigins.split(",")));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}