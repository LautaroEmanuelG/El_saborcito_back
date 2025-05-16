package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.enums.Estado;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.FacturaRepository;
import utn.saborcito.El_saborcito_back.repositories.NotaCreditoRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotaCreditoService {
    private final NotaCreditoRepository repo;
    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;

    public List<NotaCredito> findAll() { return repo.findAll(); }
    public NotaCredito findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public NotaCredito save(NotaCredito nota) {
        boolean yaExiste = repo.existsByFacturaIdAndMonto(nota.getFactura().getId(), nota.getMonto());

        if (yaExiste) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una nota de crédito con ese monto para esta factura.");
        }

        // Actualiza la factura (ver CORRECCIÓN 15)
        actualizarFacturaYPedido(nota);

        return repo.save(nota);
    }

    private void actualizarFacturaYPedido(NotaCredito nota) {
        Factura factura = facturaRepository.findById(nota.getFactura().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada"));

        // Actualiza monto total acreditado
        double nuevoMonto = factura.getMontoTotalAcreditado() + nota.getMonto();
        factura.setMontoTotalAcreditado(nuevoMonto);

        if (nuevoMonto >= factura.getTotalVenta()) {
            factura.setAjustada(true); // Ya se devolvió todo
        }

        facturaRepository.save(factura);

        // Cambiar estado del Pedido
        Pedido pedido = factura.getPedido();
        if (pedido != null) {
            pedido.setEstado(Estado.CANCELADO); // o ESTADO_AJUSTADO, si tenés uno más específico
            pedidoRepository.save(pedido);
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            Articulo articulo = detalle.getArticulo();

            if (articulo instanceof ArticuloInsumo insumo) {
                insumo.setStockActual(insumo.getStockActual() + detalle.getCantidad());
                articuloInsumoRepository.save(insumo);
            }
            // Para manufacturados: stock de insumos en detalleManufacturado si querés ir más profundo
        }

    }

    public NotaCredito update(Long id, NotaCredito n) {
        n.setId(id);
        return repo.save(n);
    }
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
