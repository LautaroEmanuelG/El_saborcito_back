package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.DetallePedidoId;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.services.DetallePedidoService;
import utn.saborcito.El_saborcito_back.services.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {
    private final DetallePedidoService service;
    private final PedidoService pedidoService;

    @GetMapping
    public List<DetallePedido> getAll() {
        return service.findAll();
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<DetallePedido> getByPedido(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.findById(pedidoId);
        return service.findByPedido(pedido);
    }

    @GetMapping("/{pedidoId}/{articuloId}")
    public DetallePedido getById(@PathVariable Long pedidoId, @PathVariable Long articuloId) {
        DetallePedidoId id = new DetallePedidoId(pedidoId, articuloId);
        return service.findById(id);
    }

    @PostMapping
    public DetallePedido create(@RequestBody DetallePedido detalle) {
        return service.save(detalle);
    }

    @PutMapping("/{pedidoId}/{articuloId}")
    public DetallePedido update(
            @PathVariable Long pedidoId,
            @PathVariable Long articuloId,
            @RequestBody DetallePedido detalle) {
        return service.update(pedidoId, articuloId, detalle);
    }

    @DeleteMapping("/{pedidoId}/{articuloId}")
    public void delete(@PathVariable Long pedidoId, @PathVariable Long articuloId) {
        service.delete(pedidoId, articuloId);
    }
}
