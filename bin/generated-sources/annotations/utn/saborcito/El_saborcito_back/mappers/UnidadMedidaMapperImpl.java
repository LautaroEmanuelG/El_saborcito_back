package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.UnidadMedidaDTO;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UnidadMedidaMapperImpl implements UnidadMedidaMapper {

    @Override
    public UnidadMedidaDTO toDTO(UnidadMedida unidadMedida) {
        if ( unidadMedida == null ) {
            return null;
        }

        UnidadMedidaDTO unidadMedidaDTO = new UnidadMedidaDTO();

        unidadMedidaDTO.setDenominacion( unidadMedida.getDenominacion() );
        unidadMedidaDTO.setId( unidadMedida.getId() );

        return unidadMedidaDTO;
    }

    @Override
    public UnidadMedida toEntity(UnidadMedidaDTO unidadMedidaDTO) {
        if ( unidadMedidaDTO == null ) {
            return null;
        }

        UnidadMedida.UnidadMedidaBuilder unidadMedida = UnidadMedida.builder();

        unidadMedida.denominacion( unidadMedidaDTO.getDenominacion() );
        unidadMedida.id( unidadMedidaDTO.getId() );

        return unidadMedida.build();
    }
}
