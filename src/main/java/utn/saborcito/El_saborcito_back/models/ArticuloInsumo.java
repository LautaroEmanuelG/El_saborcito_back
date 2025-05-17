package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArticuloInsumo extends Articulo {

    private Double precioCompra;
    private Integer stockActual;
    private Integer stockMaximo;
    private Boolean esParaElaborar;

    @ManyToOne
    @JoinColumn(name = "imagen_id") // La imagen es obligatoria según ArticuloService
    private Imagen imagen;
}
