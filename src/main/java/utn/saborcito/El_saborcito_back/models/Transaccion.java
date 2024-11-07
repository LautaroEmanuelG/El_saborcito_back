package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.TransaccionTipo;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransaccionTipo tipo ;

    // Nuevo campo preferenciaId para vincular con MercadoPago
    private String preferenciaId;

    // Estado de la transacción ("PENDIENTE", "COMPLETADO", etc.)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    private Date fecha=new Date();;

    // Getters y Setters para el nuevo campo preferenciaId
    public String getPreferenciaId() {
        return preferenciaId;
    }

    public void setPreferenciaId(String preferenciaId) {
        this.preferenciaId = preferenciaId;
    }
}