package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import utn.saborcito.El_saborcito_back.dto.UnidadMedidaDTO;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;

@Mapper(componentModel = "spring")
public interface UnidadMedidaMapper extends BaseMapper<UnidadMedida, UnidadMedidaDTO> {
    // Las implementaciones de toDTO y toEntity son generadas automáticamente
    // si los nombres de los campos coinciden y no hay lógica compleja.
}
