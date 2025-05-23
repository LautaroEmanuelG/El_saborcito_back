package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.ProvinciaDTO;
import utn.saborcito.El_saborcito_back.models.Provincia;

@Mapper(componentModel = "spring", uses = PaisMapper.class)
public interface ProvinciaMapper {
    ProvinciaMapper INSTANCE = Mappers.getMapper(ProvinciaMapper.class);

    ProvinciaDTO toDTO(Provincia provincia);

    Provincia toEntity(ProvinciaDTO provinciaDTO);
}
