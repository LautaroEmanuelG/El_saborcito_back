package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDetalleDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;

@Mapper(componentModel = "spring", uses = { ArticuloInsumoMapper.class })
public interface ArticuloManufacturadoDetalleMapper
        extends BaseMapper<ArticuloManufacturadoDetalle, ArticuloManufacturadoDetalleDTO> {

    @Override
    @Mapping(source = "articuloInsumo", target = "articuloInsumo")
    // @Mapping(source = "articuloManufacturado.id", target =
    // "articuloManufacturadoId") // Descomentar si se usa articuloManufacturadoId
    // en DTO
    ArticuloManufacturadoDetalleDTO toDTO(ArticuloManufacturadoDetalle entity);

    @Override
    @Mapping(target = "articuloInsumo", ignore = true) // Se manejará en la capa de servicio
    @Mapping(target = "articuloManufacturado", ignore = true) // Se manejará en la capa de servicio
    ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleDTO dto);
}
