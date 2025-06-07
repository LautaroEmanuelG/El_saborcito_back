package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.Rol;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroEmpleadoDTO extends RegistroClienteDTO {
    // Rol seleccionable tengo dudas que se maneje aca
    private Rol rol; //  Correcto según HU4
    private Long sucursalId;
}
