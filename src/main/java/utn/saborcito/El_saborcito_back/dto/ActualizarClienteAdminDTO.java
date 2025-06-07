package utn.saborcito.El_saborcito_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarClienteAdminDTO {
    private Long clienteId;
    private String nombre;
    private String apellido;
    private String telefono;
    @Email
    @NotBlank
    private String email;
    private List<DomicilioDTO> domicilios;
    private Boolean estado; // Para alta/baja según HU7
}
