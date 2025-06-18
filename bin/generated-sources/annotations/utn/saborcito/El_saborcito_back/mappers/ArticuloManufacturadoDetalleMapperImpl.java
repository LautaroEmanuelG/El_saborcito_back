package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDetalleDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:34-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ArticuloManufacturadoDetalleMapperImpl implements ArticuloManufacturadoDetalleMapper {

    @Autowired
    private ArticuloInsumoMapper articuloInsumoMapper;

    @Override
    public ArticuloManufacturadoDetalleDTO toDTO(ArticuloManufacturadoDetalle entity) {
        if ( entity == null ) {
            return null;
        }

        ArticuloManufacturadoDetalleDTO.ArticuloManufacturadoDetalleDTOBuilder articuloManufacturadoDetalleDTO = ArticuloManufacturadoDetalleDTO.builder();

        articuloManufacturadoDetalleDTO.articuloInsumo( articuloInsumoMapper.toDTO( entity.getArticuloInsumo() ) );
        articuloManufacturadoDetalleDTO.cantidad( entity.getCantidad() );
        articuloManufacturadoDetalleDTO.id( entity.getId() );

        return articuloManufacturadoDetalleDTO.build();
    }

    @Override
    public ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ArticuloManufacturadoDetalle.ArticuloManufacturadoDetalleBuilder articuloManufacturadoDetalle = ArticuloManufacturadoDetalle.builder();

        articuloManufacturadoDetalle.cantidad( dto.getCantidad() );
        articuloManufacturadoDetalle.id( dto.getId() );

        return articuloManufacturadoDetalle.build();
    }
}
