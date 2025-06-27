package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredienteDTO {
    private String nombre;
    private Double cantidad;    // ✅ Cambio de Integer a Double
    private String unidadMedida;
}