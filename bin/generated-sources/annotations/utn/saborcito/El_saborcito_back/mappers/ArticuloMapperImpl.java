package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ArticuloMapperImpl implements ArticuloMapper {

    @Autowired
    private ImagenMapper imagenMapper;

    @Override
    public ArticuloDTO toDTO(Articulo articulo) {
        if ( articulo == null ) {
            return null;
        }

        ArticuloDTO articuloDTO = new ArticuloDTO();

        articuloDTO.setCategoriaId( articuloCategoriaId( articulo ) );
        articuloDTO.setImagen( imagenMapper.toDTO( articulo.getImagen() ) );
        articuloDTO.setEliminado( articulo.getEliminado() );
        articuloDTO.setFechaEliminacion( articulo.getFechaEliminacion() );
        articuloDTO.setDenominacion( articulo.getDenominacion() );
        articuloDTO.setId( articulo.getId() );
        articuloDTO.setPrecioVenta( articulo.getPrecioVenta() );

        return articuloDTO;
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
}
