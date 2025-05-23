package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.PaisDTO;
import utn.saborcito.El_saborcito_back.models.Pais;

@Mapper(componentModel = "spring")
public interface PaisMapper {
    PaisMapper INSTANCE = Mappers.getMapper(PaisMapper.class);

    PaisDTO toDTO(Pais pais);

    Pais toEntity(PaisDTO paisDTO);
}
