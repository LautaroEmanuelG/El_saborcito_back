package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // Añadido SuperBuilder
public abstract class Articulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denominacion;
    private Double precioVenta; // Corregido de Integer a Double

    @ManyToOne
    @JoinColumn(name = "imagen_id") // Añadido para la imagen
    private Imagen imagen; // Añadido para la imagen

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "unidad_medida_id")
    private UnidadMedida unidadMedida;

    // Campos para delete lógico
    @Column(name = "eliminado", nullable = false)
    @Builder.Default
    private Boolean eliminado = false;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;
}
