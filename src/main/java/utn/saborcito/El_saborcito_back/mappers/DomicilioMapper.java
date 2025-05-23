package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.models.Domicilio;

@Mapper(componentModel = "spring", uses = LocalidadMapper.class)
public interface DomicilioMapper {
    DomicilioMapper INSTANCE = Mappers.getMapper(DomicilioMapper.class);

    DomicilioDTO toDTO(Domicilio domicilio);

    Domicilio toEntity(DomicilioDTO domicilioDTO);
}
