// src/main/java/utn/saborcito/El_saborcito_back/models/CompraDetalle.java
package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CompraDetalle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "compra_id")
    private CompraInsumo compra;

    @ManyToOne @JoinColumn(name = "insumo_id")
    private ArticuloInsumo insumo;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;   // = cantidad * precioUnitario
}
