package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.CategoriaTipo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    private Long id;
    private String denominacion;
    private CategoriaDTO tipoCategoria;
    private CategoriaTipo tipo;
    private SucursalDTO sucursal; // <--- AGREGAR ESTA LÍNEA
}
