package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provincia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // Muchos a uno: varias provincias en un país
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;
}