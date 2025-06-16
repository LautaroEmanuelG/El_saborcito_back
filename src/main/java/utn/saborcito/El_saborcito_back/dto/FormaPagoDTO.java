package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormaPagoDTO {
    private Long id;
    private String nombre;
}
