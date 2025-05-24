package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.UsuarioDTO;
import utn.saborcito.El_saborcito_back.models.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "auth0Id", ignore = true)
    @Mapping(target = "username", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
