package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 📋 DTO para el historial de pedidos del cliente
 * Evita referencias circulares y optimiza la respuesta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialPedidoDTO {
    private Long id;
    private LocalDateTime fechaRegistro;
    private String observacion;

    // Datos básicos del cliente
    private ClienteBasicoDTO cliente;

    // Datos básicos del pedido
    private PedidoBasicoDTO pedido;

    /**
     * 👤 Información básica del cliente
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteBasicoDTO {
        private Long id;
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
    }

    /**
     * 🛒 Información básica del pedido
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PedidoBasicoDTO {
        private Long id;
        private LocalDate fechaPedido;
        private LocalTime horasEstimadaFinalizacion;
        private Double total;
        private String estado;
        private String tipoEnvio;
        private String formaPago;
        private String sucursal;
        private Integer cantidadArticulos;
    }
}
