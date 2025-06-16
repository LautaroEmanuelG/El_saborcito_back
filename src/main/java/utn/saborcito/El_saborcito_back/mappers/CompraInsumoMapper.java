package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.models.CompraInsumo;
import utn.saborcito.El_saborcito_back.models.CompraInsumoDetalle;
import utn.saborcito.El_saborcito_back.dto.CompraInsumoDTO;
import utn.saborcito.El_saborcito_back.dto.CompraInsumoDetalleDTO;

@Mapper(componentModel = "spring", uses = { SucursalMapper.class, ArticuloMapper.class })
public interface CompraInsumoMapper {
    CompraInsumoMapper INSTANCE = Mappers.getMapper(CompraInsumoMapper.class);

    @Mapping(source = "detalles", target = "detalles")
    @Mapping(source = "denominacion", target = "denominacion")
    CompraInsumoDTO toDTO(CompraInsumo compraInsumo);

    @Mapping(source = "detalles", target = "detalles")
    @Mapping(source = "denominacion", target = "denominacion")
    CompraInsumo toEntity(CompraInsumoDTO compraInsumoDTO);

    CompraInsumoDetalleDTO toDetalleDTO(CompraInsumoDetalle compraInsumoDetalle);

    CompraInsumoDetalle toDetalleEntity(CompraInsumoDetalleDTO compraInsumoDetalleDTO);
}