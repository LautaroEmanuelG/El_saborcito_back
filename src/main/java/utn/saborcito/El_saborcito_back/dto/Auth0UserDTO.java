package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auth0UserDTO {
    private String sub; // ID único de Auth0
    private String email;
    private String givenName; // Nombre
    private String familyName; // Apellido
}
