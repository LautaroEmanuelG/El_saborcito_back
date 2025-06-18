package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.TipoEnvioDTO;
import utn.saborcito.El_saborcito_back.models.TipoEnvio;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:34-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class TipoEnvioMapperImpl implements TipoEnvioMapper {

    @Override
    public TipoEnvioDTO toDTO(TipoEnvio tipoEnvio) {
        if ( tipoEnvio == null ) {
            return null;
        }

        TipoEnvioDTO tipoEnvioDTO = new TipoEnvioDTO();

        tipoEnvioDTO.setId( tipoEnvio.getId() );
        tipoEnvioDTO.setNombre( tipoEnvio.getNombre() );

        return tipoEnvioDTO;
    }

    @Override
    public TipoEnvio toEntity(TipoEnvioDTO tipoEnvioDTO) {
        if ( tipoEnvioDTO == null ) {
            return null;
        }

        TipoEnvio.TipoEnvioBuilder tipoEnvio = TipoEnvio.builder();

        tipoEnvio.id( tipoEnvioDTO.getId() );
        tipoEnvio.nombre( tipoEnvioDTO.getNombre() );

        return tipoEnvio.build();
    }
}
