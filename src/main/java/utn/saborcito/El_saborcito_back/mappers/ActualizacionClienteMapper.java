package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosClienteDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;

// ✅ Para HU3 - Actualización de cliente
@Mapper(componentModel = "spring")
public interface ActualizacionClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "rol", ignore = true)
    void updateClienteFromDTO(ActualizarDatosClienteDTO dto, @MappingTarget Cliente cliente);
}
