package utn.saborcito.El_saborcito_back.dto;


import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CompraDetalleDTO {
    private Long insumoId;
    private Double cantidad;    // ✅ Cambio de Integer a Double
    private Double precioUnitario;
    private Double subtotal;
}
