package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosEmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

// ✅ Para HU6 - Actualización de empleado
@Mapper(componentModel = "spring")
public interface ActualizacionEmpleadoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "rol", ignore = true) // ✅ Solo admin puede cambiar rol
    @Mapping(target = "primerLogin", ignore = true)
    void updateEmpleadoFromDTO(ActualizarDatosEmpleadoDTO dto, @MappingTarget Empleado empleado);
}
