package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.services.CocinaService;

import java.util.List;

@RestController
@RequestMapping("/api/cocina")
@RequiredArgsConstructor
public class CocinaController {

    private final CocinaService cocinaService;

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoDTO>> getPedidosEnCocina() {
        List<PedidoDTO> pedidos = cocinaService.obtenerPedidosParaCocina();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/pedidos/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado
    ) {
        PedidoDTO actualizado = cocinaService.actualizarEstadoPedido(id, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }
}
