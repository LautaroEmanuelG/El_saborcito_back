package utn.saborcito.El_saborcito_back.dto;


import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class CompraInsumoDTO {
    private Long id;
    private String denominacion;
    private LocalDate fechaCompra;
    private Double totalCompra;
    private List<CompraDetalleDTO> detalles;
}
