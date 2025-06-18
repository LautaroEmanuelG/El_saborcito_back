package utn.saborcito.El_saborcito_back.mappers;

import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosClienteDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Domicilio;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ActualizacionClienteMapperImpl implements ActualizacionClienteMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public void updateClienteFromDTO(ActualizarDatosClienteDTO dto, Cliente cliente) {
        if ( dto == null ) {
            return;
        }

        if ( cliente.getDomicilios() != null ) {
            List<Domicilio> list = domicilioMapper.toEntityList( dto.getDomicilios() );
            if ( list != null ) {
                cliente.getDomicilios().clear();
                cliente.getDomicilios().addAll( list );
            }
            else {
                cliente.setDomicilios( null );
            }
        }
        else {
            List<Domicilio> list = domicilioMapper.toEntityList( dto.getDomicilios() );
            if ( list != null ) {
                cliente.setDomicilios( list );
            }
        }
        cliente.setApellido( dto.getApellido() );
        cliente.setEmail( dto.getEmail() );
        cliente.setNombre( dto.getNombre() );
        cliente.setTelefono( dto.getTelefono() );
    }
}
