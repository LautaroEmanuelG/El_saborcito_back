package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.TransaccionTipo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaccion {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransaccionTipo tipo;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    private Ticket ticket;
}
