package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO;
import utn.saborcito.El_saborcito_back.models.DetallePedido;

@Mapper(componentModel = "spring", uses = { ArticuloMapper.class })
public interface DetallePedidoMapper {
    DetallePedidoMapper INSTANCE = Mappers.getMapper(DetallePedidoMapper.class);

    @Mapping(source = "articulo", target = "articulo")
    @Mapping(target = "subtotal", expression = "java(detallePedido.calcularSubtotal())") // Usar el método para calcular
                                                                                         // el subtotal
    DetallePedidoDTO toDTO(DetallePedido detallePedido);

    /*
     * // Comentado debido a la naturaleza abstracta de Articulo
     * 
     * @Mapping(source = "articulo", target = "articulo")
     * 
     * @Mapping(target = "pedido", ignore = true) // Ignorar pedido en la conversión
     * a entidad
     * DetallePedido toEntity(DetallePedidoDTO detallePedidoDTO);
     */
}
