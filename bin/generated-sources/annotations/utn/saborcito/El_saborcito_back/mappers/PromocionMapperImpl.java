package utn.saborcito.El_saborcito_back.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO;
import utn.saborcito.El_saborcito_back.dto.PromocionDetalleDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.models.PromocionDetalle;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:30-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PromocionMapperImpl implements PromocionMapper {

    @Autowired
    private ImagenMapper imagenMapper;
    @Autowired
    private SucursalMapper sucursalMapper;

    @Override
    public PromocionDTO toDTO(Promocion promocion) {
        if ( promocion == null ) {
            return null;
        }

        PromocionDTO promocionDTO = new PromocionDTO();

        promocionDTO.setPromocionDetalles( promocionDetalleListToPromocionDetalleDTOList( promocion.getPromocionDetalles() ) );
        promocionDTO.setEliminado( promocion.isEliminado() );
        promocionDTO.setDenominacion( promocion.getDenominacion() );
        promocionDTO.setDescuento( promocion.getDescuento() );
        promocionDTO.setFechaDesde( promocion.getFechaDesde() );
        promocionDTO.setFechaHasta( promocion.getFechaHasta() );
        promocionDTO.setHoraDesde( promocion.getHoraDesde() );
        promocionDTO.setHoraHasta( promocion.getHoraHasta() );
        promocionDTO.setId( promocion.getId() );
        promocionDTO.setImagen( imagenMapper.toDTO( promocion.getImagen() ) );
        promocionDTO.setPrecioPromocional( promocion.getPrecioPromocional() );
        promocionDTO.setSucursal( sucursalMapper.toDTO( promocion.getSucursal() ) );

        return promocionDTO;
    }

    @Override
    public Promocion toEntity(PromocionDTO promocionDTO) {
        if ( promocionDTO == null ) {
            return null;
        }

        Promocion.PromocionBuilder promocion = Promocion.builder();

        promocion.promocionDetalles( promocionDetalleDTOListToPromocionDetalleList( promocionDTO.getPromocionDetalles() ) );
        if ( promocionDTO.getEliminado() != null ) {
            promocion.eliminado( promocionDTO.getEliminado() );
        }
        promocion.denominacion( promocionDTO.getDenominacion() );
        promocion.descuento( promocionDTO.getDescuento() );
        promocion.fechaDesde( promocionDTO.getFechaDesde() );
        promocion.fechaHasta( promocionDTO.getFechaHasta() );
        promocion.horaDesde( promocionDTO.getHoraDesde() );
        promocion.horaHasta( promocionDTO.getHoraHasta() );
        promocion.id( promocionDTO.getId() );
        promocion.imagen( imagenMapper.toEntity( promocionDTO.getImagen() ) );
        promocion.precioPromocional( promocionDTO.getPrecioPromocional() );

        return promocion.build();
    }

    @Override
    public PromocionDetalleDTO toDetalleDTO(PromocionDetalle promocionDetalle) {
        if ( promocionDetalle == null ) {
            return null;
        }

        PromocionDetalleDTO promocionDetalleDTO = new PromocionDetalleDTO();

        promocionDetalleDTO.setArticulo( articuloToArticuloDTO( promocionDetalle.getArticulo() ) );
        promocionDetalleDTO.setPromocionId( promocionDetallePromocionId( promocionDetalle ) );
        promocionDetalleDTO.setCantidadRequerida( promocionDetalle.getCantidadRequerida() );
        promocionDetalleDTO.setId( promocionDetalle.getId() );

        return promocionDetalleDTO;
    }

    @Override
    public PromocionDetalle toDetalleEntity(PromocionDetalleDTO promocionDetalleDTO) {
        if ( promocionDetalleDTO == null ) {
            return null;
        }

        PromocionDetalle.PromocionDetalleBuilder promocionDetalle = PromocionDetalle.builder();

        promocionDetalle.cantidadRequerida( promocionDetalleDTO.getCantidadRequerida() );
        promocionDetalle.id( promocionDetalleDTO.getId() );

        return promocionDetalle.build();
    }

    protected List<PromocionDetalleDTO> promocionDetalleListToPromocionDetalleDTOList(List<PromocionDetalle> list) {
        if ( list == null ) {
            return null;
        }

        List<PromocionDetalleDTO> list1 = new ArrayList<PromocionDetalleDTO>( list.size() );
        for ( PromocionDetalle promocionDetalle : list ) {
            list1.add( toDetalleDTO( promocionDetalle ) );
        }

        return list1;
    }

    protected List<PromocionDetalle> promocionDetalleDTOListToPromocionDetalleList(List<PromocionDetalleDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<PromocionDetalle> list1 = new ArrayList<PromocionDetalle>( list.size() );
        for ( PromocionDetalleDTO promocionDetalleDTO : list ) {
            list1.add( toDetalleEntity( promocionDetalleDTO ) );
        }

        return list1;
    }

    private Long articuloCategoriaId(Articulo articulo) {
        if ( articulo == null ) {
            return null;
        }
        Categoria categoria = articulo.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        Long id = categoria.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected ArticuloDTO articuloToArticuloDTO(Articulo articulo) {
        if ( articulo == null ) {
            return null;
        }

        ArticuloDTO articuloDTO = new ArticuloDTO();

        articuloDTO.setCategoriaId( articuloCategoriaId( articulo ) );
        articuloDTO.setDenominacion( articulo.getDenominacion() );
        articuloDTO.setEliminado( articulo.getEliminado() );
        articuloDTO.setFechaEliminacion( articulo.getFechaEliminacion() );
        articuloDTO.setId( articulo.getId() );
        articuloDTO.setImagen( imagenMapper.toDTO( articulo.getImagen() ) );
        articuloDTO.setPrecioVenta( articulo.getPrecioVenta() );

        return articuloDTO;
    }

    private Long promocionDetallePromocionId(PromocionDetalle promocionDetalle) {
        if ( promocionDetalle == null ) {
            return null;
        }
        Promocion promocion = promocionDetalle.getPromocion();
        if ( promocion == null ) {
            return null;
        }
        Long id = promocion.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
