package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.DatosMercadoPagoDto;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;

@Mapper(componentModel = "spring")
public interface DatosMercadoPagoMapper {
    DatosMercadoPagoMapper INSTANCE = Mappers.getMapper(DatosMercadoPagoMapper.class);

    @Mapping(source = "factura.id", target = "facturaId")
    DatosMercadoPagoDto toDto(DatosMercadoPago datosMercadoPago);

    @Mapping(source = "facturaId", target = "factura.id")
    DatosMercadoPago toEntity(DatosMercadoPagoDto datosMercadoPagoDto);
}
