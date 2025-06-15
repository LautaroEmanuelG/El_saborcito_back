package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

@Data
public class PromocionSeleccionadaDTO {
    private Long promocionId;
    private Integer cantidad; // Cuántas veces quiere aplicar la promoción
}
