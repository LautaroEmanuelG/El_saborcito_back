package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.FacturaDTO;
import utn.saborcito.El_saborcito_back.services.FacturaService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {
    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> getAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.findById(id));
    }

    // NUEVO: Buscar factura por pedido ID
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<FacturaDTO> getByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(facturaService.findByPedidoId(pedidoId));
    }

    // NUEVO: Regenerar y descargar PDF de una factura
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPDF(@PathVariable Long id) {
        try {
            byte[] pdfBytes = facturaService.regenerarPDF(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "factura-" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // NUEVO: Reenviar factura por email
//    @PostMapping("/{id}/reenviar")
//    public ResponseEntity<String> reenviarFacturaPorEmail(@PathVariable Long id) {
//        try {
//            facturaService.reenviarFacturaPorEmail(id);
//            return ResponseEntity.ok("Factura reenviada exitosamente");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error al reenviar factura: " + e.getMessage());
//        }
//    }

    @PostMapping
    public ResponseEntity<FacturaDTO> create(@RequestBody FacturaDTO dto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> update(@PathVariable Long id, @RequestBody FacturaDTO dto) {
        return ResponseEntity.ok(facturaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}