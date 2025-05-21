package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.saborcito.El_saborcito_back.enums.DiaSemana;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioAtencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_HorarioAtencion;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    private LocalTime apertura;
    private LocalTime cierre;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;
}
