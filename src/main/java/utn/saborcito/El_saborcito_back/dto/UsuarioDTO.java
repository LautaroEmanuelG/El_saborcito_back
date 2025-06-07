package utn.saborcito.El_saborcito_back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import utn.saborcito.El_saborcito_back.enums.Rol;
// import utn.saborcito.El_saborcito_back.enums.Rol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true) // Cambiar a SuperBuilder para herencia
public class UsuarioDTO {
    private Long id;
    @Email
    @NotBlank
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
    private Rol rol;
    private Boolean estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaModificacion;
    @JsonInclude(JsonInclude.Include.NON_NULL) // No incluir si es null
    private List<DomicilioDTO> domicilios;
    private ImagenDTO imagen;
    
}
