package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, CategoriaMapper.class, UnidadMedidaMapper.class })
public interface ArticuloInsumoMapper extends BaseMapper<ArticuloInsumo, ArticuloInsumoDTO> {
    @Override
    @Mapping(source = "imagen", target = "imagen") // Corregido: mapear a imagen (ImagenDTO)
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "unidadMedida", target = "unidadMedida")
    ArticuloInsumoDTO toDTO(ArticuloInsumo source);

    @Override
    @Mapping(target = "id", ignore = true) // El ID se genera automáticamente
    @Mapping(target = "imagen", ignore = true) // Se manejará en el servicio
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "unidadMedida", target = "unidadMedida")
    ArticuloInsumo toEntity(ArticuloInsumoDTO source);
}
