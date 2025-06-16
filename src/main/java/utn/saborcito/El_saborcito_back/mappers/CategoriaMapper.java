package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.models.Categoria;

@Mapper(componentModel = "spring", uses = {SucursalMapper.class}) // Asegúrate de incluir SucursalMapper
public interface CategoriaMapper extends BaseMapper<Categoria, CategoriaDTO> {
    @Override
    @Mapping(source = "tipoCategoria", target = "tipoCategoria")
    @Mapping(source = "sucursal", target = "sucursal") // Mapea sucursal correctamente
    CategoriaDTO toDTO(Categoria source);

    @Override
    @Mapping(source = "sucursal", target = "sucursal") // Mapea sucursal correctamente
    @Mapping(target = "eliminado", ignore = true) // Ignorar campo eliminado al crear desde DTO
    Categoria toEntity(CategoriaDTO source);
}
