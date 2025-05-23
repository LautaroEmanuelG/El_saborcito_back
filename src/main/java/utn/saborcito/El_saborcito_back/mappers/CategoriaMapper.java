package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Mapper(componentModel = "spring") // SucursalMapper.class podría causar ciclo si CategoriaDTO tiene SucursalDTO
public interface CategoriaMapper extends BaseMapper<Categoria, CategoriaDTO> {
    // @Mapping(source = "sucursal", target = "sucursal") // Omitir o manejar con cuidado
    @Override
    @Mapping(source = "tipoCategoria", target = "tipoCategoria")
    CategoriaDTO toDTO(Categoria source);

    @Override
    @Mapping(target = "sucursal", ignore = true) // Ignorar sucursal al convertir de DTO a entidad para evitar problemas
    // @Mapping(target = "articulos", ignore = true) // Property does not exist in Categoria
    Categoria toEntity(CategoriaDTO source);
}
