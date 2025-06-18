package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ClienteDTO;
import utn.saborcito.El_saborcito_back.dto.ImagenDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Imagen;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public ClienteDTO toDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        ClienteDTO.ClienteDTOBuilder<?, ?> clienteDTO = ClienteDTO.builder();

        clienteDTO.apellido( cliente.getApellido() );
        clienteDTO.auth0Id( cliente.getAuth0Id() );
        clienteDTO.domicilios( domicilioMapper.toDTOList( cliente.getDomicilios() ) );
        clienteDTO.email( cliente.getEmail() );
        clienteDTO.estado( cliente.getEstado() );
        clienteDTO.fechaNacimiento( cliente.getFechaNacimiento() );
        clienteDTO.fechaRegistro( cliente.getFechaRegistro() );
        clienteDTO.fechaUltimaModificacion( cliente.getFechaUltimaModificacion() );
        clienteDTO.id( cliente.getId() );
        clienteDTO.imagen( imagenToImagenDTO( cliente.getImagen() ) );
        clienteDTO.nombre( cliente.getNombre() );
        clienteDTO.rol( cliente.getRol() );
        clienteDTO.telefono( cliente.getTelefono() );

        return clienteDTO.build();
    }

    @Override
    public Cliente toEntity(ClienteDTO clienteDTO) {
        if ( clienteDTO == null ) {
            return null;
        }

        Cliente.ClienteBuilder<?, ?> cliente = Cliente.builder();

        cliente.domicilios( domicilioMapper.toEntityList( clienteDTO.getDomicilios() ) );
        cliente.apellido( clienteDTO.getApellido() );
        cliente.email( clienteDTO.getEmail() );
        cliente.estado( clienteDTO.getEstado() );
        cliente.fechaNacimiento( clienteDTO.getFechaNacimiento() );
        cliente.id( clienteDTO.getId() );
        cliente.imagen( imagenDTOToImagen( clienteDTO.getImagen() ) );
        cliente.nombre( clienteDTO.getNombre() );
        cliente.rol( clienteDTO.getRol() );
        cliente.telefono( clienteDTO.getTelefono() );

        return cliente.build();
    }

    protected ImagenDTO imagenToImagenDTO(Imagen imagen) {
        if ( imagen == null ) {
            return null;
        }

        ImagenDTO imagenDTO = new ImagenDTO();

        imagenDTO.setId( imagen.getId() );
        imagenDTO.setUrl( imagen.getUrl() );

        return imagenDTO;
    }

    protected Imagen imagenDTOToImagen(ImagenDTO imagenDTO) {
        if ( imagenDTO == null ) {
            return null;
        }

        Imagen.ImagenBuilder imagen = Imagen.builder();

        imagen.id( imagenDTO.getId() );
        imagen.url( imagenDTO.getUrl() );

        return imagen.build();
    }
}
