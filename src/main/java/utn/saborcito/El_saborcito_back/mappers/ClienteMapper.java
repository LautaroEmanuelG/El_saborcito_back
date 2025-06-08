package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.ActualizarDatosClienteDTO;
import utn.saborcito.El_saborcito_back.dto.ClienteDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;

@Mapper(componentModel = "spring", uses = { DomicilioMapper.class })
public interface ClienteMapper extends BaseMapper<Cliente, ClienteDTO> {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO toDTO(Cliente cliente);

    @Override
    @Mapping(target = "historialPedidos", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaUltimaModificacion", ignore = true)
    @Mapping(target = "auth0Id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "domicilios", source = "domicilios")
    Cliente toEntity(ClienteDTO clienteDTO);
}
