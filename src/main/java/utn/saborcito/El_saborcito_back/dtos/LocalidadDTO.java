package utn.saborcito.El_saborcito_back.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalidadDTO {
    private Long id;
    private String nombre;
    private ProvinciaDTO provincia;
}
