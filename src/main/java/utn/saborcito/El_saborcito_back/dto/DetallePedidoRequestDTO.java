package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

@Data
public class DetallePedidoRequestDTO {
    private Integer cantidad;
    private Long articuloId;
}
