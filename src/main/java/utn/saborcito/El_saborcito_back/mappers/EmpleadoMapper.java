package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.EmpleadoDTO;
import utn.saborcito.El_saborcito_back.models.Empleado;

@Mapper(componentModel = "spring", uses = { SucursalMapper.class, UsuarioMapper.class })
public interface EmpleadoMapper extends BaseMapper<Empleado, EmpleadoDTO> {

    @Override
    @Mapping(source = "sucursal", target = "sucursal")
    @Mapping(source = "usuario", target = "usuario")
    EmpleadoDTO toDTO(Empleado source);

    @Override
    // La carga de las entidades Sucursal y Usuario se hará en el servicio
    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Empleado toEntity(EmpleadoDTO source);
}
