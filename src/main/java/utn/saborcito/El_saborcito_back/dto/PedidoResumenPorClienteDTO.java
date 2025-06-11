package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoResumenPorClienteDTO {
    private Long idPedido;
    private LocalDate fechaPedido;
    private Double total;
    private List<DetallePedidoDTO> detalles;
}

