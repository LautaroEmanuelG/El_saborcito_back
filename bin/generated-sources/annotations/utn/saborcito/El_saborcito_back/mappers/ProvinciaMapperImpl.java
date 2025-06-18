package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ProvinciaDTO;
import utn.saborcito.El_saborcito_back.models.Provincia;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ProvinciaMapperImpl implements ProvinciaMapper {

    @Autowired
    private PaisMapper paisMapper;

    @Override
    public ProvinciaDTO toDTO(Provincia provincia) {
        if ( provincia == null ) {
            return null;
        }

        ProvinciaDTO provinciaDTO = new ProvinciaDTO();

        provinciaDTO.setId( provincia.getId() );
        provinciaDTO.setNombre( provincia.getNombre() );
        provinciaDTO.setPais( paisMapper.toDTO( provincia.getPais() ) );

        return provinciaDTO;
    }

    @Override
    public Provincia toEntity(ProvinciaDTO provinciaDTO) {
        if ( provinciaDTO == null ) {
            return null;
        }

        Provincia.ProvinciaBuilder provincia = Provincia.builder();

        provincia.id( provinciaDTO.getId() );
        provincia.nombre( provinciaDTO.getNombre() );
        provincia.pais( paisMapper.toEntity( provinciaDTO.getPais() ) );

        return provincia.build();
    }
}
