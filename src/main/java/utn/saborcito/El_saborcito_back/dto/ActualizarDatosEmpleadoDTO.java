package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.Rol;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarDatosEmpleadoDTO {
    private String telefono;
    private String email;

    // Dirección opcional
    private String calle;
    private Integer numero;
    private String cp;
    private Long localidadId;

    // Cambio de contraseña (opcional)
    private String contraseñaActual;
    private String nuevaContraseña;
    private String confirmarContraseña;

    // Rol seleccionable
    private Rol rol;

    // Sucursal seleccionable
    private Long sucursalId;
}
