package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

@Data
public class ArticuloDTO {
    private Long id;
    private String denominacion;
    private Integer precioVenta;
    private Long categoriaId;
    private Long unidadMedidaId;
}
