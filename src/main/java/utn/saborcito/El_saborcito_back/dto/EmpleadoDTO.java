package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {
    private Long id;
    private String legajo;
    private LocalDate fechaIngreso;
    private SucursalDTO sucursal; // Usar SucursalDTO para evitar recursividad y JSON grande
    private UsuarioDTO usuario; // Usar UsuarioDTO por las mismas razones
}
