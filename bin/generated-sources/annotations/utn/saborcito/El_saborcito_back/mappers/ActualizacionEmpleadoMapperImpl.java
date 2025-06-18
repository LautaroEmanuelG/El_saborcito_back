package utn.saborcito.El_saborcito_back.mappers;

import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosEmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Empleado;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:34-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ActualizacionEmpleadoMapperImpl implements ActualizacionEmpleadoMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;

    @Override
    public void updateEmpleadoFromDTO(ActualizarDatosEmpleadoDTO dto, Empleado empleado) {
        if ( dto == null ) {
            return;
        }

        if ( empleado.getDomicilios() != null ) {
            List<Domicilio> list = domicilioMapper.toEntityList( dto.getDomicilios() );
            if ( list != null ) {
                empleado.getDomicilios().clear();
                empleado.getDomicilios().addAll( list );
            }
            else {
                empleado.setDomicilios( null );
            }
        }
        else {
            List<Domicilio> list = domicilioMapper.toEntityList( dto.getDomicilios() );
            if ( list != null ) {
                empleado.setDomicilios( list );
            }
        }
        empleado.setApellido( dto.getApellido() );
        empleado.setEmail( dto.getEmail() );
        empleado.setNombre( dto.getNombre() );
        empleado.setTelefono( dto.getTelefono() );
    }
}
