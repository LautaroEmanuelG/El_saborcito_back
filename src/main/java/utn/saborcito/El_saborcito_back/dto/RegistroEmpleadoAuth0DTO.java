package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import utn.saborcito.El_saborcito_back.enums.Rol;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class RegistroEmpleadoAuth0DTO extends RegistroAuth0DTO {
    private Rol rol;
    private SucursalDTO sucursal;
}
