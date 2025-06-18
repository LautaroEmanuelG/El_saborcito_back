package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.models.Pedido;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PedidoMapperImpl implements PedidoMapper {

    @Autowired
    private EstadoMapper estadoMapper;
    @Autowired
    private TipoEnvioMapper tipoEnvioMapper;
    @Autowired
    private FormaPagoMapper formaPagoMapper;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private SucursalMapper sucursalMapper;
    @Autowired
    private DetallePedidoMapper detallePedidoMapper;

    @Override
    public PedidoDTO toDTO(Pedido pedido) {
        if ( pedido == null ) {
            return null;
        }

        PedidoDTO.PedidoDTOBuilder pedidoDTO = PedidoDTO.builder();

        pedidoDTO.estado( estadoMapper.toDTO( pedido.getEstado() ) );
        pedidoDTO.tipoEnvio( tipoEnvioMapper.toDTO( pedido.getTipoEnvio() ) );
        pedidoDTO.formaPago( formaPagoMapper.toDTO( pedido.getFormaPago() ) );
        pedidoDTO.cliente( clienteMapper.toDTO( pedido.getCliente() ) );
        pedidoDTO.sucursal( sucursalMapper.toDTO( pedido.getSucursal() ) );
        pedidoDTO.detalles( detallePedidoMapper.toDTOList( pedido.getDetalles() ) );
        pedidoDTO.fechaPedido( pedido.getFechaPedido() );
        pedidoDTO.horasEstimadaFinalizacion( pedido.getHorasEstimadaFinalizacion() );
        pedidoDTO.id( pedido.getId() );
        pedidoDTO.total( pedido.getTotal() );
        pedidoDTO.totalCosto( pedido.getTotalCosto() );

        return pedidoDTO.build();
    }
}
