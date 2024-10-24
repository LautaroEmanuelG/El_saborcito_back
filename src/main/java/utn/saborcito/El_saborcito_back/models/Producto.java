package utn.saborcito.El_saborcito_back.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private Date fechaCreacion;
    private Boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name="valorId")
    private Valor valor;

    @ManyToOne
    @JoinColumn(name="categoriaId")
    private Categoria categoria;

}
