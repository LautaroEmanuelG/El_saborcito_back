package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.mappers.PedidoMapper;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repo;
    private final CalculadoraPedidoService calculadoraService;
    private final EstadoService estadoService;
    private final TipoEnvioService tipoEnvioService;
    private final FormaPagoService formaPagoService;
    private final PedidoMapper pedidoMapper; // Inyectar el mapper

    // Estados constantes para evitar hardcoding
    private static final String ESTADO_CANCELADO = "CANCELADO";
    private static final String ESTADO_LISTO = "LISTO";

    public List<PedidoDTO> findAll() {
        return repo.findAll().stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> findByClienteId(Long clienteId) {
        return repo.findByClienteId(clienteId).stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO findById(Long id) {
        Pedido pedido = repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));
        return pedidoMapper.toDTO(pedido);
    }

    // Nuevo método para obtener la entidad Pedido
    public Pedido findEntityById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));
    }

    public PedidoDTO save(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        } // Asegurar que los estados, tipos de envío y formas de pago existan
        validarReferencias(pedido);

        // 🆕 Establecer estado automático si todos los artículos son insumos
        establecerEstadoAutomatico(pedido);

        // Usar el servicio de calculadora para actualizar los totales (incluye
        // promociones, descuentos y hora estimada)
        calculadoraService.actualizarTotalesPedido(pedido);
        return pedidoMapper.toDTO(repo.save(pedido));
    }

    public PedidoDTO update(Long id, Pedido pedido) {
        Pedido existing = repo.findById(id).orElseThrow( // Simplificado, ya que findById del repo lanza la excepción si
                                                         // no se encuentra
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));

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

        // 🆕 Establecer estado automático si todos los artículos son insumos y no tiene
        // estado
        establecerEstadoAutomatico(existing);

        // Usar el servicio de calculadora para actualizar los totales (incluye
        // promociones, descuentos y hora estimada)
        calculadoraService.actualizarTotalesPedido(existing);

        return pedidoMapper.toDTO(repo.save(existing));
    }

    /**
     * Recalcula el total de un pedido existente y lo guarda.
     * Útil para actualizar el total después de cambios en los detalles.
     *
     * @param id El ID del pedido a recalcular
     * @return El pedido actualizado como DTO
     */
    public PedidoDTO recalcularTotal(Long id) {
        Pedido pedido = repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));
        calculadoraService.actualizarTotalesPedido(pedido);
        return pedidoMapper.toDTO(repo.save(pedido));
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

    /**
     * 🆕 Establece automáticamente el estado del pedido basado en sus artículos
     */
    private void establecerEstadoAutomatico(Pedido pedido) {
        // Solo aplicar si no tiene estado definido
        if (pedido.getEstado() == null) {
            // Si todos los artículos son insumos, establecer estado LISTO
            if (calculadoraService.esPedidoSoloInsumos(pedido)) {
                try {
                    Estado estadoListo = estadoService.findByNombre(ESTADO_LISTO);
                    pedido.setEstado(estadoListo);
                } catch (ResponseStatusException e) {
                    // Si no existe el estado LISTO, continuar sin establecer estado
                    System.err.println("Estado LISTO no encontrado: " + e.getMessage());
                }
            }
        }
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
    public PedidoDTO cancelarPedido(Long id, String motivo) {
        Pedido pedido = repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));

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

        return pedidoMapper.toDTO(pedidoCancelado);
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
        if (pedido.getCliente() != null && pedido.getCliente() != null) { // lo cambie porque cliente ya no tiene un
                                                                          // usuario
            // Aquí se implementaría el código para enviar una notificación al cliente
            // Por ejemplo, usando un servicio de email, SMS, o notificaciones push

            // Para propósitos de desarrollo, sólo imprimimos un mensaje
            System.out.println("NOTIFICACIÓN: Se ha cancelado el pedido #" + pedido.getId() +
                    " para el cliente " + pedido.getCliente().getNombre() + " " +
                    pedido.getCliente().getApellido() + ". Motivo: " +
                    (motivo != null ? motivo : "No especificado"));
        }
    }
}
