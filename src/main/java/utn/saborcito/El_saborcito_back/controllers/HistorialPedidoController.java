package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.HistorialPedido;
import utn.saborcito.El_saborcito_back.services.HistorialPedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/historial-pedidos")
@RequiredArgsConstructor
public class HistorialPedidoController {

    private final HistorialPedidoService service;

    @GetMapping("/cliente/{clienteId}")
    public List<HistorialPedido> getByCliente(@PathVariable Long clienteId) {
        return service.findByCliente(clienteId);
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<HistorialPedido> getByPedido(@PathVariable Long pedidoId) {
        return service.findByPedido(pedidoId);
    }

    @PostMapping("/registrar")
    public HistorialPedido registrarPedido(
            @RequestParam Long clienteId,
            @RequestParam Long pedidoId,
            @RequestParam(required = false) String observacion) {
        return service.registrarPedido(clienteId, pedidoId, observacion);
    }

    @GetMapping("/{id}")
    public HistorialPedido getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public HistorialPedido update(@PathVariable Long id, @RequestBody HistorialPedido historial) {
        return service.update(id, historial);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
