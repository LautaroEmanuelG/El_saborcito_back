package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import utn.saborcito.El_saborcito_back.enums.Estado;
import utn.saborcito.El_saborcito_back.enums.FormaPago;
import utn.saborcito.El_saborcito_back.enums.TipoEnvio;

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

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

}
