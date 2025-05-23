package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dtos.SucursalDTO;
import utn.saborcito.El_saborcito_back.models.Sucursal;

import java.util.List;

@Mapper(componentModel = "spring", uses = { DomicilioMapper.class, EmpresaMapper.class, HorarioAtencionMapper.class })
public interface SucursalMapper {
    SucursalMapper INSTANCE = Mappers.getMapper(SucursalMapper.class);

    SucursalDTO toDTO(Sucursal sucursal);

    Sucursal toEntity(SucursalDTO sucursalDTO);

    List<SucursalDTO> toDTOList(List<Sucursal> sucursales);
}
