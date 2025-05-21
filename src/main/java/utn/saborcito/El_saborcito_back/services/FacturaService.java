package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.repositories.FacturaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private final FacturaRepository repo;

    public List<Factura> findAll() {
        return repo.findAll();
    }

    public Factura findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Factura save(Factura f) {
        // Asegurarse de que la fecha de facturación se establezca si es nula (por
        // ejemplo, al crear)
        if (f.getFechaFacturacion() == null) {
            f.setFechaFacturacion(java.time.LocalDate.now());
        }
        return repo.save(f);
    }

    public Factura update(Long id, Factura facturaActualizada) {
        Factura facturaExistente = findById(id);

        // CORRECCIÓN 18: No se puede modificar la fecha de facturación.
        if (facturaActualizada.getFechaFacturacion() != null &&
                !facturaActualizada.getFechaFacturacion().equals(facturaExistente.getFechaFacturacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar la fecha de facturación.");
        }

        // Actualizar campos permitidos
        // Los campos relacionados con Mercado Pago se actualizan si vienen en la
        // petición
        if (facturaActualizada.getMpPaymentId() != null) {
            facturaExistente.setMpPaymentId(facturaActualizada.getMpPaymentId());
        }
        if (facturaActualizada.getMpMerchantOrderId() != null) {
            facturaExistente.setMpMerchantOrderId(facturaActualizada.getMpMerchantOrderId());
        }
        if (facturaActualizada.getMpPreferenceId() != null) {
            facturaExistente.setMpPreferenceId(facturaActualizada.getMpPreferenceId());
        }
        if (facturaActualizada.getMpPaymentType() != null) {
            facturaExistente.setMpPaymentType(facturaActualizada.getMpPaymentType());
        }
        // montoTotalAcreditado y ajustada se manejan internamente por
        // NotaCreditoService o similar
        // No deberían ser directamente actualizables aquí a menos que haya un caso de
        // uso específico.
        // Si se permite actualizar, se debe validar la lógica de negocio.
        // Ejemplo:
        // facturaExistente.setMontoTotalAcreditado(facturaActualizada.getMontoTotalAcreditado());
        // Ejemplo: facturaExistente.setAjustada(facturaActualizada.getAjustada());

        // Campos como pedido, formaPago, totalVenta no deberían cambiar post-creación.
        // Si se intenta modificar alguno de ellos que no sea la fecha (ya validada),
        // se podría lanzar una excepción o simplemente ignorar el cambio.
        if (facturaActualizada.getFormaPago() != null
                && !facturaActualizada.getFormaPago().equals(facturaExistente.getFormaPago())) {
            // Considerar si esto debe ser un error o simplemente no se actualiza
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede
            // modificar la forma de pago de una factura existente.");
        }
        if (facturaActualizada.getTotalVenta() != null
                && !facturaActualizada.getTotalVenta().equals(facturaExistente.getTotalVenta())) {
            // Considerar si esto debe ser un error o simplemente no se actualiza
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede
            // modificar el total de venta de una factura existente.");
        }
        if (facturaActualizada.getPedido() != null && (facturaExistente.getPedido() == null
                || !facturaActualizada.getPedido().getId().equals(facturaExistente.getPedido().getId()))) {
            // Considerar si esto debe ser un error o simplemente no se actualiza
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede
            // modificar el pedido asociado a una factura existente.");
        }

        return repo.save(facturaExistente);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
