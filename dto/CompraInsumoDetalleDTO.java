package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraInsumoDetalleDTO {
    private Long id;
    private ArticuloInsumoDTO articuloInsumo;
    private Double cantidad;
    private Double precioCosto;
    private Double subtotal;
}
