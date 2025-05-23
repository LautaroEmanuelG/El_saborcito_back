package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    private Long id;
    private LocalTime horasEstimadaFinalizacion;
    private Double total;
    private Double totalCosto;
    private LocalDate fechaPedido;
    private EstadoDTO estado;
    private TipoEnvioDTO tipoEnvio;
    private FormaPagoDTO formaPago;
    private ClienteDTO cliente;
    private SucursalDTO sucursal;
    private List<DetallePedidoDTO> detalles;
}
