package utn.saborcito.El_saborcito_back.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleFacturaRenderizadoDTO {
    private String descripcion;
    private int cantidad;
    private double subtotal;
    private boolean esPromocion;

    private List<String> articulosIncluidos;
}

