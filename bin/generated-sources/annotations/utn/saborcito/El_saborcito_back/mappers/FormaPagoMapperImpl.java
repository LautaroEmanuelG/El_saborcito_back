package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.FormaPagoDTO;
import utn.saborcito.El_saborcito_back.models.FormaPago;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class FormaPagoMapperImpl implements FormaPagoMapper {

    @Override
    public FormaPagoDTO toDTO(FormaPago formaPago) {
        if ( formaPago == null ) {
            return null;
        }

        FormaPagoDTO.FormaPagoDTOBuilder formaPagoDTO = FormaPagoDTO.builder();

        formaPagoDTO.id( formaPago.getId() );
        formaPagoDTO.nombre( formaPago.getNombre() );

        return formaPagoDTO.build();
    }

    @Override
    public FormaPago toEntity(FormaPagoDTO formaPagoDTO) {
        if ( formaPagoDTO == null ) {
            return null;
        }

        FormaPago.FormaPagoBuilder formaPago = FormaPago.builder();

        formaPago.id( formaPagoDTO.getId() );
        formaPago.nombre( formaPagoDTO.getNombre() );

        return formaPago.build();
    }
}
