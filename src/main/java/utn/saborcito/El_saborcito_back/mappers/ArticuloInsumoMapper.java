package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, CategoriaMapper.class, UnidadMedidaMapper.class })
public interface ArticuloInsumoMapper extends BaseMapper<ArticuloInsumo, ArticuloInsumoDTO> {

    @Override
    @Mapping(source = "imagen", target = "imagen") // mapear a imagen (ImagenDTO)
    @Mapping(source = "categoria.id", target = "categoriaId") // Para el campo heredado de ArticuloDTO
    @Mapping(source = "categoria", target = "categoria") // Mapea el objeto completo
    @Mapping(source = "unidadMedida", target = "unidadMedida") // Campo específico de ArticuloInsumoDTO
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "fechaEliminacion", target = "fechaEliminacion")
    ArticuloInsumoDTO toDTO(ArticuloInsumo source);

    @Override
    @Mapping(target = "id", ignore = true) // El ID se genera automáticamente
    @Mapping(target = "imagen", ignore = true) // Se manejará en el servicio
    @Mapping(target = "categoria", ignore = true) // Se manejará en el servicio
    @Mapping(target = "unidadMedida", ignore = true) // Se manejará en el servicio
    @Mapping(target = "eliminado", expression = "java(source.getEliminado() != null ? source.getEliminado() : false)")
    @Mapping(target = "fechaEliminacion", source = "fechaEliminacion")
    ArticuloInsumo toEntity(ArticuloInsumoDTO source);
}
