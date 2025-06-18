package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.HorarioAtencionDTO;
import utn.saborcito.El_saborcito_back.enums.DiaSemana;
import utn.saborcito.El_saborcito_back.models.HorarioAtencion;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class HorarioAtencionMapperImpl implements HorarioAtencionMapper {

    @Override
    public HorarioAtencionDTO toDTO(HorarioAtencion horarioAtencion) {
        if ( horarioAtencion == null ) {
            return null;
        }

        HorarioAtencionDTO horarioAtencionDTO = new HorarioAtencionDTO();

        horarioAtencionDTO.setApertura( horarioAtencion.getApertura() );
        horarioAtencionDTO.setCierre( horarioAtencion.getCierre() );
        if ( horarioAtencion.getDiaSemana() != null ) {
            horarioAtencionDTO.setDiaSemana( horarioAtencion.getDiaSemana().name() );
        }
        horarioAtencionDTO.setId( horarioAtencion.getId() );

        return horarioAtencionDTO;
    }

    @Override
    public HorarioAtencion toEntity(HorarioAtencionDTO horarioAtencionDTO) {
        if ( horarioAtencionDTO == null ) {
            return null;
        }

        HorarioAtencion.HorarioAtencionBuilder horarioAtencion = HorarioAtencion.builder();

        horarioAtencion.apertura( horarioAtencionDTO.getApertura() );
        horarioAtencion.cierre( horarioAtencionDTO.getCierre() );
        if ( horarioAtencionDTO.getDiaSemana() != null ) {
            horarioAtencion.diaSemana( Enum.valueOf( DiaSemana.class, horarioAtencionDTO.getDiaSemana() ) );
        }
        horarioAtencion.id( horarioAtencionDTO.getId() );

        return horarioAtencion.build();
    }
}
