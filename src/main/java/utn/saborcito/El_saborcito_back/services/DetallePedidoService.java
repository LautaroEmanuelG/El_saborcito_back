package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {
    private final DetallePedidoRepository repo;

    public List<DetallePedido> findAll() {
        return repo.findAll();
    }

    public DetallePedido findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetallePedido no encontrado"));
    }

    public DetallePedido save(DetallePedido detalle) {
        calcularSubtotal(detalle);
        return repo.save(detalle);
    }

    public DetallePedido update(Long id, DetallePedido detalle) {
        detalle.setId_DetallePedido(id);
        calcularSubtotal(detalle);
        return repo.save(detalle);
    }

    private void calcularSubtotal(DetallePedido d) {
        if (d.getArticulo() == null || d.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar artículo y cantidad");
        }
        d.setSubTotal(d.getCantidad() * d.getArticulo().getPrecioVenta().doubleValue());
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DetallePedido no encontrado");
        repo.deleteById(id);
    }
}
