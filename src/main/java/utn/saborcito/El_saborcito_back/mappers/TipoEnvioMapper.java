package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.TipoEnvioDTO;
import utn.saborcito.El_saborcito_back.models.TipoEnvio;

@Mapper(componentModel = "spring")
public interface TipoEnvioMapper {
    TipoEnvioMapper INSTANCE = Mappers.getMapper(TipoEnvioMapper.class);

    TipoEnvioDTO toDTO(TipoEnvio tipoEnvio);

    TipoEnvio toEntity(TipoEnvioDTO tipoEnvioDTO);
}
