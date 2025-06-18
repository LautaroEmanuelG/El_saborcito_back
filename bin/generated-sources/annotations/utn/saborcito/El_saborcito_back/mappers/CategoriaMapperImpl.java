package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:29-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CategoriaMapperImpl implements CategoriaMapper {

    @Autowired
    private SucursalMapper sucursalMapper;

    @Override
    public CategoriaDTO toDTO(Categoria source) {
        if ( source == null ) {
            return null;
        }

        CategoriaDTO categoriaDTO = new CategoriaDTO();

        categoriaDTO.setTipoCategoria( toDTO( source.getTipoCategoria() ) );
        categoriaDTO.setSucursal( sucursalMapper.toDTO( source.getSucursal() ) );
        categoriaDTO.setDenominacion( source.getDenominacion() );
        categoriaDTO.setId( source.getId() );
        categoriaDTO.setTipo( source.getTipo() );

        return categoriaDTO;
    }

    @Override
    public Categoria toEntity(CategoriaDTO source) {
        if ( source == null ) {
            return null;
        }

        Categoria.CategoriaBuilder categoria = Categoria.builder();

        categoria.sucursal( sucursalMapper.toEntity( source.getSucursal() ) );
        categoria.denominacion( source.getDenominacion() );
        categoria.id( source.getId() );
        categoria.tipo( source.getTipo() );
        categoria.tipoCategoria( toEntity( source.getTipoCategoria() ) );

        return categoria.build();
    }
}
