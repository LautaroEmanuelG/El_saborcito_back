package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.DetalleFacturaRenderizadoDTO;
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
    private final FacturaRenderService facturaRenderService;


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

    // NUEVO: Buscar factura por pedido ID
    public FacturaDTO findByPedidoId(Long pedidoId) {
        Factura factura = facturaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada para el pedido con id: " + pedidoId));
        return facturaMapper.toDTO(factura);
    }

    public Factura findEntityById(Long id) {
        // Usar fetch join para traer la factura con pedido y detalles inicializados
        return facturaRepository.findByIdWithPedidoAndDetalles(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con id: " + id));
    }

    public byte[] regenerarPDF(Long facturaId) throws IOException {
        Factura factura = findEntityById(facturaId);
        facturaRenderService.cargarDetallesPedido(factura.getPedido());

        List<DetalleFacturaRenderizadoDTO> renderItems = facturaRenderService.construirDetalleFactura(factura.getPedido());
        return facturaPdfGenerator.generarFacturaPdf(factura, renderItems);
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
        }

        if (dto.getFormaPago() != null && dto.getFormaPago().getId() != null) {
            FormaPago formaPago = formaPagoRepository.findById(dto.getFormaPago().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de Pago no encontrada"));
            factura.setFormaPago(formaPago);
        }

        Factura saved = facturaRepository.save(factura);

        facturaRenderService.cargarDetallesPedido(saved.getPedido());
        List<DetalleFacturaRenderizadoDTO> renderItems = facturaRenderService.construirDetalleFactura(saved.getPedido());
        byte[] pdfBytes = facturaPdfGenerator.generarFacturaPdf(saved, renderItems);

        String emailCliente = (dto.getClienteEmail() != null && !dto.getClienteEmail().isEmpty())
                ? dto.getClienteEmail()
                : saved.getPedido().getCliente().getEmail();

        emailService.enviarFacturaPorEmail(emailCliente, pdfBytes, "factura_" + saved.getId() + ".pdf");

        return facturaMapper.toDTO(saved);
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

    // NUEVO: Reenviar factura por email
    public void reenviarFacturaPorEmail(Long facturaId) throws IOException {
        Factura factura = findEntityById(facturaId);
        facturaRenderService.cargarDetallesPedido(factura.getPedido());
        List<DetalleFacturaRenderizadoDTO> renderItems = facturaRenderService.construirDetalleFactura(factura.getPedido());
        byte[] pdfBytes = facturaPdfGenerator.generarFacturaPdf(factura, renderItems);
        String emailCliente = factura.getPedido().getCliente().getEmail();
        emailService.enviarFacturaPorEmail(emailCliente, pdfBytes, "factura_" + factura.getId() + ".pdf");
    }
}