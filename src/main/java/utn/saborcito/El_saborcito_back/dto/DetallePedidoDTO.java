package utn.saborcito.El_saborcito_back.dto;

// Asumiendo que ArticuloDTO ya existe
// import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private Long id;
    private Integer cantidad;
    private Integer cantidadConPromocion;
    private Integer cantidadSinPromocion;
    private Double subtotal; // 💰 Subtotal guardado al momento de la venta
    private OrigenDetalle origen; // 🏷️ Origen del detalle (INDIVIDUAL/PROMOCION)
    private Long promocionOrigenId; // 🎁 ID de promoción si origen=PROMOCION
    private ArticuloDTO articulo; // Usar ArticuloDTO para evitar recursividad
}
