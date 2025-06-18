package utn.saborcito.El_saborcito_back.mappers;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.RegistroEmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class RegistroEmpleadoMapperImpl implements RegistroEmpleadoMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public Empleado toEntity(RegistroEmpleadoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Empleado.EmpleadoBuilder<?, ?> empleado = Empleado.builder();

        empleado.domicilios( domicilioMapper.toEntityList( dto.getDomicilios() ) );
        empleado.apellido( dto.getApellido() );
        empleado.email( dto.getEmail() );
        empleado.fechaNacimiento( dto.getFechaNacimiento() );
        empleado.nombre( dto.getNombre() );
        empleado.password( dto.getPassword() );
        empleado.telefono( dto.getTelefono() );

        empleado.estado( true );
        empleado.primerLogin( true );
        empleado.fechaIngreso( LocalDate.now() );

        return empleado.build();
    }
}
