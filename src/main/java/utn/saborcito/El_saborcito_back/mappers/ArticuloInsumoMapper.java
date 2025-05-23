package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, CategoriaMapper.class, UnidadMedidaMapper.class })
public interface ArticuloInsumoMapper {
    @Mapping(source = "imagen", target = "imagen")
    @Mapping(source = "categoria", target = "categoria")
    // @Mapping(source = "unidadMedida", target = "unidadMedida") // Descomentar si
    // se crea UnidadMedidaMapper
    ArticuloInsumoDTO toDTO(ArticuloInsumo source);

    @Mapping(target = "precioCompra", ignore = true)
    @Mapping(target = "stockActual", ignore = true)
    @Mapping(target = "stockMaximo", ignore = true)
    // @Mapping(target = "stockMinimo", ignore = true) // Property does not exist in ArticuloInsumo
    @Mapping(target = "esParaElaborar", ignore = true)
    @Mapping(target = "unidadMedida", ignore = true) // Ignorar por ahora si no se mapea
    // @Mapping(target = "articuloManufacturadoDetalles", ignore = true) // Property does not exist in ArticuloInsumo
    // @Mapping(target = "promocionDetalles", ignore = true) // Property does not exist in ArticuloInsumo
    ArticuloInsumo toEntity(ArticuloInsumoDTO source);
}
