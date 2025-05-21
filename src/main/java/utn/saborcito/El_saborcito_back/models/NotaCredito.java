package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime; // Asegurar que este import esté presente

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_NotaCredito;
    private Double monto;
    private LocalDateTime fechaEmision; // Tipo cambiado a LocalDateTime
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "factura_id") // CORRECCIÓN 13: Asociada a Factura, que a su vez está asociada a Pedido
    private Factura factura;
}
