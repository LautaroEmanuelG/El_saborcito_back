package utn.saborcito.El_saborcito_back.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Empleado extends Usuario {

    private String legajo;
    private LocalDate fechaIngreso;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    // Campos específicos del empleado
    private Boolean primerLogin; // Para controlar cambio de contraseña obligatorio HU4
}

