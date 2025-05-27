package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, CategoriaMapper.class,
        ArticuloManufacturadoDetalleMapper.class })
public interface ArticuloManufacturadoMapper extends BaseMapper<ArticuloManufacturado, ArticuloManufacturadoDTO> {

    @Override
    @Mapping(source = "imagen", target = "imagen") // Corregido: mapear a imagen (ImagenDTO)
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "unidadMedida.id", target = "unidadMedidaId")
    @Mapping(source = "articuloManufacturadoDetalles", target = "articuloManufacturadoDetalles")
    ArticuloManufacturadoDTO toDTO(ArticuloManufacturado source);

    @Override
    @Mapping(target = "imagen", ignore = true) // Se manejará en el servicio
    @Mapping(target = "categoria", ignore = true) // Ignorar para manejo manual/en servicio
    @Mapping(target = "unidadMedida", ignore = true) // Ignorar para manejo manual/en servicio
    ArticuloManufacturado toEntity(ArticuloManufacturadoDTO source);
}
