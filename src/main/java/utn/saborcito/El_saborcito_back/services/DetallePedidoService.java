package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
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

    public DetallePedido findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "DetallePedido no encontrado con ID: " + id));
    }

    public DetallePedido save(DetallePedido detalle) {
        validarDetalle(detalle);

        // Verificar que no exista otro detalle con el mismo artículo y pedido
        if (detalle.getPedido() != null && detalle.getArticulo() != null && detalle.getId() == null) {
            Long count = repo.countByPedidoAndArticulo(detalle.getPedido(), detalle.getArticulo());
            if (count > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe un detalle para este artículo en este pedido");
            }
        }

        return repo.save(detalle);
    }

    /**
     * Actualiza un detalle de pedido
     * 
     * @param id      ID del detalle de pedido
     * @param detalle Datos actualizados
     * @return El detalle actualizado
     */
    public DetallePedido update(Long id, DetallePedido detalle) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: DetallePedido no encontrado");
        }

        detalle.setId(id);
        validarDetalle(detalle);

        // Verificar que no exista otro detalle con el mismo artículo y pedido
        if (detalle.getPedido() != null && detalle.getArticulo() != null) {
            Long count = repo.countByPedidoAndArticuloAndIdNot(
                    detalle.getPedido(), detalle.getArticulo(), id);
            if (count > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe otro detalle para este artículo en este pedido");
            }
        }

        return repo.save(detalle);
    }

    /**
     * Valida que el detalle tenga los campos necesarios para ser guardado
     * 
     * @param detalle El detalle a validar
     */
    private void validarDetalle(DetallePedido detalle) {
        if (detalle.getArticulo() == null || detalle.getCantidad() == null || detalle.getPedido() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar artículo, pedido y cantidad");
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
     * @param id ID del detalle de pedido
     */
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: DetallePedido no encontrado");
        }

        repo.deleteById(id);
    }

    /**
     * Elimina todos los detalles de un pedido
     * 
     * @param pedido El pedido del cual se eliminarán todos los detalles
     */
    public void deleteByPedido(Pedido pedido) {
        repo.deleteByPedido(pedido);
    }
}
