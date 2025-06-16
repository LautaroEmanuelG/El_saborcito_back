package utn.saborcito.El_saborcito_back.dto;


import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CompraDetalleDTO {
    private Long insumoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
