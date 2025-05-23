package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEnvio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Constructor conveniente para inicializar enums
    public TipoEnvio(String nombre) {
        this.nombre = nombre;
    }
}
