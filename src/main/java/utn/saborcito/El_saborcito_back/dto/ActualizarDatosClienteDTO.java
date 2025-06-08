package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarDatosClienteDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    @Email
    @NotBlank
    private String email;
    // Dirección permite actualizar HU3
    private List<DomicilioDTO> domicilios;
    // Contraseña permite actualizar HU3
    private String contraseñaActual;
    private String nuevaContraseña;
    private String confirmarContraseña;
}

