package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "detalle_pedido_promocion")
public class DetallePedidoPromocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "promocion_id", nullable = false)
    private Promocion promocion;

    @Column(nullable = false)
    private Integer cantidadPromocion; // Cuántas veces se aplicó la promo

    @Column(nullable = false)
    private Double precioTotalPromocion; // Precio total aplicado

    @Column(nullable = false)
    private Double ahorroTotal; // Cuánto se ahorró
}
