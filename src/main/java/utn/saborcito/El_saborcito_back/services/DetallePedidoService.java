package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.DetallePedidoId;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {
    private final DetallePedidoRepository repo;

    public List<DetallePedido> findAll() {
        return repo.findAll();
    }

    public List<DetallePedido> findByPedido(Pedido pedido) {
        return repo.findByPedido(pedido);
    }

    public DetallePedido findById(DetallePedidoId id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "DetallePedido no encontrado con ID: " + id));
    }

    public DetallePedido save(DetallePedido detalle) {
        validarDetalle(detalle);
        return repo.save(detalle);
    }

    /**
     * Actualiza un detalle de pedido
     * 
     * @param pedidoId   ID del pedido
     * @param articuloId ID del artículo
     * @param detalle    Datos actualizados
     * @return El detalle actualizado
     */
    public DetallePedido update(Long pedidoId, Long articuloId, DetallePedido detalle) {
        DetallePedidoId id = new DetallePedidoId(pedidoId, articuloId);

        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: DetallePedido no encontrado");
        }

        detalle.setPedidoId(pedidoId);
        detalle.setArticuloId(articuloId);
        validarDetalle(detalle);

        return repo.save(detalle);
    }

    /**
     * Valida que el detalle tenga los campos necesarios para ser guardado
     * 
     * @param detalle El detalle a validar
     */
    private void validarDetalle(DetallePedido detalle) {
        if (detalle.getArticulo() == null || detalle.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar artículo y cantidad");
        }

        if (detalle.getArticulo().getPrecioVenta() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El artículo " + detalle.getArticulo().getDenominacion() + " no tiene precio de venta asignado");
        }

        if (detalle.getCantidad() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }
    }

    /**
     * Elimina un detalle de pedido
     * 
     * @param pedidoId   ID del pedido
     * @param articuloId ID del artículo
     */
    public void delete(Long pedidoId, Long articuloId) {
        DetallePedidoId id = new DetallePedidoId(pedidoId, articuloId);

        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: DetallePedido no encontrado");
        }

        repo.deleteById(id);
    }

    /**
     * Elimina todos los detalles de un pedido
     * 
     * @param pedido El pedido
     */
    public void deleteByPedido(Pedido pedido) {
        repo.deleteByPedido(pedido);
    }
}
