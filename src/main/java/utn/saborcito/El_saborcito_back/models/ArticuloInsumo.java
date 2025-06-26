package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("INSUMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArticuloInsumo extends Articulo {

    private Double precioCompra;
    private Double stockActual;    // ✅ Cambio de Integer a Double
    private Double stockMinimo;    // ✅ Cambio de Integer a Double
    private Boolean esParaElaborar;
}
