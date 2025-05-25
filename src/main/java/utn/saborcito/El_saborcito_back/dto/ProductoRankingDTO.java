package utn.saborcito.El_saborcito_back.dto;
import lombok.*;

@Data
@AllArgsConstructor
public class ProductoRankingDTO {
    private Long id;
    private String denominacion;
    private Long cantidadVendida;
    private String tipoProducto; // "MANUFACTURADO" o "INSUMO"
}
