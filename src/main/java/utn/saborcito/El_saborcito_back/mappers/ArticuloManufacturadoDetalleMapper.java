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
    ArticuloManufacturadoDetalleDTO toDTO(ArticuloManufacturadoDetalle source);

    @Override
    @Mapping(target = "articuloManufacturado", ignore = true) // Evitar recursividad
    ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleDTO source);
}
