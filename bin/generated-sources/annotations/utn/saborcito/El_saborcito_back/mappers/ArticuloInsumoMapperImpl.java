package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ArticuloInsumoMapperImpl implements ArticuloInsumoMapper {

    @Autowired
    private ImagenMapper imagenMapper;
    @Autowired
    private CategoriaMapper categoriaMapper;
    @Autowired
    private UnidadMedidaMapper unidadMedidaMapper;

    @Override
    public ArticuloInsumoDTO toDTO(ArticuloInsumo source) {
        if ( source == null ) {
            return null;
        }

        ArticuloInsumoDTO articuloInsumoDTO = new ArticuloInsumoDTO();

        articuloInsumoDTO.setImagen( imagenMapper.toDTO( source.getImagen() ) );
        articuloInsumoDTO.setCategoriaId( sourceCategoriaId( source ) );
        articuloInsumoDTO.setCategoria( categoriaMapper.toDTO( source.getCategoria() ) );
        articuloInsumoDTO.setUnidadMedida( unidadMedidaMapper.toDTO( source.getUnidadMedida() ) );
        articuloInsumoDTO.setEliminado( source.getEliminado() );
        articuloInsumoDTO.setFechaEliminacion( source.getFechaEliminacion() );
        articuloInsumoDTO.setDenominacion( source.getDenominacion() );
        articuloInsumoDTO.setId( source.getId() );
        articuloInsumoDTO.setPrecioVenta( source.getPrecioVenta() );
        articuloInsumoDTO.setEsParaElaborar( source.getEsParaElaborar() );
        articuloInsumoDTO.setPrecioCompra( source.getPrecioCompra() );
        articuloInsumoDTO.setStockActual( source.getStockActual() );
        articuloInsumoDTO.setStockMinimo( source.getStockMinimo() );

        return articuloInsumoDTO;
    }

    @Override
    public ArticuloInsumo toEntity(ArticuloInsumoDTO source) {
        if ( source == null ) {
            return null;
        }

        ArticuloInsumo.ArticuloInsumoBuilder<?, ?> articuloInsumo = ArticuloInsumo.builder();

        articuloInsumo.fechaEliminacion( source.getFechaEliminacion() );
        articuloInsumo.denominacion( source.getDenominacion() );
        articuloInsumo.precioVenta( source.getPrecioVenta() );
        articuloInsumo.esParaElaborar( source.getEsParaElaborar() );
        articuloInsumo.precioCompra( source.getPrecioCompra() );
        articuloInsumo.stockActual( source.getStockActual() );
        articuloInsumo.stockMinimo( source.getStockMinimo() );

        articuloInsumo.eliminado( source.getEliminado() != null ? source.getEliminado() : false );

        return articuloInsumo.build();
    }

    private Long sourceCategoriaId(ArticuloInsumo articuloInsumo) {
        if ( articuloInsumo == null ) {
            return null;
        }
        Categoria categoria = articuloInsumo.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        Long id = categoria.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
