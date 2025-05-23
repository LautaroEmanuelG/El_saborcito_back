package utn.saborcito.El_saborcito_back.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomicilioDTO {
    private Long id;
    private String calle;
    private Integer numero;
    private String cp;
    private LocalidadDTO localidad;
}
