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

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePedidoPromocion> promociones;

    /**
     * Calcula el costo total del pedido (sumando el costo de cada detalle)
     * 
     * @return el costo total calculado
     */
    @Transient
    public Double calcularCostoTotal() {
        if (detalles == null || detalles.isEmpty())
            return 0.0;
        return detalles.stream()
                .mapToDouble(DetallePedido::calcularCosto)
                .sum();
    }

    /**
     * Calcula el total del pedido incluyendo promociones
     * 
     * @return el total calculado
     */
    @Transient
    public Double calcularTotalConPromociones() {
        double totalDetalles = 0.0;
        double totalPromociones = 0.0; // Sumar detalles normales (usando subtotal histórico)
        if (detalles != null) {
            totalDetalles = detalles.stream()
                    .mapToDouble(detalle -> detalle.getSubtotal() != null ? detalle.getSubtotal() : 0.0)
                    .sum();
        }

        // Sumar promociones
        if (promociones != null) {
            totalPromociones = promociones.stream()
                    .mapToDouble(DetallePedidoPromocion::getPrecioTotalPromocion)
                    .sum();
        }

        return totalDetalles + totalPromociones;
    }

    /**
     * Calcula el ahorro total por promociones
     * 
     * @return el ahorro total
     */
    @Transient
    public Double calcularAhorroTotalPromociones() {
        if (promociones == null || promociones.isEmpty())
            return 0.0;
        return promociones.stream()
                .mapToDouble(DetallePedidoPromocion::getAhorroTotal)
                .sum();
    }
}
