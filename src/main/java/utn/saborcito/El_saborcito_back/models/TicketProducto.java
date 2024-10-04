package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TicketProducto {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "productoId")
    private Producto producto;
}