package utn.saborcito.El_saborcito_back.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEmision;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;
}
