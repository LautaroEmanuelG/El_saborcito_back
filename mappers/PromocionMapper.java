package com.example.mapper;

import com.example.dto.PromocionDTO;
import com.example.dto.PromocionDetalleDTO;
import com.example.entity.Promocion;
import com.example.entity.PromocionDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, SucursalMapper.class })
public interface PromocionMapper {
    PromocionMapper INSTANCE = Mappers.getMapper(PromocionMapper.class);

    @Mapping(target = "articulo", ignore = true)
    @Mapping(source = "promocionDetalles", target = "promocionDetalles")
    @Mapping(source = "eliminado", target = "eliminado")
    PromocionDTO toDTO(Promocion promocion);

    @Mapping(target = "sucursal", ignore = true)
    @Mapping(source = "promocionDetalles", target = "promocionDetalles")
    @Mapping(source = "eliminado", target = "eliminado")
    Promocion toEntity(PromocionDTO promocionDTO);

    @Mapping(source = "promocion.id", target = "promocionId")
    @Mapping(source = "articulo", target = "articulo")
    @Mapping(source = "articulo.categoria.id", target = "articulo.categoriaId")
    PromocionDetalleDTO toDetalleDTO(PromocionDetalle promocionDetalle);

    @Mapping(target = "promocion", ignore = true)
    @Mapping(target = "articulo", ignore = true)
    PromocionDetalle toDetalleEntity(PromocionDetalleDTO promocionDetalleDTO);
}