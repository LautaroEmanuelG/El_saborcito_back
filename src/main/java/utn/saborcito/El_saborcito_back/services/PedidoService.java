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
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle; // <--- IMPORTACIÓN AÑADIDA

import java.time.LocalDate; // <--- IMPORTACIÓN AÑADIDA
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repo;

    public List<Pedido> findAll() {
        return repo.findAll();
    }

    public Pedido findById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));
    }

    public Pedido save(Pedido pedido) {
        if (pedido.getFechaPedido() == null) { // <--- CORRECCIÓN 18: Establecer fechaPedido si es nula
            pedido.setFechaPedido(LocalDate.now());
        }
        calcularTotal(pedido);
        calcularHoraEstimada(pedido);
        return repo.save(pedido);
    }

    public Pedido update(Long id, Pedido pedido) {
        Pedido existing = findById(id); // findById ya lanza la excepción si no se encuentra

        // CORRECCIÓN 18: No permitir la modificación de fechaPedido
        // existing.setFechaPedido(pedido.getFechaPedido()); // <--- LÍNEA ELIMINADA O
        // COMENTADA

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

        double totalPedido = 0.0;
        double totalCostoPedido = 0.0;

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getArticulo() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Detalle de pedido inválido: artículo o cantidad nulos o cantidad no positiva.");
            }
            if (detalle.getArticulo().getPrecioVenta() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El artículo "
                        + detalle.getArticulo().getDenominacion() + " no tiene precio de venta asignado.");
            }

            // C5: Calcular DetallePedido.subTotal
            double subTotal = detalle.getCantidad() * detalle.getArticulo().getPrecioVenta();
            detalle.setSubTotal(subTotal);
            totalPedido += subTotal;

            // Cálculo del costo para Pedido.totalCosto
            if (detalle.getArticulo() instanceof ArticuloInsumo insumo) {
                if (insumo.getPrecioCompra() != null) {
                    totalCostoPedido += insumo.getPrecioCompra() * detalle.getCantidad();
                }
                // Considerar lanzar error si el precio de compra es nulo y se necesita para el
                // cálculo
            } else if (detalle.getArticulo() instanceof ArticuloManufacturado manufacturado) {
                if (manufacturado.getArticuloManufacturadoDetalles() != null) {
                    double costoManufacturado = 0.0;
                    for (ArticuloManufacturadoDetalle amd : manufacturado.getArticuloManufacturadoDetalles()) {
                        if (amd.getArticuloInsumo() != null && amd.getArticuloInsumo().getPrecioCompra() != null
                                && amd.getCantidad() != null) {
                            costoManufacturado += amd.getArticuloInsumo().getPrecioCompra() * amd.getCantidad();
                        }
                        // Considerar manejo si falta precio de compra en algún insumo del manufacturado
                    }
                    totalCostoPedido += costoManufacturado * detalle.getCantidad();
                }
            }
        }

        // C6: Calcular Pedido.total
        pedido.setTotal(totalPedido);
        pedido.setTotalCosto(totalCostoPedido); // Asignar el totalCosto calculado
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Pedido no encontrado con ID: " + id);
        repo.deleteById(id);
    }
}
