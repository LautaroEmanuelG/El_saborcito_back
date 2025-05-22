package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class DetallePedidoId implements Serializable {
    private Long pedidoId;
    private Long articuloId;
}
