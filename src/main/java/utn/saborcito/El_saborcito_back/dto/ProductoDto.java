package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private CategoriaDTO categoria;
    private ValorDto valor;
}
