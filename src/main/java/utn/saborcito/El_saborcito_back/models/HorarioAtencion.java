package utn.saborcito.El_saborcito_back.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
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
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    private LocalTime apertura;
    private LocalTime cierre;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    @ToString.Exclude
    @JsonBackReference
    private Sucursal sucursal;
}
