package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.RegistroClienteDTO;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.models.Cliente;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class RegistroClienteMapperImpl implements RegistroClienteMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public Cliente toEntity(RegistroClienteDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Cliente.ClienteBuilder<?, ?> cliente = Cliente.builder();

        cliente.domicilios( domicilioMapper.toEntityList( dto.getDomicilios() ) );
        cliente.apellido( dto.getApellido() );
        cliente.email( dto.getEmail() );
        cliente.fechaNacimiento( dto.getFechaNacimiento() );
        cliente.nombre( dto.getNombre() );
        cliente.telefono( dto.getTelefono() );

        cliente.estado( true );
        cliente.rol( Rol.CLIENTE );

        return cliente.build();
    }
}
