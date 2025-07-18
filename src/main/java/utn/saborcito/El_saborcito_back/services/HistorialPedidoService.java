package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.HistorialPedidoDTO;
import utn.saborcito.El_saborcito_back.mappers.HistorialPedidoMapper;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.HistorialPedido;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.HistorialPedidoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistorialPedidoService {

    private final HistorialPedidoRepository repo;
    private final ClienteService clienteService;
    private final PedidoService pedidoService;
    private final HistorialPedidoMapper mapper;

    /**
     * 📊 Obtiene el historial de pedidos de un cliente optimizado (DTO)
     * Evita referencias circulares y consultas N+1
     * 
     * @param clienteId ID del cliente
     * @return Lista de DTOs de historial de pedidos
     */
    public List<HistorialPedidoDTO> findByClienteOptimized(Long clienteId) {
        // Verificar que el cliente existe
        clienteService.findById(clienteId);

        // Obtener historial con consulta optimizada
        List<HistorialPedido> historial = repo.findByClienteIdWithDetailsOptimized(clienteId);

        // Convertir a DTOs para evitar referencias circulares
        return mapper.toDTOList(historial);
    }

    /**
     * Obtiene el historial de pedidos de un cliente
     * 
     * @param clienteId ID del cliente
     * @return Lista de registros de historial de pedidos
     */
    public List<HistorialPedido> findByCliente(Long clienteId) {
        Cliente cliente = clienteService.findById(clienteId);
        return repo.findByClienteOrderByFechaRegistroDesc(cliente);
    }

    /**
     * Obtiene el historial de un pedido
     * 
     * @param pedidoId ID del pedido
     * @return Lista de registros de historial del pedido
     */
    public List<HistorialPedido> findByPedido(Long pedidoId) {
        Pedido pedido = pedidoService.findEntityById(pedidoId);
        return repo.findByPedidoOrderByFechaRegistroDesc(pedido);
    }

    /**
     * Registra un pedido en el historial de un cliente
     * 
     * @param clienteId   ID del cliente
     * @param pedidoId    ID del pedido
     * @param observacion Observación opcional
     * @return El registro de historial creado
     */
    public HistorialPedido registrarPedido(Long clienteId, Long pedidoId, String observacion) {
        Cliente cliente = clienteService.findById(clienteId);
        Pedido pedido = pedidoService.findEntityById(pedidoId);

        // Permitir múltiples registros de historial para el mismo cliente y pedido
        HistorialPedido historial = new HistorialPedido();
        historial.setCliente(cliente);
        historial.setPedido(pedido);
        historial.setObservacion(observacion);
        historial.setFechaRegistro(LocalDateTime.now());
        return repo.save(historial);
    }

    /**
     * Obtiene un registro de historial por su ID
     * 
     * @param id ID del registro
     * @return El registro de historial
     */
    public HistorialPedido findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Registro de historial no encontrado con ID: " + id));
    }

    /**
     * Actualiza un registro de historial
     * 
     * @param id        ID del registro
     * @param historial Datos actualizados
     * @return El registro actualizado
     */
    public HistorialPedido update(Long id, HistorialPedido historial) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: Registro de historial no encontrado con ID: " + id);
        }

        historial.setId(id);
        return repo.save(historial);
    }

    /**
     * Elimina un registro de historial
     * 
     * @param id ID del registro
     */
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Registro de historial no encontrado con ID: " + id);
        }

        repo.deleteById(id);
    }
}
