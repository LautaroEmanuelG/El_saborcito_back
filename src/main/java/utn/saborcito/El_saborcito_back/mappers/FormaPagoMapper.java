package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.FormaPagoDTO;
import utn.saborcito.El_saborcito_back.models.FormaPago;

@Mapper(componentModel = "spring")
public interface FormaPagoMapper {
    FormaPagoMapper INSTANCE = Mappers.getMapper(FormaPagoMapper.class);

    FormaPagoDTO toDTO(FormaPago formaPago);

    FormaPago toEntity(FormaPagoDTO formaPagoDTO);
}
