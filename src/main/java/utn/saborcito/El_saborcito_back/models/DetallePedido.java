package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(DetallePedidoId.class)
public class DetallePedido {
    @Id
    @Column(name = "pedido_id", insertable = false, updatable = false)
    private Long pedidoId;

    @Id
    @Column(name = "articulo_id", insertable = false, updatable = false)
    private Long articuloId;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "articulo_id", referencedColumnName = "id")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
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

    /**
     * Método helper para establecer los valores de la clave primaria compuesta
     */
    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (pedido != null) {
            this.pedidoId = pedido.getId();
        }
        if (articulo != null) {
            this.articuloId = articulo.getId();
        }
    }
}
