package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

/**
 * 📝 DTO simplificado para la creación de pedidos desde el frontend
 * Solo contiene los campos esenciales que debe enviar el cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreacionDTO {

    // Información del pedido
    private LocalTime horasEstimadaFinalizacion; // Opcional, se calcula automáticamente

    // Referencias por ID (solo se necesitan los IDs)
    private Long estadoId; // Estado inicial (ej: PENDIENTE)
    private Long tipoEnvioId; // DELIVERY o TAKE_AWAY
    private Long formaPagoId; // EFECTIVO, MERCADO_PAGO, etc.
    private Long clienteId; // ID del cliente que hace el pedido
    private Long sucursalId; // ID de la sucursal

    // Domicilio (puede ser nuevo o existente)
    private DomicilioCreacionDTO domicilio; // null si es TAKE_AWAY o usa domicilio existente
    private Long domicilioExistenteId; // ID del domicilio existente // Detalles del pedido
    private List<DetallePedidoCreacionDTO> detalles;

    // 🎁 Promociones seleccionadas por el cliente
    private List<PromocionSeleccionadaDTO> promocionesSeleccionadas;

    /**
     * 🏠 DTO para crear un nuevo domicilio junto con el pedido
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomicilioCreacionDTO {
        private String calle;
        private Integer numero;
        private String cp;
        private Double latitud;
        private Double longitud;
        private Long localidadId; // ID de la localidad
    }

    /**
     * 🛒 DTO para los detalles del pedido
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetallePedidoCreacionDTO {
        private Integer cantidad;
        private Long articuloId; // ID del artículo (puede ser insumo o manufacturado)
    }
}
