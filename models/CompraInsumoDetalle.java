package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraInsumoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "compra_insumo_id", nullable = false)
    private CompraInsumo compraInsumo;

    @ManyToOne
    @JoinColumn(name = "articulo_insumo_id", nullable = false)
    private ArticuloInsumo articuloInsumo;

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private Double precioCosto;

    @Column(nullable = false)
    private Double subtotal;
}
