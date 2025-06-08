package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.models.Usuario;

@Mapper(componentModel = "spring", uses = { DomicilioMapper.class })
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDTO> {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Override
    @Mapping(target = "imagen", source = "imagen")
    @Mapping(target = "domicilios", source = "domicilios")
    UsuarioDTO toDTO(Usuario usuario);

    @Override
    @Mapping(target = "password", ignore = true) // ✅ Auth0 maneja esto
    @Mapping(target = "username", ignore = true) // ✅ Usar email como username
    @Mapping(target = "fechaRegistro", ignore = true) // ✅ Se setea automáticamente
    @Mapping(target = "fechaUltimaModificacion", ignore = true)
    @Mapping(target = "auth0Id", ignore = true)
    @Mapping(target = "domicilios", source = "domicilios")
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
