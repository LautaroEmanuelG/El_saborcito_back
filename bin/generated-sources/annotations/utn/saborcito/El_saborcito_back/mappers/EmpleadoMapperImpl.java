package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.EmpleadoDTO;
import utn.saborcito.El_saborcito_back.dto.ImagenDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.models.Imagen;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:30-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EmpleadoMapperImpl implements EmpleadoMapper {

    @Autowired
    private SucursalMapper sucursalMapper;
    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public EmpleadoDTO toDTO(Empleado empleado) {
        if ( empleado == null ) {
            return null;
        }

        EmpleadoDTO.EmpleadoDTOBuilder<?, ?> empleadoDTO = EmpleadoDTO.builder();

        empleadoDTO.sucursal( sucursalMapper.toDTO( empleado.getSucursal() ) );
        empleadoDTO.apellido( empleado.getApellido() );
        empleadoDTO.auth0Id( empleado.getAuth0Id() );
        empleadoDTO.domicilios( domicilioMapper.toDTOList( empleado.getDomicilios() ) );
        empleadoDTO.email( empleado.getEmail() );
        empleadoDTO.estado( empleado.getEstado() );
        empleadoDTO.fechaNacimiento( empleado.getFechaNacimiento() );
        empleadoDTO.fechaRegistro( empleado.getFechaRegistro() );
        empleadoDTO.fechaUltimaModificacion( empleado.getFechaUltimaModificacion() );
        empleadoDTO.id( empleado.getId() );
        empleadoDTO.imagen( imagenToImagenDTO( empleado.getImagen() ) );
        empleadoDTO.nombre( empleado.getNombre() );
        empleadoDTO.rol( empleado.getRol() );
        empleadoDTO.telefono( empleado.getTelefono() );
        empleadoDTO.fechaIngreso( empleado.getFechaIngreso() );
        empleadoDTO.legajo( empleado.getLegajo() );
        empleadoDTO.primerLogin( empleado.getPrimerLogin() );

        return empleadoDTO.build();
    }

    @Override
    public Empleado toEntity(EmpleadoDTO empleadoDTO) {
        if ( empleadoDTO == null ) {
            return null;
        }

        Empleado.EmpleadoBuilder<?, ?> empleado = Empleado.builder();

        empleado.domicilios( domicilioMapper.toEntityList( empleadoDTO.getDomicilios() ) );
        empleado.apellido( empleadoDTO.getApellido() );
        empleado.email( empleadoDTO.getEmail() );
        empleado.estado( empleadoDTO.getEstado() );
        empleado.fechaNacimiento( empleadoDTO.getFechaNacimiento() );
        empleado.id( empleadoDTO.getId() );
        empleado.imagen( imagenDTOToImagen( empleadoDTO.getImagen() ) );
        empleado.nombre( empleadoDTO.getNombre() );
        empleado.rol( empleadoDTO.getRol() );
        empleado.telefono( empleadoDTO.getTelefono() );
        empleado.fechaIngreso( empleadoDTO.getFechaIngreso() );
        empleado.legajo( empleadoDTO.getLegajo() );
        empleado.primerLogin( empleadoDTO.getPrimerLogin() );

        return empleado.build();
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
