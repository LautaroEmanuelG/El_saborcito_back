package utn.saborcito.El_saborcito_back.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosMercadoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateCreate;
    private LocalDate dateApproved;
    private LocalDate dateLastUpdate;
    private String paymentType;
    private String paymentMethod;
    private String status;
    private String statusDetail;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;
}
