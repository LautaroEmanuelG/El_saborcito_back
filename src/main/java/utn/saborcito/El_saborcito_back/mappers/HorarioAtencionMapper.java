package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.HorarioAtencionDTO;
import utn.saborcito.El_saborcito_back.models.HorarioAtencion;

@Mapper(componentModel = "spring")
public interface HorarioAtencionMapper {
    HorarioAtencionMapper INSTANCE = Mappers.getMapper(HorarioAtencionMapper.class);

    HorarioAtencionDTO toDTO(HorarioAtencion horarioAtencion);

    HorarioAtencion toEntity(HorarioAtencionDTO horarioAtencionDTO);
}
