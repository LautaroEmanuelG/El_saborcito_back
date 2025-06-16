package utn.saborcito.El_saborcito_back.dto;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class NuevaCompraDTO {
    private String denominacion;
    private List<CompraDetalleDTO> detalles;
}
