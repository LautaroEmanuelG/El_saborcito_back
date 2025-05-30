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
public class RegistroEmpleadoDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;
    private String confirmPassword;

    // Dirección opcional
    private String calle;
    private Integer numero;
    private String cp;
    private Long localidadId;

    // Rol seleccionable
    private Rol rol;

    private Long sucursalId;
}
