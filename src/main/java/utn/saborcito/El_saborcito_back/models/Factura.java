package utn.saborcito.El_saborcito_back.models;
import jakarta.persistence.*;
import lombok.*;
import utn.saborcito.El_saborcito_back.enums.FormaPago;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaFacturacion;
    private Integer mpPaymentId;
    private Integer mpMerchantOrderId;
    private String mpPreferenceId;
    private String mpPaymentType;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    private Double totalVenta;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
