package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosEmpleadoDTO;
import utn.saborcito.El_saborcito_back.dto.EmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

@Mapper(componentModel = "spring", uses = { SucursalMapper.class, UsuarioMapper.class })
public interface EmpleadoMapper extends BaseMapper<Empleado, EmpleadoDTO> {
    @Override
    @Mapping(target = "sucursal", source = "sucursal")
    EmpleadoDTO toDTO(Empleado empleado);
    @Override
    @Mapping(target = "sucursal", ignore = true) // ✅ Se carga en servicio
    @Mapping(target = "usuario.password", ignore = true)
    @Mapping(target = "usuario.fechaRegistro", ignore = true)
    Empleado toEntity(EmpleadoDTO empleadoDTO);
}
