package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDTO {
    private Long id;
    private String denominacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private LocalTime horaDesde;
    private LocalTime horaHasta;
    private Double descuento;
    private Double precioPromocional;
    private SucursalDTO sucursal;
    private ImagenDTO imagen; // 🆕 Nueva imagen para la promoción
    private List<PromocionDetalleDTO> promocionDetalles; // 🆕 Nueva lista de detalles
    private Boolean eliminado;

    // @Deprecated - Mantener por compatibilidad temporal
    private ArticuloDTO articulo;
}
