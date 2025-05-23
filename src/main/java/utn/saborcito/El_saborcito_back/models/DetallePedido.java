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
}
