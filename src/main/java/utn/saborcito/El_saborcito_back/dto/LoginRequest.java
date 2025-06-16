package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Clase para el login HU2
public class LoginRequest {
    @Email
    @NotBlank
    private String email;
    private String password;
    private String auth0Id;
    private Boolean esAuth0Login; // Para saber si viene de Auth0 o manual
}
