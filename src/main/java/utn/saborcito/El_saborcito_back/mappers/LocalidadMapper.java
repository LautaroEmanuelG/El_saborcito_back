package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.LocalidadDTO;
import utn.saborcito.El_saborcito_back.models.Localidad;

@Mapper(componentModel = "spring", uses = ProvinciaMapper.class)
public interface LocalidadMapper {
    LocalidadMapper INSTANCE = Mappers.getMapper(LocalidadMapper.class);

    LocalidadDTO toDTO(Localidad localidad);

    Localidad toEntity(LocalidadDTO localidadDTO);
}
