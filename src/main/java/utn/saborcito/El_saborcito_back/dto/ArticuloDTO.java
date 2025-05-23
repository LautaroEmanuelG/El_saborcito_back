package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloDTO {
    private Long id;
    private String denominacion;
    private Double precioVenta; // Corregido de Integer a Double
    private Long categoriaId;
    private Long unidadMedidaId;
}
