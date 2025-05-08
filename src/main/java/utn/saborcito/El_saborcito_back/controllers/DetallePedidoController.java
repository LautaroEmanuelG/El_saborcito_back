package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.services.DetallePedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {
    private final DetallePedidoService service;

    @GetMapping
    public List<DetallePedido> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public DetallePedido getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public DetallePedido create(@RequestBody DetallePedido detalle) { return service.save(detalle); }
    @PutMapping("/{id}") public DetallePedido update(@PathVariable Long id, @RequestBody DetallePedido detalle) { return service.update(id, detalle); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
