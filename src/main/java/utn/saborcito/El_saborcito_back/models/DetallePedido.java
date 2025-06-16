package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "pedido")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pedido_id", "articulo_id" }, name = "UK_pedido_articulo"))
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;
    @Builder.Default
    private Integer cantidadConPromocion = 0; // Cantidad que se vendió con promoción
    @Builder.Default
    private Integer cantidadSinPromocion = 0; // Cantidad que se vendió sin promoción
    @Column(name = "subtotal")
    @Builder.Default
    private Double subtotal = 0.0; // Subtotal guardado al momento de la venta (precio histórico)

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrigenDetalle origen = OrigenDetalle.INDIVIDUAL; // Origen del detalle

    @Column(name = "promocion_origen_id")
    private Long promocionOrigenId; // ID de promoción si origen=PROMOCION

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    /**
     * Calcula el subtotal del detalle de pedido solo para artículos SIN promoción
     * Los artículos con promoción se calculan en DetallePedidoPromocion
     * NOTA: Este método es para cálculos en tiempo real. Para históricos usar el
     * campo 'subtotal'
     * 
     * @return El subtotal calculado
     */
    @Transient
    public Double calcularSubtotalTiempoReal() {
        if (articulo == null || articulo.getPrecioVenta() == null || cantidadSinPromocion == null) {
            return 0.0;
        }
        return cantidadSinPromocion * articulo.getPrecioVenta().doubleValue();
    }

    /**
     * Calcula y establece el subtotal basado en precios actuales (para guardar en
     * BD)
     * Este método debe llamarse antes de persistir el DetallePedido
     * 🎁 Para artículos de promoción, el subtotal siempre es 0.0
     */
    public void calcularYEstablecerSubtotal() {
        // Si es de promoción, subtotal = 0 (se calcula en DetallePedidoPromocion)
        if (origen == OrigenDetalle.PROMOCION) {
            this.subtotal = 0.0;
            return;
        }

        // Solo artículos individuales tienen subtotal calculado
        if (articulo == null || articulo.getPrecioVenta() == null || cantidadSinPromocion == null) {
            this.subtotal = 0.0;
            return;
        }
        this.subtotal = cantidadSinPromocion * articulo.getPrecioVenta().doubleValue();
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
        if (articulo instanceof ArticuloManufacturado manufacturado
                && manufacturado.getArticuloManufacturadoDetalles() != null) {
            double costoUnitario = manufacturado.getArticuloManufacturadoDetalles().stream()
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
