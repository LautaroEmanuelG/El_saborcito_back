package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ImagenDTO;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.models.Usuario;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO.UsuarioDTOBuilder<?, ?> usuarioDTO = UsuarioDTO.builder();

        usuarioDTO.imagen( imagenToImagenDTO( usuario.getImagen() ) );
        usuarioDTO.domicilios( domicilioMapper.toDTOList( usuario.getDomicilios() ) );
        usuarioDTO.apellido( usuario.getApellido() );
        usuarioDTO.auth0Id( usuario.getAuth0Id() );
        usuarioDTO.email( usuario.getEmail() );
        usuarioDTO.estado( usuario.getEstado() );
        usuarioDTO.fechaNacimiento( usuario.getFechaNacimiento() );
        usuarioDTO.fechaRegistro( usuario.getFechaRegistro() );
        usuarioDTO.fechaUltimaModificacion( usuario.getFechaUltimaModificacion() );
        usuarioDTO.id( usuario.getId() );
        usuarioDTO.nombre( usuario.getNombre() );
        usuarioDTO.rol( usuario.getRol() );
        usuarioDTO.telefono( usuario.getTelefono() );

        return usuarioDTO.build();
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario.UsuarioBuilder<?, ?> usuario = Usuario.builder();

        usuario.domicilios( domicilioMapper.toEntityList( usuarioDTO.getDomicilios() ) );
        usuario.apellido( usuarioDTO.getApellido() );
        usuario.email( usuarioDTO.getEmail() );
        usuario.estado( usuarioDTO.getEstado() );
        usuario.fechaNacimiento( usuarioDTO.getFechaNacimiento() );
        usuario.id( usuarioDTO.getId() );
        usuario.imagen( imagenDTOToImagen( usuarioDTO.getImagen() ) );
        usuario.nombre( usuarioDTO.getNombre() );
        usuario.rol( usuarioDTO.getRol() );
        usuario.telefono( usuarioDTO.getTelefono() );

        return usuario.build();
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
