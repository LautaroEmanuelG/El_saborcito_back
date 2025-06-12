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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;
    private final PedidoRepository pedidoRepository;
    private final FormaPagoRepository formaPagoRepository;
    private final FacturaPdfGeneratorService facturaPdfGenerator;
    private final EmailService emailService;

    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll()
                .stream()
                .map(facturaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FacturaDTO findById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
        return facturaMapper.toDTO(factura);
    }

    public Factura findEntityById(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
    }

    public FacturaDTO save(FacturaDTO dto) throws IOException {
        Factura factura = facturaMapper.toEntity(dto);

        if (factura.getFechaFacturacion() == null) {
            factura.setFechaFacturacion(java.time.LocalDate.now());
        }

        if (dto.getPedido() != null && dto.getPedido().getId() != null) {
            Pedido pedido = pedidoRepository.findById(dto.getPedido().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con id: " + dto.getPedido().getId()));
            factura.setPedido(pedido);
        } else if (dto.getPedido() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de Pedido es requerido para asociar a la Factura.");
        }

        if (dto.getFormaPago() != null && dto.getFormaPago().getId() != null) {
            FormaPago formaPago = formaPagoRepository.findById(dto.getFormaPago().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de Pago no encontrada con id: " + dto.getFormaPago().getId()));
            factura.setFormaPago(formaPago);
        } else if (dto.getFormaPago() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de Forma de Pago es requerido para asociar a la Factura.");
        }

        Factura savedFactura = facturaRepository.save(factura);

        // Generar PDF y enviar email
        byte[] pdfBytes = facturaPdfGenerator.generarFacturaPdf(savedFactura);
        String emailCliente = (dto.getClienteEmail() != null && !dto.getClienteEmail().isEmpty())
                ? dto.getClienteEmail()
                : savedFactura.getPedido().getCliente().getEmail();
        emailService.enviarFacturaPorEmail(emailCliente, pdfBytes, "factura_" + savedFactura.getId() + ".pdf");
        return facturaMapper.toDTO(savedFactura);
    }

    public FacturaDTO update(Long id, FacturaDTO dto) {
        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));

        if (dto.getFechaFacturacion() != null && !dto.getFechaFacturacion().equals(facturaExistente.getFechaFacturacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar la fecha de facturación.");
        }

        if (dto.getMpPaymentId() != null) facturaExistente.setMpPaymentId(dto.getMpPaymentId());
        if (dto.getMpMerchantOrderId() != null) facturaExistente.setMpMerchantOrderId(dto.getMpMerchantOrderId());
        if (dto.getMpPreferenceId() != null) facturaExistente.setMpPreferenceId(dto.getMpPreferenceId());
        if (dto.getMpPaymentType() != null) facturaExistente.setMpPaymentType(dto.getMpPaymentType());

        if (dto.getPedido() != null && dto.getPedido().getId() != null &&
                !dto.getPedido().getId().equals(facturaExistente.getPedido().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar el pedido asociado.");
        }

        if (dto.getFormaPago() != null && dto.getFormaPago().getId() != null &&
                !dto.getFormaPago().getId().equals(facturaExistente.getFormaPago().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar la forma de pago.");
        }

        if (dto.getTotalVenta() != null && !dto.getTotalVenta().equals(facturaExistente.getTotalVenta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar el total de venta.");
        }

        return facturaMapper.toDTO(facturaRepository.save(facturaExistente));
    }

    public void delete(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id);
        }
        facturaRepository.deleteById(id);
    }
}
