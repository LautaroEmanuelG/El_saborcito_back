package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.LocalidadDTO;
import utn.saborcito.El_saborcito_back.models.Localidad;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class LocalidadMapperImpl implements LocalidadMapper {

    @Autowired
    private ProvinciaMapper provinciaMapper;

    @Override
    public LocalidadDTO toDTO(Localidad localidad) {
        if ( localidad == null ) {
            return null;
        }

        LocalidadDTO localidadDTO = new LocalidadDTO();

        localidadDTO.setId( localidad.getId() );
        localidadDTO.setNombre( localidad.getNombre() );
        localidadDTO.setProvincia( provinciaMapper.toDTO( localidad.getProvincia() ) );

        return localidadDTO;
    }

    @Override
    public Localidad toEntity(LocalidadDTO localidadDTO) {
        if ( localidadDTO == null ) {
            return null;
        }

        Localidad.LocalidadBuilder localidad = Localidad.builder();

        localidad.id( localidadDTO.getId() );
        localidad.nombre( localidadDTO.getNombre() );
        localidad.provincia( provinciaMapper.toEntity( localidadDTO.getProvincia() ) );

        return localidad.build();
    }
}
