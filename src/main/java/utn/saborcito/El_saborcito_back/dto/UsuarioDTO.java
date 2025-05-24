package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// import utn.saborcito.El_saborcito_back.enums.Rol;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    // private String auth0Id;
    // private String username;
    private String email;
    // No incluir password en el DTO por seguridad
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
    // private Rol rol;
    // private Boolean estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaModificacion;
    private ImagenDTO imagen;
}
