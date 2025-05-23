package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.models.Pedido;

@Mapper(componentModel = "spring", uses = {
        EstadoMapper.class,
        TipoEnvioMapper.class,
        FormaPagoMapper.class,
        ClienteMapper.class,
        SucursalMapper.class, // Asegúrate que SucursalMapper existe y es accesible
        DetallePedidoMapper.class
})
public interface PedidoMapper {
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "tipoEnvio", target = "tipoEnvio")
    @Mapping(source = "formaPago", target = "formaPago")
    @Mapping(source = "cliente", target = "cliente")
    @Mapping(source = "sucursal", target = "sucursal")
    @Mapping(source = "detalles", target = "detalles")
    PedidoDTO toDTO(Pedido pedido);

    /*
     * // Comentado debido a la naturaleza abstracta de Articulo dentro de
     * DetallePedido
     * 
     * @Mapping(source = "estado", target = "estado")
     * 
     * @Mapping(source = "tipoEnvio", target = "tipoEnvio")
     * 
     * @Mapping(source = "formaPago", target = "formaPago")
     * 
     * @Mapping(source = "cliente", target = "cliente")
     * 
     * @Mapping(source = "sucursal", target = "sucursal")
     * 
     * @Mapping(source = "detalles", target = "detalles")
     * Pedido toEntity(PedidoDTO pedidoDTO);
     */
}
