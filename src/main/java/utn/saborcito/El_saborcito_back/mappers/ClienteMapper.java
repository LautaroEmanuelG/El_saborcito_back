package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.ClienteDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;

@Mapper(componentModel = "spring", uses = { UsuarioMapper.class })
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    @Mapping(source = "usuario", target = "usuario")
    // No es necesario ignorar historialPedidos aquí, ya que no existe en ClienteDTO
    ClienteDTO toDTO(Cliente cliente);

    @Mapping(source = "usuario", target = "usuario")
    @Mapping(target = "historialPedidos", ignore = true) // Mantener para toEntity si es necesario
    Cliente toEntity(ClienteDTO clienteDTO);
}
