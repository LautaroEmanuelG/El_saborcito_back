package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {
    private Long clienteId;
    private Long sucursalId;
    private Long tipoEnvioId;
    private Long formaPagoId;
    private Long estadoId;
    private DomicilioDTO domicilio;
    private Long domicilioExistenteId;
    private List<DetallePedidoRequestDTO> detalles;
    private List<PromocionSeleccionadaDTO> promocionesSeleccionadas; // 🆕 Nuevo campo
}
