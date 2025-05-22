package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
import utn.saborcito.El_saborcito_back.models.DetallePedido;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repo;
    private final CalculadoraPedidoService calculadoraService;
    private final EstadoService estadoService;
    private final TipoEnvioService tipoEnvioService;
    private final FormaPagoService formaPagoService;

    // Estados constantes para evitar hardcoding
    private static final String ESTADO_CANCELADO = "CANCELADO";

    public List<Pedido> findAll() {
        return repo.findAll();
    }

    public Pedido findById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));
    }

    public Pedido save(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }

        // Asegurar que los estados, tipos de envío y formas de pago existan
        validarReferencias(pedido);

        // Usar el servicio de calculadora para actualizar los totales
        calculadoraService.actualizarTotalesPedido(pedido);
        calcularHoraEstimada(pedido);
        return repo.save(pedido);
    }

    public Pedido update(Long id, Pedido pedido) {
        Pedido existing = findById(id); // findById ya lanza la excepción si no se encuentra

        // No permitir la modificación de fechaPedido
        // existing.setFechaPedido(pedido.getFechaPedido());

        existing.getDetalles().clear();

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(existing);
                existing.getDetalles().add(detalle);
            }
        }

        // Asegurar que los estados, tipos de envío y formas de pago existan
        validarReferencias(pedido);

        existing.setTipoEnvio(pedido.getTipoEnvio());
        existing.setFormaPago(pedido.getFormaPago());
        existing.setSucursal(pedido.getSucursal());
        existing.setCliente(pedido.getCliente());
        existing.setEstado(pedido.getEstado());

        // Usar el servicio de calculadora para actualizar los totales
        calculadoraService.actualizarTotalesPedido(existing);
        calcularHoraEstimada(existing);

        return repo.save(existing);
    }

    /**
     * Recalcula el total de un pedido existente y lo guarda.
     * Útil para actualizar el total después de cambios en los detalles.
     *
     * @param id El ID del pedido a recalcular
     * @return El pedido actualizado
     */
    public Pedido recalcularTotal(Long id) {
        Pedido pedido = findById(id);
        calculadoraService.actualizarTotalesPedido(pedido);
        return repo.save(pedido);
    }

    /**
     * Valida que las referencias a estado, tipo de envío y forma de pago existan
     * 
     * @param pedido El pedido a validar
     */
    private void validarReferencias(Pedido pedido) {
        if (pedido.getEstado() != null && pedido.getEstado().getId() != null) {
            // Verificar que exista el estado
            estadoService.findById(pedido.getEstado().getId());
        }

        if (pedido.getTipoEnvio() != null && pedido.getTipoEnvio().getId() != null) {
            // Verificar que exista el tipo de envío
            tipoEnvioService.findById(pedido.getTipoEnvio().getId());
        }

        if (pedido.getFormaPago() != null && pedido.getFormaPago().getId() != null) {
            // Verificar que exista la forma de pago
            formaPagoService.findById(pedido.getFormaPago().getId());
        }
    }

    private void calcularHoraEstimada(Pedido pedido) {
        int minutosCocina = pedido.getDetalles().stream()
                .filter(det -> det.getArticulo() instanceof ArticuloManufacturado)
                .map(det -> ((ArticuloManufacturado) det.getArticulo()).getTiempoEstimadoMinutos() * det.getCantidad())
                .reduce(0, Integer::sum);

        int minutosDelivery = pedido.getTipoEnvio() != null &&
                "DELIVERY".equals(pedido.getTipoEnvio().getNombre()) ? 30 : 0;

        LocalTime horaEstimada = LocalTime.now().plusMinutes(minutosCocina + minutosDelivery);
        pedido.setHorasEstimadaFinalizacion(horaEstimada);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Pedido no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    /**
     * Cancela un pedido cambiando su estado a CANCELADO.
     * Esta funcionalidad reemplaza el uso de NotaCredito para cancelaciones.
     * 
     * @param id     El ID del pedido a cancelar
     * @param motivo La razón de la cancelación (opcional)
     * @return El pedido cancelado
     */
    public Pedido cancelarPedido(Long id, String motivo) {
        Pedido pedido = findById(id);

        // Verificar si el pedido ya está cancelado
        if (pedido.getEstado() != null && ESTADO_CANCELADO.equals(pedido.getEstado().getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El pedido con ID: " + id + " ya se encuentra cancelado.");
        }

        // Cambiar el estado del pedido a CANCELADO
        utn.saborcito.El_saborcito_back.models.Estado estadoCancelado = estadoService.findByNombre(ESTADO_CANCELADO);
        pedido.setEstado(estadoCancelado);

        // Guardar el pedido con el nuevo estado
        Pedido pedidoCancelado = repo.save(pedido);

        // Notificar al cliente sobre la cancelación (simulado o implementación real)
        notificarClienteSobreCancelacion(pedidoCancelado, motivo);

        return pedidoCancelado;
    }

    /**
     * Método para notificar al cliente sobre la cancelación de su pedido.
     * Esta es una implementación básica que podría ser reemplazada
     * por un servicio real de notificaciones (email, SMS, etc.)
     * 
     * @param pedido El pedido cancelado
     * @param motivo La razón de la cancelación
     */
    private void notificarClienteSobreCancelacion(Pedido pedido, String motivo) {
        if (pedido.getCliente() != null && pedido.getCliente().getUsuario() != null) {
            // Aquí se implementaría el código para enviar una notificación al cliente
            // Por ejemplo, usando un servicio de email, SMS, o notificaciones push

            // Para propósitos de desarrollo, sólo imprimimos un mensaje
            System.out.println("NOTIFICACIÓN: Se ha cancelado el pedido #" + pedido.getId() +
                    " para el cliente " + pedido.getCliente().getUsuario().getNombre() + " " +
                    pedido.getCliente().getUsuario().getApellido() + ". Motivo: " +
                    (motivo != null ? motivo : "No especificado"));
        }
    }
}
