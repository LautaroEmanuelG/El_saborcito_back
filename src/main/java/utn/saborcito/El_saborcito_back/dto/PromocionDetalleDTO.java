package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDetalleDTO {
    private Long id;
    private Long promocionId;
    private ArticuloDTO articulo;
    private Integer cantidadRequerida;
}
