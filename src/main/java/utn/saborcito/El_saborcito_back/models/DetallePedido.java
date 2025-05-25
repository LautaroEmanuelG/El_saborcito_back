package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pedido_id", "articulo_id" }, name = "UK_pedido_articulo"))
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    /**
     * Calcula el subtotal del detalle de pedido (cantidad * precio del artículo)
     * 
     * @return El subtotal calculado
     */
    @Transient
    public Double calcularSubtotal() {
        if (articulo == null || articulo.getPrecioVenta() == null || cantidad == null) {
            return 0.0;
        }
        return cantidad * articulo.getPrecioVenta().doubleValue();
    }

    @Transient
    public Double calcularCosto() {
        if (articulo == null || cantidad == null) {
            return 0.0;
        }

        // Si es un artículo insumo, se toma el precio de compra directamente
        if (articulo instanceof ArticuloInsumo insumo && insumo.getPrecioCompra() != null) {
            return cantidad * insumo.getPrecioCompra();
        }

        // Si es un artículo manufacturado, calcular el costo en base a sus detalles
        if (articulo instanceof ArticuloManufacturado manufacturado && manufacturado.getDetalles() != null) {
            double costoUnitario = manufacturado.getDetalles().stream()
                    .mapToDouble(det -> {
                        double precioInsumo = det.getArticuloInsumo().getPrecioCompra() != null
                                ? det.getArticuloInsumo().getPrecioCompra()
                                : 0.0;
                        return det.getCantidad() * precioInsumo;
                    }).sum();

            return cantidad * costoUnitario;
        }

        return 0.0;
    }
}
