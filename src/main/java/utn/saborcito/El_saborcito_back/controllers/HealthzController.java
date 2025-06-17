package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 🏥 Health Check endpoint estándar para Render
 * Endpoint simplificado que Render usa para monitoreo
 */
@RestController
@RequiredArgsConstructor
public class HealthzController {

    private final DataSource dataSource;

    /**
     * Health check principal para Render
     * Render requiere que responda "OK" cuando todo está bien
     */
    @GetMapping("/healthz")
    public ResponseEntity<String> healthz() {
        try {
            // Verificación rápida de conexión a BD
            try (Connection connection = dataSource.getConnection()) {
                boolean dbHealthy = connection.isValid(2); // timeout muy corto para Render

                if (dbHealthy) {
                    return ResponseEntity.ok("OK");
                } else {
                    return ResponseEntity.status(503).body("Database connection failed");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Health check failed: " + e.getMessage());
        }
    }

    /**
     * Ping simple para verificar que el servicio responde
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    /**
     * Health check detallado para debugging
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();

        try {
            // Verificar BD
            try (Connection connection = dataSource.getConnection()) {
                boolean dbHealthy = connection.isValid(3);

                health.put("status", dbHealthy ? "UP" : "DOWN");
                health.put("timestamp", LocalDateTime.now().toString());
                health.put("service", "El_Saborcito_Backend");
                health.put("database", dbHealthy ? "CONNECTED" : "DISCONNECTED");

                // Info del sistema
                Runtime runtime = Runtime.getRuntime();
                Map<String, Object> system = new HashMap<>();
                system.put("maxMemory", runtime.maxMemory() / (1024 * 1024) + " MB");
                system.put("freeMemory", runtime.freeMemory() / (1024 * 1024) + " MB");
                system.put("processors", runtime.availableProcessors());
                health.put("system", system);

                return dbHealthy ? ResponseEntity.ok(health) : ResponseEntity.status(503).body(health);
            }
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            health.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(503).body(health);
        }
    }
}
