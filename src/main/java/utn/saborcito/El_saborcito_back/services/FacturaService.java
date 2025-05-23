package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.FacturaDTO;
import utn.saborcito.El_saborcito_back.mappers.FacturaMapper;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.models.FormaPago;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.FacturaRepository;
import utn.saborcito.El_saborcito_back.repositories.FormaPagoRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;
    private final PedidoRepository pedidoRepository; // Para cargar la entidad Pedido
    private final FormaPagoRepository formaPagoRepository; // Para cargar la entidad FormaPago

    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll()
                .stream()
                .map(facturaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FacturaDTO findById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
        return facturaMapper.toDTO(factura);
    }

    public Factura findEntityById(Long id) { // Método para obtener la entidad
        return facturaRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
    }

    public FacturaDTO save(FacturaDTO dto) {
        Factura factura = facturaMapper.toEntity(dto);

        // Asegurarse de que la fecha de facturación se establezca si es nula
        if (factura.getFechaFacturacion() == null) {
            factura.setFechaFacturacion(java.time.LocalDate.now());
        }

        // Cargar y asignar el Pedido si se proporciona un ID
        if (dto.getPedido() != null && dto.getPedido().getId() != null) {
            Pedido pedido = pedidoRepository.findById(dto.getPedido().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Pedido no encontrado con id: " + dto.getPedido().getId()));
            factura.setPedido(pedido);
        } else if (dto.getPedido() != null) {
            // Si se proporciona un PedidoDTO pero sin ID, o se quiere crear uno nuevo junto
            // con la factura
            // Aquí se necesitaría lógica adicional, por ahora asumimos que el Pedido debe
            // existir
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID de Pedido es requerido para asociar a la Factura.");
        }

        // Cargar y asignar la FormaPago si se proporciona un ID
        if (dto.getFormaPago() != null && dto.getFormaPago().getId() != null) {
            FormaPago formaPago = formaPagoRepository.findById(dto.getFormaPago().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Forma de Pago no encontrada con id: " + dto.getFormaPago().getId()));
            factura.setFormaPago(formaPago);
        } else if (dto.getFormaPago() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID de Forma de Pago es requerido para asociar a la Factura.");
        }

        Factura savedFactura = facturaRepository.save(factura);
        return facturaMapper.toDTO(savedFactura);
    }

    public FacturaDTO update(Long id, FacturaDTO dto) {
        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));

        // No se puede modificar la fecha de facturación.
        if (dto.getFechaFacturacion() != null &&
                !dto.getFechaFacturacion().equals(facturaExistente.getFechaFacturacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar la fecha de facturación.");
        }

        // Actualizar campos permitidos de Mercado Pago
        if (dto.getMpPaymentId() != null) {
            facturaExistente.setMpPaymentId(dto.getMpPaymentId());
        }
        if (dto.getMpMerchantOrderId() != null) {
            facturaExistente.setMpMerchantOrderId(dto.getMpMerchantOrderId());
        }
        if (dto.getMpPreferenceId() != null) {
            facturaExistente.setMpPreferenceId(dto.getMpPreferenceId());
        }
        if (dto.getMpPaymentType() != null) {
            facturaExistente.setMpPaymentType(dto.getMpPaymentType());
        }

        // montoTotalAcreditado y ajustada no se actualizan directamente aquí
        // Se asume que se manejan por otros servicios o procesos (ej. Nota de Crédito)

        // Campos como pedido, formaPago, totalVenta no deberían cambiar post-creación.
        // Validar si se intentan modificar y lanzar error o ignorar.
        if (dto.getPedido() != null && dto.getPedido().getId() != null &&
                (facturaExistente.getPedido() == null
                        || !dto.getPedido().getId().equals(facturaExistente.getPedido().getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede modificar el pedido asociado a una factura existente.");
        }

        if (dto.getFormaPago() != null && dto.getFormaPago().getId() != null &&
                (facturaExistente.getFormaPago() == null
                        || !dto.getFormaPago().getId().equals(facturaExistente.getFormaPago().getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede modificar la forma de pago de una factura existente.");
        }

        if (dto.getTotalVenta() != null &&
                !dto.getTotalVenta().equals(facturaExistente.getTotalVenta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede modificar el total de venta de una factura existente.");
        }

        Factura updatedFactura = facturaRepository.save(facturaExistente);
        return facturaMapper.toDTO(updatedFactura);
    }

    public void delete(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id);
        }
        facturaRepository.deleteById(id);
    }
}
