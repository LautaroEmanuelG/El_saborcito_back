package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Date fecha;
    private Double total; //Quiza no sea tan necesario revisar solicitudes a bd

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Caja admin;

    @OneToMany(mappedBy = "ticket",cascade = CascadeType.ALL)
    private List<TicketProducto> ticketProductos;
}
