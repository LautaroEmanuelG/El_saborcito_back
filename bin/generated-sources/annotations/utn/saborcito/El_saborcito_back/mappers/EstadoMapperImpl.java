package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.EstadoDTO;
import utn.saborcito.El_saborcito_back.models.Estado;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EstadoMapperImpl implements EstadoMapper {

    @Override
    public EstadoDTO toDTO(Estado estado) {
        if ( estado == null ) {
            return null;
        }

        EstadoDTO estadoDTO = new EstadoDTO();

        estadoDTO.setId( estado.getId() );
        estadoDTO.setNombre( estado.getNombre() );

        return estadoDTO;
    }

    @Override
    public Estado toEntity(EstadoDTO estadoDTO) {
        if ( estadoDTO == null ) {
            return null;
        }

        Estado.EstadoBuilder estado = Estado.builder();

        estado.id( estadoDTO.getId() );
        estado.nombre( estadoDTO.getNombre() );

        return estado.build();
    }
}
