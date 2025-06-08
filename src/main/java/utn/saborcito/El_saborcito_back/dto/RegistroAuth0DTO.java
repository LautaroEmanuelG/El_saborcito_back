package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true) // Cambiar a SuperBuilder para herencia
public class RegistroAuth0DTO {
    private String sub; // ID único de Auth0
    private String email;
    private String givenName; // Nombre
    private String familyName; // Apellido
    private List<DomicilioDTO> domicilios; // Lista de domicilios>
}
