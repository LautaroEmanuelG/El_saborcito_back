package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

}