package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.services.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService service;

    @GetMapping
    public List<PedidoDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PedidoDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PedidoDTO create(@RequestBody Pedido pedido) {
        return service.save(pedido);
    }

    @PutMapping("/{id}")
    public PedidoDTO update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.update(id, pedido);
    }

    @PutMapping("/{id}/recalcular")
    public PedidoDTO recalcularTotal(@PathVariable Long id) {
        return service.recalcularTotal(id);
    }

    /**
     * Endpoint para cancelar un pedido
     * 
     * @param id     ID del pedido a cancelar
     * @param motivo Razón de la cancelación (opcional)
     * @return El pedido cancelado como DTO
     */
    @PutMapping("/{id}/cancelar")
    public PedidoDTO cancelarPedido(@PathVariable Long id, @RequestParam(required = false) String motivo) {
        return service.cancelarPedido(id, motivo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
