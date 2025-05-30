package utn.saborcito.El_saborcito_back.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEmpleadoResponseDTO {
    private String mensaje;
    private Boolean cambioRequerido;
    private UsuarioDTO usuario;
}
