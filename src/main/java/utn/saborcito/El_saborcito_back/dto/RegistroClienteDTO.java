package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Se maneja para HU1
public class RegistroClienteDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    //cumple criterio de HU1
    @Email(message = "El email no es válido")
    @NotBlank
    private String email;
    // ⚠️ PROBLEMA: Con Auth0, estos campos no deberían estar aquí
    // private String password;
    // private String confirmPassword;
    // ✅ MEJOR: Solo para registro manual (sin Auth0)
    private String password; // Solo si permites registro manual
    private String confirmarPassword; // Solo si permites registro manual
    private LocalDate fechaNacimiento;
    private List<DomicilioDTO> domicilios;
    // ✅ AGREGAR: Para manejar Auth0
    private String auth0Id; // Cuando viene de Auth0
    private Boolean esRegistroAuth0; // Flag para distinguir tipo de registro
}
