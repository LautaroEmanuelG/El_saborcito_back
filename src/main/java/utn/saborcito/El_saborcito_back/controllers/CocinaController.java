package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.services.CocinaService;
import utn.saborcito.El_saborcito_back.services.PedidoCocinaService;

import java.util.List;

@RestController
@RequestMapping("/api/cocina")
@RequiredArgsConstructor
public class CocinaController {

    private final CocinaService cocinaService;
    private final PedidoCocinaService pedidoCocinaService;

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoDTO>> getPedidosEnCocina() {
        List<PedidoDTO> pedidos = cocinaService.obtenerPedidosParaCocina();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/pedidos/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        PedidoDTO actualizado = cocinaService.actualizarEstadoPedido(id, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }

    // ENDPOINTS DE PEDIDO COCINA
    @PutMapping("/pedidos/{id}/estado-cocina")
    public ResponseEntity<PedidoDTO> cambiarEstadoCocina(@PathVariable Long id, @RequestParam Long nuevoEstadoId) {
        return ResponseEntity.ok(pedidoCocinaService.avanzarEstadoCocina(id, nuevoEstadoId));
    }

    @GetMapping("/pedidos/activos")
    public ResponseEntity<List<PedidoDTO>> getPedidosActivosCocina() {
        return ResponseEntity.ok(pedidoCocinaService.obtenerPedidosActivosEnCocina());
    }

    @PutMapping("/pedidos/{id}/avanzar")
    public ResponseEntity<PedidoDTO> avanzarEstado(@PathVariable Long id) {
        PedidoDTO pedidoActualizado = pedidoCocinaService.avanzarEstadoPedido(id);
        return ResponseEntity.ok(pedidoActualizado);
    }

    @PutMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<PedidoDTO> cancelarPedido(@PathVariable Long id) {
        PedidoDTO pedidoCancelado = cocinaService.cancelarPedido(id);
        return ResponseEntity.ok(pedidoCancelado);
    }
}
