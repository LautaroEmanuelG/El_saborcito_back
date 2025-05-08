package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloManufacturado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denominacion;
    private String descripcion;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;
    private Double precioVenta;

    @ManyToOne
    @JoinColumn(name = "imagen_id")
    private Imagen imagen;
}
