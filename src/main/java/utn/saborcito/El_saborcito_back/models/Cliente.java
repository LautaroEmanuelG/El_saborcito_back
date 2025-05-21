package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String historialPedidos; // Representa el JSON como texto

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
