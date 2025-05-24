package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import utn.saborcito.El_saborcito_back.dto.UnidadMedidaDTO;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;

@Mapper(componentModel = "spring")
public interface UnidadMedidaMapper extends BaseMapper<UnidadMedida, UnidadMedidaDTO> {
    UnidadMedidaDTO toDTO(UnidadMedida unidadMedida);

    UnidadMedida toEntity(UnidadMedidaDTO unidadMedidaDTO);
}
