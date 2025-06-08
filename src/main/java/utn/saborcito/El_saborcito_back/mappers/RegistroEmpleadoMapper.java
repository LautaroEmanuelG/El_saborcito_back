package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.RegistroEmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

import java.time.LocalDate;

// ✅ Para HU4 - Registro de empleado
@Mapper(componentModel = "spring", imports = { LocalDate.class }, uses = { DomicilioMapper.class })
public interface RegistroEmpleadoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "true")
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaUltimaModificacion", ignore = true)
    @Mapping(target = "primerLogin", constant = "true") // ✅ Según HU5
    @Mapping(target = "fechaIngreso", expression = "java(LocalDate.now())")
    @Mapping(target = "sucursal", ignore = true) // Se carga en servicio
    @Mapping(target = "auth0Id", ignore = true) // ✅ Se asigna desde el servicio
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "legajo", ignore = true) // Se carga en servicio
    @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "domicilios", source = "domicilios")
    Empleado toEntity(RegistroEmpleadoDTO dto);
}
