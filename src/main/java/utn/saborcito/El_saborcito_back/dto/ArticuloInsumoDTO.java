package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloInsumoDTO {
    private Long id;
    private String denominacion;
    private Double precioVenta;
    // private Double precioCompra; // Considerar si es necesario para el cliente
    // private Integer stockActual; // Considerar si es necesario para el cliente
    // private Integer stockMaximo; // Considerar si es necesario para el cliente
    // private Boolean esParaElaborar; // Considerar si es necesario para el cliente
    private ImagenDTO imagen;
    private CategoriaDTO categoria; // Corregido de CategoriaDTO a CategoriaDTO
    private UnidadMedidaDTO unidadMedida; // Descomentado y añadido
}
