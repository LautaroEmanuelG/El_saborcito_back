package utn.saborcito.El_saborcito_back.dto;

// Asumiendo que ArticuloDTO ya existe
// import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private Long id;
    private Integer cantidad;
    private ArticuloDTO articulo; // Usar ArticuloDTO
    private Double subtotal; // Campo calculado, se puede poblar en el mapper o servicio
}
