package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloManufacturado extends Articulo {

    private String descripcion;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;

    @ManyToOne
    @JoinColumn(name = "imagen_id")
    private Imagen imagen;
}
