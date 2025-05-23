package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloManufacturadoDetalleDTO {
    private Long id;
    private Integer cantidad;
    private ArticuloInsumoDTO articuloInsumo; // Usar ArticuloInsumoDTO para el insumo
    // private Long articuloManufacturadoId; // Opcional: si se necesita el ID del
    // manufacturado padre
}
