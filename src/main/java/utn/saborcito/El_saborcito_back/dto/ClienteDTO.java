package utn.saborcito.El_saborcito_back.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClienteDTO extends UsuarioDTO {
    // Campos específicos del cliente si los hay
}
