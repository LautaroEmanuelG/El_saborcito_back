package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosEmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

// ✅ Para HU6 - Actualización de empleado
@Mapper(componentModel = "spring", uses = { DomicilioMapper.class })
public interface ActualizacionEmpleadoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "rol", ignore = true) // ✅ Solo admin puede cambiar rol
    @Mapping(target = "primerLogin", ignore = true)
    @Mapping(target = "auth0Id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "fechaNacimiento", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaUltimaModificacion", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "legajo", ignore = true)
    @Mapping(target = "fechaIngreso", ignore = true)
    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "domicilios", source = "domicilios")
    void updateEmpleadoFromDTO(ActualizarDatosEmpleadoDTO dto, @MappingTarget Empleado empleado);
}
