package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;

@Mapper(componentModel = "spring")
public interface ArticuloMapper {
    ArticuloMapper INSTANCE = Mappers.getMapper(ArticuloMapper.class);

    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "unidadMedida.id", target = "unidadMedidaId")
    ArticuloDTO toDTO(Articulo articulo);

    // Se comenta el método toEntity problemático por ahora.
    // Si se necesita, se deberá implementar una estrategia para manejar la clase
    // abstracta Articulo.
    /*
     * @Mapping(target = "categoria", ignore = true)
     * 
     * @Mapping(target = "unidadMedida", ignore = true)
     * Articulo toEntity(ArticuloDTO articuloDTO);
     */
}
