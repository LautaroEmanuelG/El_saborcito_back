package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // Mantenido SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = "articuloManufacturadoDetalles")
@ToString(callSuper = true, exclude = "articuloManufacturadoDetalles")
public class ArticuloManufacturado extends Articulo {

    private String descripcion;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;

    @ManyToOne
    @JoinColumn(name = "imagen_id")
    private Imagen imagen;

    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ArticuloManufacturadoDetalle> articuloManufacturadoDetalles = new HashSet<>();

}
