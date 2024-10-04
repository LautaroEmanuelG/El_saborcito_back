package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private Categoria categoria;
    private ValorDto valor;
}
