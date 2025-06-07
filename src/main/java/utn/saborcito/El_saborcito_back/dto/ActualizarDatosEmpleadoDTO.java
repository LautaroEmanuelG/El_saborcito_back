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
public class ActualizarDatosEmpleadoDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    @Email
    @NotBlank
    private String email;
    private List<DomicilioDTO> domicilios;

    // Cambio de contraseña
    //deberia poner aca el de dto de CambiarPasswordDTO?
    private String contraseñaActual;
    private String nuevaContraseña;
    private String confirmarContraseña;
}

