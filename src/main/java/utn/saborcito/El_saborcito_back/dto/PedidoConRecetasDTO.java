package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoConRecetasDTO {
    private Long id;
    private LocalDate fecha;
    private String cliente;
    private List<DetalleConRecetaDTO> detalles;
}
