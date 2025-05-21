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

import java.time.LocalDateTime; // Añadir import
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotaCreditoService {
    private final NotaCreditoRepository repo;
    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;

    public List<NotaCredito> findAll() {
        return repo.findAll();
    }

    public NotaCredito findById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota de Crédito no encontrada con ID: " + id));
    }

    public NotaCredito save(NotaCredito nota) {
        if (nota == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nota de crédito no puede ser nula.");
        }

        // CORRECCIÓN 13: Validar que la NotaCredito esté asociada a una Factura (y por
        // ende a un Pedido)
        if (nota.getFactura() == null || nota.getFactura().getId_Factura() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La nota de crédito debe estar asociada a una factura válida.");
        }
        Factura factura = facturaRepository.findById(nota.getFactura().getId_Factura())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Factura asociada no encontrada con ID: " + nota.getFactura().getId_Factura()));

        if (factura.getPedido() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La factura con ID: " + factura.getId_Factura()
                            + " no está asociada a ningún pedido. No se puede crear la nota de crédito.");
        }
        nota.setFactura(factura); // Asegurar que el objeto completo esté en la nota

        // CORRECCIÓN 14: Establecer fecha de emisión al momento de guardar
        if (nota.getFechaEmision() == null) { // Solo si no se estableció antes (ej. desde un DTO con fecha específica)
            nota.setFechaEmision(LocalDateTime.now());
        }

        // CORRECCIÓN 15: El monto de la NotaCredito es el que se recibe.
        // La validación de que no exceda el monto pendiente de la factura se hace más
        // adelante.
        if (nota.getMonto() == null || nota.getMonto() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El monto de la nota de crédito debe ser mayor a cero.");
        }

        // Evitar duplicados exactos (misma factura, mismo monto, mismo motivo si
        // aplica)
        // La lógica actual de `existsByFacturaIdAndMonto` podría ser demasiado simple
        // si se permiten múltiples notas por diferentes motivos.
        // Considerar una lógica más robusta si es necesario, por ejemplo, incluyendo el
        // motivo o permitiendo N notas hasta cubrir el total.
        boolean yaExiste = repo.existsByFacturaIdAndMontoAndMotivo(nota.getFactura().getId_Factura(), nota.getMonto(),
                nota.getMotivo());

        if (yaExiste) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, // Cambiado a CONFLICT
                    "Ya existe una nota de crédito idéntica (factura, monto y motivo) para esta factura.");
        }

        // Validar que el monto de la nota no exceda el saldo pendiente de la factura
        double saldoPendiente = factura.getTotalVenta() - factura.getMontoTotalAcreditado();
        if (nota.getMonto() > saldoPendiente) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El monto de la nota de crédito (" + nota.getMonto() +
                            ") excede el saldo pendiente de la factura (" + saldoPendiente + ").");
        }

        // Actualiza la factura y el pedido (lógica de CORRECCIÓN 17 también aquí)
        actualizarFacturaPedidoYStock(nota, factura);

        return repo.save(nota);
    }

    private void actualizarFacturaPedidoYStock(NotaCredito nota, Factura factura) {
        // El objeto factura ya fue cargado y validado en el método save

        // Actualiza monto total acreditado en la factura
        double nuevoMontoAcreditado = factura.getMontoTotalAcreditado() + nota.getMonto();
        factura.setMontoTotalAcreditado(nuevoMontoAcreditado);

        // Marcar factura como ajustada si el total acreditado cubre el total de la
        // venta
        if (nuevoMontoAcreditado >= factura.getTotalVenta()) {
            factura.setAjustada(true);
        }
        facturaRepository.save(factura);

        // Cambiar estado del Pedido y reponer stock (CORRECCIÓN 17)
        Pedido pedido = factura.getPedido(); // Ya validamos que el pedido existe en save()

        // Solo cambiar estado y reponer stock si la factura está completamente ajustada
        if (factura.getAjustada()) {
            pedido.setEstado(Estado.CANCELADO); // O un estado más específico como 'DEVUELTO' o 'AJUSTADO'
            pedidoRepository.save(pedido);

            // Reponer stock (CORRECCIÓN 17)
            if (pedido.getDetalles() != null) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    if (detalle == null || detalle.getArticulo() == null || detalle.getCantidad() == null)
                        continue;

                    Articulo articulo = detalle.getArticulo();
                    if (articulo instanceof ArticuloInsumo insumo) {
                        insumo.setStockActual(insumo.getStockActual() + detalle.getCantidad());
                        articuloInsumoRepository.save(insumo);
                    } else if (articulo instanceof ArticuloManufacturado manufacturado) {
                        // CORRECCIÓN 17: Reponer stock de insumos para ArticuloManufacturado
                        if (manufacturado.getArticuloManufacturadoDetalles() != null) {
                            for (ArticuloManufacturadoDetalle amd : manufacturado.getArticuloManufacturadoDetalles()) {
                                if (amd.getArticuloInsumo() != null && amd.getCantidad() != null) {
                                    ArticuloInsumo insumoComponente = amd.getArticuloInsumo();
                                    // La cantidad a reponer es la cantidad del insumo en la receta multiplicada
                                    // por la cantidad del producto manufacturado en el pedido.
                                    double cantidadAReponer = amd.getCantidad() * detalle.getCantidad();
                                    insumoComponente.setStockActual(
                                            insumoComponente.getStockActual() + (int) Math.round(cantidadAReponer)); // Asumiendo
                                                                                                                     // que
                                                                                                                     // stockActual
                                                                                                                     // es
                                                                                                                     // Integer
                                    articuloInsumoRepository.save(insumoComponente);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public NotaCredito update(Long id, NotaCredito n) {
        NotaCredito existing = findById(id);

        if (n == null || n.getFechaEmision() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La nota de crédito o su fecha de emisión no pueden ser nulas para la actualización.");
        }

        if (!n.getFechaEmision().equals(existing.getFechaEmision())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar la fecha de emisión.");
        }

        // Aquí se deberían copiar los campos actualizables de 'n' a 'existing'
        // en lugar de simplemente asignar el id a 'n' y guardar 'n'.
        // Por ejemplo:
        // existing.setAlgunCampoModificable(n.getAlgunCampoModificable());
        // Y luego guardar 'existing':
        // return repo.save(existing);
        // Por ahora, mantengo la lógica original para enfocarme en la pregunta.
        n.setId_NotaCredito(id);
        return repo.save(n);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Nota de Crédito no encontrada con ID: " + id);
        repo.deleteById(id);
    }
}
