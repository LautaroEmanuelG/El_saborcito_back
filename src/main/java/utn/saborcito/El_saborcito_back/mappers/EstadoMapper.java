package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.EstadoDTO;
import utn.saborcito.El_saborcito_back.models.Estado;

@Mapper(componentModel = "spring")
public interface EstadoMapper {
    EstadoMapper INSTANCE = Mappers.getMapper(EstadoMapper.class);

    EstadoDTO toDTO(Estado estado);

    Estado toEntity(EstadoDTO estadoDTO);
}
