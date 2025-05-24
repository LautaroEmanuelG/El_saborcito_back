package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.DatosMercadoPagoDto;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
import utn.saborcito.El_saborcito_back.models.Factura;

@Mapper(componentModel = "spring")
public interface DatosMercadoPagoMapper {
    DatosMercadoPagoMapper INSTANCE = Mappers.getMapper(DatosMercadoPagoMapper.class);

    @Mapping(source = "factura", target = "factura", qualifiedByName = "facturaToId")
    DatosMercadoPagoDto toDto(DatosMercadoPago datosMercadoPago);

    @Mapping(target = "paymentType", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(source = "factura", target = "factura", qualifiedByName = "idToFactura")
    DatosMercadoPago toEntity(DatosMercadoPagoDto datosMercadoPagoDto);

    @Named("facturaToId")
    default Long facturaToId(Factura factura) {
        return factura != null ? factura.getId() : null;
    }

    @Named("idToFactura")
    default Factura idToFactura(Long id) {
        if (id == null)
            return null;
        Factura factura = new Factura();
        factura.setId(id);
        return factura;
    }
}