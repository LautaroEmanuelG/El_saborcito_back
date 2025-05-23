package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO;
import utn.saborcito.El_saborcito_back.services.DetallePedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @GetMapping
    public ResponseEntity<List<DetallePedidoDTO>> getAll() {
        List<DetallePedidoDTO> detallesDTO = detallePedidoService.findAll();
        return ResponseEntity.ok(detallesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoDTO> getDetallePedidoById(@PathVariable Long id) {
        DetallePedidoDTO detalleDTO = detallePedidoService.findById(id);
        return ResponseEntity.ok(detalleDTO);
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<DetallePedidoDTO> createDetallePedido(@PathVariable Long pedidoId, @RequestBody DetallePedidoDTO detallePedidoDTO) {
        DetallePedidoDTO nuevoDetalleDTO = detallePedidoService.save(detallePedidoDTO, pedidoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalleDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoDTO> updateDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoDTO detallePedidoDTO) {
        DetallePedidoDTO detalleActualizadoDTO = detallePedidoService.update(id, detallePedidoDTO);
        return ResponseEntity.ok(detalleActualizadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable Long id) {
        detallePedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
