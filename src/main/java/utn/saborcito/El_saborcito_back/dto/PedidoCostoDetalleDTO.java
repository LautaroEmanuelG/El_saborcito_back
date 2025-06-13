package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCostoDetalleDTO {
    private Long idPedido;
    private LocalDate fechaPedido;
    private Double totalCosto;
}