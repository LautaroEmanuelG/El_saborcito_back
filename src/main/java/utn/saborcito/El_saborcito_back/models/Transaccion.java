package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.transaccion;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaccion {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private transaccion tipo;
    private Double monto;
    private String descripcion;
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "libroContableId")
    private LibroContable libroContable;
}
