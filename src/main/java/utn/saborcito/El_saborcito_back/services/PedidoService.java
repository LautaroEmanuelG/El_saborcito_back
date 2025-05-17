package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.enums.TipoEnvio;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
import utn.saborcito.El_saborcito_back.models.DetallePedido;


import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repo;

    public List<Pedido> findAll() { return repo.findAll(); }
    public Pedido findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public Pedido save(Pedido pedido) {
        calcularTotal(pedido);
        calcularHoraEstimada(pedido);
        return repo.save(pedido);
    }

    public Pedido update(Long id, Pedido pedido) {
        Pedido existing = findById(id);

        existing.getDetalles().clear();

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(existing);
                existing.getDetalles().add(detalle);
            }
        }

        existing.setTipoEnvio(pedido.getTipoEnvio());
        existing.setFormaPago(pedido.getFormaPago());
        existing.setSucursal(pedido.getSucursal());
        existing.setCliente(pedido.getCliente());
        existing.setEstado(pedido.getEstado());

        calcularTotal(existing);
        calcularHoraEstimada(existing);

        return repo.save(existing);
    }


    private void calcularHoraEstimada(Pedido pedido) {
        int minutosCocina = pedido.getDetalles().stream()
                .filter(det -> det.getArticulo() instanceof ArticuloManufacturado)
                .map(det -> ((ArticuloManufacturado) det.getArticulo()).getTiempoEstimadoMinutos() * det.getCantidad())
                .reduce(0, Integer::sum);

        int minutosDelivery = pedido.getTipoEnvio() == TipoEnvio.DELIVERY ? 30 : 0;

        LocalTime horaEstimada = LocalTime.now().plusMinutes(minutosCocina + minutosDelivery);
        pedido.setHorasEstimadaFinalizacion(horaEstimada);
    }

    private void calcularTotal(Pedido pedido) {
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido debe tener detalles");
        }

        double total = pedido.getDetalles().stream()
                .mapToDouble(DetallePedido::getSubTotal)
                .sum();

        pedido.setTotal(total); // o aplicar descuento si lo tenés
    }
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
