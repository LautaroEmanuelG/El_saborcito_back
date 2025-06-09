package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true) // Cambiar a SuperBuilder para herencia
//Se maneja para HU1
public class RegistroClienteDTO {
    private String auth0Id;
    private String nombre;
    private String apellido;
    private String telefono;
    //cumple criterio de HU1
    @Email(message = "El email no es válido")
    @NotBlank
    private String email;

    // ✅ MEJOR: Solo para registro manual (sin Auth0)
    private String password; // Solo para registro manual
    private String confirmarPassword; // Solo para registro manual
    private LocalDate fechaNacimiento;
    private List<DomicilioDTO> domicilios;
    @Builder.Default
    private Boolean esAuth0 = false;
}
