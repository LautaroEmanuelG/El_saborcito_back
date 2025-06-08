package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosEmpleadoDTO;
import utn.saborcito.El_saborcito_back.dto.EmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

@Mapper(componentModel = "spring", uses = { SucursalMapper.class, DomicilioMapper.class })
public interface EmpleadoMapper extends BaseMapper<Empleado, EmpleadoDTO> {
    @Override
    @Mapping(target = "sucursal", source = "sucursal")
    EmpleadoDTO toDTO(Empleado empleado);

    @Override
    @Mapping(target = "sucursal", ignore = true) // ✅ Se carga en servicio
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaUltimaModificacion", ignore = true)
    @Mapping(target = "auth0Id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "domicilios", source = "domicilios")
    Empleado toEntity(EmpleadoDTO empleadoDTO);
}
