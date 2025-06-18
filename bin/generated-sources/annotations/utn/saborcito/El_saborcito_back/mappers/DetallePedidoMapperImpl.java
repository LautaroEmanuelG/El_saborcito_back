package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.DetallePedido;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:28-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class DetallePedidoMapperImpl implements DetallePedidoMapper {

    @Autowired
    private ImagenMapper imagenMapper;

    @Override
    public DetallePedidoDTO toDTO(DetallePedido entity) {
        if ( entity == null ) {
            return null;
        }

        DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();

        detallePedidoDTO.setArticulo( articuloToArticuloDTO( entity.getArticulo() ) );
        detallePedidoDTO.setCantidad( entity.getCantidad() );
        detallePedidoDTO.setCantidadConPromocion( entity.getCantidadConPromocion() );
        detallePedidoDTO.setCantidadSinPromocion( entity.getCantidadSinPromocion() );
        detallePedidoDTO.setId( entity.getId() );
        detallePedidoDTO.setOrigen( entity.getOrigen() );
        detallePedidoDTO.setPromocionOrigenId( entity.getPromocionOrigenId() );
        detallePedidoDTO.setSubtotal( entity.getSubtotal() );

        return detallePedidoDTO;
    }

    @Override
    public DetallePedido toEntity(DetallePedidoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DetallePedido.DetallePedidoBuilder detallePedido = DetallePedido.builder();

        detallePedido.cantidad( dto.getCantidad() );
        detallePedido.id( dto.getId() );
        detallePedido.origen( dto.getOrigen() );
        detallePedido.promocionOrigenId( dto.getPromocionOrigenId() );
        detallePedido.subtotal( dto.getSubtotal() );

        return detallePedido.build();
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

        articuloDTO.setId( articulo.getId() );
        articuloDTO.setCategoriaId( articuloCategoriaId( articulo ) );
        articuloDTO.setImagen( imagenMapper.toDTO( articulo.getImagen() ) );
        articuloDTO.setDenominacion( articulo.getDenominacion() );
        articuloDTO.setEliminado( articulo.getEliminado() );
        articuloDTO.setFechaEliminacion( articulo.getFechaEliminacion() );
        articuloDTO.setPrecioVenta( articulo.getPrecioVenta() );

        return articuloDTO;
    }
}
