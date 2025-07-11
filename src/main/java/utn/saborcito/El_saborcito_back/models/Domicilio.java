package utn.saborcito.El_saborcito_back.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "domicilio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Domicilio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String calle;
    private Integer numero;
    private String cp;
    private Double latitud;
    private Double longitud;
    // Muchos domicilios para un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("domicilios")
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    @JsonBackReference
    private Usuario usuario;
    // Muchos domicilios para una localidad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;
}