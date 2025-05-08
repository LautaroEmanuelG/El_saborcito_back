package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import utn.saborcito.El_saborcito_back.enums.Rol;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String auth0Id;
    private String username;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Boolean estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaModificacion;
}
