package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticuloInsumoDTO extends ArticuloDTO {
    private Double precioCompra;
    private Double stockActual;    // ✅ Cambio de Integer a Double
    private Double stockMinimo;    // ✅ Cambio de Integer a Double
    private Boolean esParaElaborar;
    private UnidadMedidaDTO unidadMedida; // Para mapear completo
    private CategoriaDTO categoria;
}
