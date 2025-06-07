package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import utn.saborcito.El_saborcito_back.enums.Rol;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class RegistroEmpleadoDTO extends RegistroClienteDTO {
    // Rol seleccionable tengo dudas que se maneje aca
    private Rol rol; //  Correcto según HU4
    private Long sucursalId;
}
