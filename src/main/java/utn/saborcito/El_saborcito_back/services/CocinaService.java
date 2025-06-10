package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.mappers.PedidoMapper;
import utn.saborcito.El_saborcito_back.models.Estado;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.EstadoRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CocinaService {

    private final PedidoRepository pedidoRepository;
    private final EstadoRepository estadoRepository;
    private final PedidoMapper pedidoMapper;

    private static final Set<String> ESTADOS_COCINA = Set.of("PENDIENTE", "EN_PREPARACION", "DEMORADO", "LISTO");

    public List<PedidoDTO> obtenerPedidosParaCocina() {
        List<Pedido> pedidos = pedidoRepository.findByEstadoNombreIn(ESTADOS_COCINA.stream().toList());
        return pedidos.stream().map(pedidoMapper::toDTO).collect(Collectors.toList());
    }

    public PedidoDTO actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        if (!ESTADOS_COCINA.contains(nuevoEstado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido para cocina.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        Estado estado = estadoRepository.findByNombre(nuevoEstado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no reconocido"));

        pedido.setEstado(estado);
        return pedidoMapper.toDTO(pedidoRepository.save(pedido));
    }
}
