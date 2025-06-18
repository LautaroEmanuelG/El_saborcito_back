package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.DatosMercadoPagoDto;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class DatosMercadoPagoMapperImpl implements DatosMercadoPagoMapper {

    @Override
    public DatosMercadoPagoDto toDto(DatosMercadoPago datosMercadoPago) {
        if ( datosMercadoPago == null ) {
            return null;
        }

        DatosMercadoPagoDto.DatosMercadoPagoDtoBuilder datosMercadoPagoDto = DatosMercadoPagoDto.builder();

        datosMercadoPagoDto.factura( facturaToId( datosMercadoPago.getFactura() ) );
        datosMercadoPagoDto.dateApproved( datosMercadoPago.getDateApproved() );
        datosMercadoPagoDto.dateCreate( datosMercadoPago.getDateCreate() );
        datosMercadoPagoDto.dateLastUpdate( datosMercadoPago.getDateLastUpdate() );
        datosMercadoPagoDto.id( datosMercadoPago.getId() );
        datosMercadoPagoDto.status( datosMercadoPago.getStatus() );
        datosMercadoPagoDto.statusDetail( datosMercadoPago.getStatusDetail() );

        return datosMercadoPagoDto.build();
    }

    @Override
    public DatosMercadoPago toEntity(DatosMercadoPagoDto datosMercadoPagoDto) {
        if ( datosMercadoPagoDto == null ) {
            return null;
        }

        DatosMercadoPago.DatosMercadoPagoBuilder datosMercadoPago = DatosMercadoPago.builder();

        datosMercadoPago.factura( idToFactura( datosMercadoPagoDto.getFactura() ) );
        datosMercadoPago.dateApproved( datosMercadoPagoDto.getDateApproved() );
        datosMercadoPago.dateCreate( datosMercadoPagoDto.getDateCreate() );
        datosMercadoPago.dateLastUpdate( datosMercadoPagoDto.getDateLastUpdate() );
        datosMercadoPago.id( datosMercadoPagoDto.getId() );
        datosMercadoPago.status( datosMercadoPagoDto.getStatus() );
        datosMercadoPago.statusDetail( datosMercadoPagoDto.getStatusDetail() );

        return datosMercadoPago.build();
    }
}
