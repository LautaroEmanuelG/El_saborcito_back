package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "domicilio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Domicilio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String calle;
    private Integer numero;
    private String cp;


    // Muchos domicilios para un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Muchos domicilios para una localidad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;
}