package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.PaisDTO;
import utn.saborcito.El_saborcito_back.models.Pais;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PaisMapperImpl implements PaisMapper {

    @Override
    public PaisDTO toDTO(Pais pais) {
        if ( pais == null ) {
            return null;
        }

        PaisDTO paisDTO = new PaisDTO();

        paisDTO.setId( pais.getId() );
        paisDTO.setNombre( pais.getNombre() );

        return paisDTO;
    }

    @Override
    public Pais toEntity(PaisDTO paisDTO) {
        if ( paisDTO == null ) {
            return null;
        }

        Pais.PaisBuilder pais = Pais.builder();

        pais.id( paisDTO.getId() );
        pais.nombre( paisDTO.getNombre() );

        return pais.build();
    }
}
