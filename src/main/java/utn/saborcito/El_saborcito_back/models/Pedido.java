package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime horasEstimadaFinalizacion;
    private Double total;
    private Double totalCosto;
    private LocalDate fechaPedido;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "tipo_envio_id")
    private TipoEnvio tipoEnvio;

    @ManyToOne
    @JoinColumn(name = "forma_pago_id")
    private FormaPago formaPago;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePedido> detalles;

     /**
     * Calcula el costo total del pedido (sumando el costo de cada detalle)
     * @return el costo total calculado
     */
    @Transient
    public Double calcularCostoTotal() {
        if (detalles == null || detalles.isEmpty()) return 0.0;
        return detalles.stream()
                .mapToDouble(DetallePedido::calcularCosto)
                .sum();
    }
}
