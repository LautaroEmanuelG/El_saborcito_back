package utn.saborcito.El_saborcito_back.mappers;

import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.HistorialPedidoDTO;
import utn.saborcito.El_saborcito_back.models.HistorialPedido;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 🔄 Mapper para convertir entidades HistorialPedido a DTOs
 * Evita problemas de serialización y referencias circulares
 */
@Component
public class HistorialPedidoMapper {

    /**
     * Convierte una entidad HistorialPedido a DTO
     */
    public HistorialPedidoDTO toDTO(HistorialPedido historial) {
        if (historial == null) {
            return null;
        }

        return HistorialPedidoDTO.builder()
                .id(historial.getId())
                .fechaRegistro(historial.getFechaRegistro())
                .observacion(historial.getObservacion())
                .cliente(buildClienteBasicoDTO(historial))
                .pedido(buildPedidoBasicoDTO(historial))
                .build();
    }

    /**
     * Convierte una lista de entidades a DTOs
     */
    public List<HistorialPedidoDTO> toDTOList(List<HistorialPedido> historialList) {
        if (historialList == null) {
            return null;
        }

        return historialList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Construye el DTO básico del cliente
     */
    private HistorialPedidoDTO.ClienteBasicoDTO buildClienteBasicoDTO(HistorialPedido historial) {
        if (historial.getCliente() == null) {
            return null;
        }

        var cliente = historial.getCliente();
        return HistorialPedidoDTO.ClienteBasicoDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .build();
    }

    /**
     * Construye el DTO básico del pedido
     */
    private HistorialPedidoDTO.PedidoBasicoDTO buildPedidoBasicoDTO(HistorialPedido historial) {
        if (historial.getPedido() == null) {
            return null;
        }

        var pedido = historial.getPedido();
        return HistorialPedidoDTO.PedidoBasicoDTO.builder()
                .id(pedido.getId())
                .fechaPedido(pedido.getFechaPedido())
                .horasEstimadaFinalizacion(pedido.getHorasEstimadaFinalizacion())
                .total(pedido.getTotal())
                .estado(pedido.getEstado() != null ? pedido.getEstado().getNombre() : null)
                .tipoEnvio(pedido.getTipoEnvio() != null ? pedido.getTipoEnvio().getNombre() : null)
                .formaPago(pedido.getFormaPago() != null ? pedido.getFormaPago().getNombre() : null)
                .sucursal(pedido.getSucursal() != null ? pedido.getSucursal().getNombre() : null)
                .cantidadArticulos(pedido.getDetalles() != null ? pedido.getDetalles().size() : 0)
                .build();
    }
}
