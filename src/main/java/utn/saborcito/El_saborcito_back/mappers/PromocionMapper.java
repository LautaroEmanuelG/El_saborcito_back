package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO; // Corregido a dto

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, SucursalMapper.class })
public interface PromocionMapper {
    PromocionMapper INSTANCE = Mappers.getMapper(PromocionMapper.class);

    @Mapping(target = "articulo", ignore = true) // Ignorar campo obsoleto
    PromocionDTO toDTO(Promocion promocion);

    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "promocionDetalles", ignore = true) // Se maneja por separado
    Promocion toEntity(PromocionDTO promocionDTO);
}
