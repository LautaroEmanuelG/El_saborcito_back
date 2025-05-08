package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    @GetMapping
    public List<Cliente> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public Cliente getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Cliente create(@RequestBody Cliente cliente) { return service.save(cliente); }
    @PutMapping("/{id}") public Cliente update(@PathVariable Long id, @RequestBody Cliente cliente) { return service.update(id, cliente); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
