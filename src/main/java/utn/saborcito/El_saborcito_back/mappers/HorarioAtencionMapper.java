package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.HorarioAtencionDTO;
import utn.saborcito.El_saborcito_back.models.HorarioAtencion;

@Mapper(componentModel = "spring")
public interface HorarioAtencionMapper {
    HorarioAtencionMapper INSTANCE = Mappers.getMapper(HorarioAtencionMapper.class);

    HorarioAtencionDTO toDTO(HorarioAtencion horarioAtencion);

    @Mapping(target = "sucursal", ignore = true) // Ignorar sucursal en la conversión a entidad
    HorarioAtencion toEntity(HorarioAtencionDTO horarioAtencionDTO);
}
