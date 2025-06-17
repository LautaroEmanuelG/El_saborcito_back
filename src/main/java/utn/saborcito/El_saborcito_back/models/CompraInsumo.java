package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CompraInsumo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denominacion;       // Opcional: “Compra semanal X”
    private LocalDate fechaCompra;

    private Double totalCompra;        // Suma de subtotales

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraDetalle> detalles;
}
