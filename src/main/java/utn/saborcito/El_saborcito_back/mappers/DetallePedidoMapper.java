package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO;
import utn.saborcito.El_saborcito_back.models.DetallePedido;

@Mapper(componentModel = "spring", uses = { ArticuloMapper.class, ImagenMapper.class })
public interface DetallePedidoMapper extends BaseMapper<DetallePedido, DetallePedidoDTO> {
    DetallePedidoMapper INSTANCE = Mappers.getMapper(DetallePedidoMapper.class);

    @Override
    @Mapping(source = "articulo.id", target = "articulo.id")
    @Mapping(source = "articulo.categoria.id", target = "articulo.categoriaId")
    @Mapping(source = "articulo.imagen", target = "articulo.imagen")
    DetallePedidoDTO toDTO(DetallePedido entity);

    @Override
    @Mapping(target = "articulo", ignore = true) // Se manejará en la capa de servicio
    @Mapping(target = "pedido", ignore = true) // Se manejará en la capa de servicio
    @Mapping(target = "cantidadConPromocion", ignore = true) // Campo calculado
    @Mapping(target = "cantidadSinPromocion", ignore = true) // Campo calculado
    DetallePedido toEntity(DetallePedidoDTO dto);
}
