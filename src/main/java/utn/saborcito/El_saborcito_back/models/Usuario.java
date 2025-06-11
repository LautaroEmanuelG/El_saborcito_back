package utn.saborcito.El_saborcito_back.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import utn.saborcito.El_saborcito_back.enums.Rol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED) // O TABLE_PER_CLASS
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(of = "id") // Solo usar ID para equals/hashCode
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
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Domicilio> domicilios = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id")
    private Imagen imagen;

}
