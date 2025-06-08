package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.RegistroClienteDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;

// ✅ Para HU1 - Registro de cliente
@Mapper(componentModel = "spring")
public interface RegistroClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "true") // ✅ Activo por defecto
    @Mapping(target = "fechaRegistro", ignore = true) // ✅ Se setea automáticamente
    @Mapping(target = "rol", constant = "CLIENTE") // ✅ Según HU1
    @Mapping(target = "password", ignore = true) // ✅ Auth0
    Cliente toEntity(RegistroClienteDTO dto);
}
