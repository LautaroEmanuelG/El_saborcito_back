package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloDTO {
    private Long id;
    private String denominacion;
    private Double precioVenta;
    private Long categoriaId;
    private ImagenDTO imagen;

    // Campos para delete lógico
    private Boolean eliminado;
    private LocalDateTime fechaEliminacion;
}
