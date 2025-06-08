package utn.saborcito.El_saborcito_back.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmpleadoDTO extends UsuarioDTO {
    private String legajo;
    private LocalDate fechaIngreso;
    private SucursalDTO sucursal;
    private Boolean primerLogin; //  necesario para HU5
}
