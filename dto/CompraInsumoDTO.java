package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraInsumoDTO {
    private Long id;
    private Double total;
    private LocalDateTime fecha;
    private String proveedor;
    private SucursalDTO sucursal;
    private List<CompraInsumoDetalleDTO> detalles;
}
