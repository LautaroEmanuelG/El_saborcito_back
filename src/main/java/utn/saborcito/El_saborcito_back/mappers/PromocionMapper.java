package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.models.PromocionDetalle;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO;
import utn.saborcito.El_saborcito_back.dto.PromocionDetalleDTO;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, SucursalMapper.class })
public interface PromocionMapper {
    PromocionMapper INSTANCE = Mappers.getMapper(PromocionMapper.class);

    @Mapping(target = "articulo", ignore = true) // Ignorar campo obsoleto
    @Mapping(source = "promocionDetalles", target = "promocionDetalles")
    PromocionDTO toDTO(Promocion promocion);

    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "promocionDetalles", ignore = true) // Se maneja por separado
    Promocion toEntity(PromocionDTO promocionDTO); // Mapeos para PromocionDetalle

    @Mapping(source = "promocion.id", target = "promocionId")
    @Mapping(source = "articulo", target = "articulo")
    @Mapping(source = "articulo.categoria.id", target = "articulo.categoriaId")
    PromocionDetalleDTO toDetalleDTO(PromocionDetalle promocionDetalle);

    @Mapping(target = "promocion", ignore = true)
    @Mapping(target = "articulo", ignore = true)
    PromocionDetalle toDetalleEntity(PromocionDetalleDTO promocionDetalleDTO);
}
