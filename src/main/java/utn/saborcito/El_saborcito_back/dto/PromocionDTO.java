package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private SucursalDTO sucursal; // Se asume que SucursalDTO ya existe en este paquete
    private ArticuloDTO articulo; // Se asume que ArticuloDTO ya existe en este paquete o se importará correctamente
}
