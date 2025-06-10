package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetalleConRecetaDTO {
    private String articuloNombre;
    private Integer cantidad;
    private Boolean esManufacturado;
    private List<IngredienteDTO> receta;
}
