package utn.saborcito.El_saborcito_back.mappers;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDetalleDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ArticuloManufacturadoMapperImpl implements ArticuloManufacturadoMapper {

    @Autowired
    private ImagenMapper imagenMapper;
    @Autowired
    private ArticuloManufacturadoDetalleMapper articuloManufacturadoDetalleMapper;

    @Override
    public ArticuloManufacturadoDTO toDTO(ArticuloManufacturado source) {
        if ( source == null ) {
            return null;
        }

        ArticuloManufacturadoDTO articuloManufacturadoDTO = new ArticuloManufacturadoDTO();

        articuloManufacturadoDTO.setImagen( imagenMapper.toDTO( source.getImagen() ) );
        articuloManufacturadoDTO.setCategoriaId( sourceCategoriaId( source ) );
        articuloManufacturadoDTO.setArticuloManufacturadoDetalles( articuloManufacturadoDetalleSetToArticuloManufacturadoDetalleDTOSet( source.getArticuloManufacturadoDetalles() ) );
        articuloManufacturadoDTO.setEliminado( source.getEliminado() );
        articuloManufacturadoDTO.setFechaEliminacion( source.getFechaEliminacion() );
        articuloManufacturadoDTO.setDenominacion( source.getDenominacion() );
        articuloManufacturadoDTO.setId( source.getId() );
        articuloManufacturadoDTO.setPrecioVenta( source.getPrecioVenta() );
        articuloManufacturadoDTO.setDescripcion( source.getDescripcion() );
        articuloManufacturadoDTO.setPreparacion( source.getPreparacion() );
        articuloManufacturadoDTO.setTiempoEstimadoMinutos( source.getTiempoEstimadoMinutos() );

        return articuloManufacturadoDTO;
    }

    @Override
    public ArticuloManufacturado toEntity(ArticuloManufacturadoDTO source) {
        if ( source == null ) {
            return null;
        }

        ArticuloManufacturado.ArticuloManufacturadoBuilder<?, ?> articuloManufacturado = ArticuloManufacturado.builder();

        articuloManufacturado.fechaEliminacion( source.getFechaEliminacion() );
        articuloManufacturado.denominacion( source.getDenominacion() );
        articuloManufacturado.id( source.getId() );
        articuloManufacturado.precioVenta( source.getPrecioVenta() );
        articuloManufacturado.articuloManufacturadoDetalles( articuloManufacturadoDetalleDTOSetToArticuloManufacturadoDetalleSet( source.getArticuloManufacturadoDetalles() ) );
        articuloManufacturado.descripcion( source.getDescripcion() );
        articuloManufacturado.preparacion( source.getPreparacion() );
        articuloManufacturado.tiempoEstimadoMinutos( source.getTiempoEstimadoMinutos() );

        articuloManufacturado.eliminado( source.getEliminado() != null ? source.getEliminado() : false );

        return articuloManufacturado.build();
    }

    private Long sourceCategoriaId(ArticuloManufacturado articuloManufacturado) {
        if ( articuloManufacturado == null ) {
            return null;
        }
        Categoria categoria = articuloManufacturado.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        Long id = categoria.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<ArticuloManufacturadoDetalleDTO> articuloManufacturadoDetalleSetToArticuloManufacturadoDetalleDTOSet(Set<ArticuloManufacturadoDetalle> set) {
        if ( set == null ) {
            return null;
        }

        Set<ArticuloManufacturadoDetalleDTO> set1 = new LinkedHashSet<ArticuloManufacturadoDetalleDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ArticuloManufacturadoDetalle articuloManufacturadoDetalle : set ) {
            set1.add( articuloManufacturadoDetalleMapper.toDTO( articuloManufacturadoDetalle ) );
        }

        return set1;
    }

    protected Set<ArticuloManufacturadoDetalle> articuloManufacturadoDetalleDTOSetToArticuloManufacturadoDetalleSet(Set<ArticuloManufacturadoDetalleDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<ArticuloManufacturadoDetalle> set1 = new LinkedHashSet<ArticuloManufacturadoDetalle>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ArticuloManufacturadoDetalleDTO articuloManufacturadoDetalleDTO : set ) {
            set1.add( articuloManufacturadoDetalleMapper.toEntity( articuloManufacturadoDetalleDTO ) );
        }

        return set1;
    }
}
