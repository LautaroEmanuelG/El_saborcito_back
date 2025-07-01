package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.mappers.PedidoMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.Estado;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PedidoCocinaService {

    private final PedidoRepository pedidoRepository;
    private final EstadoService estadoService;
    private final PedidoMapper pedidoMapper;
    private final HistorialPedidoService historialPedidoService;
    private final PedidoService pedidoService;

    // ✅ IDs de estados (ajustados a la base de datos real)
    private static final Long ESTADO_PENDIENTE_ID = 8L;
    private static final Long ESTADO_EN_PREPARACION_ID = 2L;
    private static final Long ESTADO_LISTO_ID = 4L;
    private static final Long ESTADO_DEMORADO_ID = 3L;

    /**
     * Cambia el estado de un pedido según el flujo de cocina.
     */
    public PedidoDTO avanzarEstadoCocina(Long pedidoId, Long nuevoEstadoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + pedidoId));

        Long estadoActualId = pedido.getEstado().getId();

        // 🔒 Validaciones de transición
        if (estadoActualId.equals(ESTADO_PENDIENTE_ID) &&
                !(nuevoEstadoId.equals(ESTADO_EN_PREPARACION_ID) || nuevoEstadoId.equals(ESTADO_DEMORADO_ID))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Desde PENDIENTE solo puede ir a EN_PREPARACION o DEMORADO");
        }

        if ((estadoActualId.equals(ESTADO_EN_PREPARACION_ID) || estadoActualId.equals(ESTADO_DEMORADO_ID)) &&
                !nuevoEstadoId.equals(ESTADO_LISTO_ID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Desde este estado solo puede ir a LISTO");
        }

        if (estadoActualId.equals(ESTADO_LISTO_ID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido ya está en estado LISTO");
        }

        Estado nuevoEstado = estadoService.findById(nuevoEstadoId);
        pedido.setEstado(nuevoEstado);

        // 🎯 Si pasa a LISTO → registrar en historial
        if (nuevoEstadoId.equals(ESTADO_LISTO_ID)) {
            historialPedidoService.registrarPedido(
                    pedido.getCliente().getId(),
                    pedido.getId(),
                    "Pedido finalizado en cocina"
            );
        }

        return pedidoMapper.toDTO(pedidoRepository.save(pedido));
    }
    // Devuelve todos los pedidos en cocina (estados: PENDIENTE, EN_PREPARACION, DEMORADO)
    public List<PedidoDTO> obtenerPedidosActivosEnCocina() {
        List<Long> estadosCocina = List.of(ESTADO_PENDIENTE_ID, ESTADO_EN_PREPARACION_ID, ESTADO_DEMORADO_ID);
        return pedidoRepository.findByEstado_IdIn(estadosCocina).stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }
    public PedidoDTO avanzarEstadoPedido(Long pedidoId) {
        Pedido pedido = pedidoService.findEntityById(pedidoId);
        Long estadoIdActual = pedido.getEstado().getId();
        Estado nuevoEstado;

        switch (estadoIdActual.intValue()) {
            case 1: // PENDIENTE
                boolean tieneManufacturados = pedido.getDetalles().stream()
                        .anyMatch(d -> d.getArticulo() instanceof ArticuloManufacturado);

                if (tieneManufacturados) {
                    nuevoEstado = estadoService.findById(3L); // EN_PREPARACION
                } else {
                    nuevoEstado = estadoService.findById(4L); // LISTO
                    historialPedidoService.registrarPedido(pedido.getCliente().getId(), pedidoId, "Pedido pasó directo a LISTO (sin manufacturados)");
                }
                break;

            case 3: // EN_PREPARACION
            case 8: // DEMORADO
                nuevoEstado = estadoService.findById(4L); // LISTO
                historialPedidoService.registrarPedido(pedido.getCliente().getId(), pedidoId, "Pedido marcado como LISTO");
                break;

            case 4: // LISTO
                String tipoEnvio = pedido.getTipoEnvio().getNombre();
                if ("DELIVERY".equalsIgnoreCase(tipoEnvio)) {
                    nuevoEstado = estadoService.findById(5L); // DELIVERY
                } else {
                    nuevoEstado = estadoService.findById(6L); // ENTREGADO
                }
                break;

            case 5: // DELIVERY
                nuevoEstado = estadoService.findById(6L); // ENTREGADO
                break;

            case 6: // ENTREGADO
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido ya fue entregado");

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido para avanzar");
        }

        pedido.setEstado(nuevoEstado);
        Pedido actualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(actualizado);
    }


}
