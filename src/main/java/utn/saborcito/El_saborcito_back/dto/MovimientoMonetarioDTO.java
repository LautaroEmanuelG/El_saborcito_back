package utn.saborcito.El_saborcito_back.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoMonetarioDTO {
    private double ingresos;
    private double costos;
    private double ganancias;
}
