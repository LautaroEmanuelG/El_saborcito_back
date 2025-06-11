package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.dto.PedidoConRecetasDTO;
import utn.saborcito.El_saborcito_back.services.HistorialService;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final HistorialService historialService;

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoDTO>> getHistorialPedidos() {
        return ResponseEntity.ok(historialService.obtenerPedidosFinalizados());
    }

    @GetMapping("/pedidos/{id}/detalle-completo")
    public ResponseEntity<PedidoConRecetasDTO> getPedidoConRecetas(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.obtenerDetalleCompletoPedido(id));
    }
    @GetMapping("/pedidos/{id}/pdf")
    public ResponseEntity<byte[]> generarPDF(@PathVariable Long id) {
        byte[] pdfBytes = historialService.generarPdfPedido(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "pedido_" + id + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
