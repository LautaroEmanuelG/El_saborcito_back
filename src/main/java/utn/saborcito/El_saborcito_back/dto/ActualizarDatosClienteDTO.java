package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarDatosClienteDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    // Dirección
    private String calle;
    private Integer numero;
    private String cp;
    private Long localidadId;

    // Contraseña
    private String contraseñaActual;
    private String nuevaContraseña;
    private String confirmarContraseña;
}
