package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
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
        Pedido pedido = pedidoService.findEntityById(pedidoId); // Usar findEntityById
        return service.findByPedido(pedido);
    }

    @GetMapping("/{id}")
    public DetallePedido getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public DetallePedido create(@RequestBody DetallePedido detalle) {
        return service.save(detalle);
    }

    @PutMapping("/{id}")
    public DetallePedido update(
            @PathVariable Long id,
            @RequestBody DetallePedido detalle) {
        return service.update(id, detalle);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
