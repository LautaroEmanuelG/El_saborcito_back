package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductoRankingConResumenDTO {
    private List<ProductoRankingDTO> productos;
    private Long totalManufacturados;
    private Long totalInsumos;
}
