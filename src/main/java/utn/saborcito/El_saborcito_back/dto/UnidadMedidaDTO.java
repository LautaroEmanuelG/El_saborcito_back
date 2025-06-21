package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadMedidaDTO {
    private Long id;
    private String denominacion;
    private Boolean eliminado; // ← NUEVO CAMPO
}