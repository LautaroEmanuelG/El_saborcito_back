package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaDTO {
    private Long id;
    private String nombre;
    private PaisDTO pais;
}
