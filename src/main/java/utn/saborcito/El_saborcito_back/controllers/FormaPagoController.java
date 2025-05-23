package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.FormaPago;
import utn.saborcito.El_saborcito_back.services.FormaPagoService;

import java.util.List;

@RestController
@RequestMapping("/api/formas-pago")
@RequiredArgsConstructor
public class FormaPagoController {

    private final FormaPagoService service;

    @GetMapping
    public List<FormaPago> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public FormaPago getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/nombre/{nombre}")
    public FormaPago getByNombre(@PathVariable String nombre) {
        return service.findByNombre(nombre);
    }

    @PostMapping
    public FormaPago create(@RequestBody FormaPago formaPago) {
        return service.save(formaPago);
    }

    @PutMapping("/{id}")
    public FormaPago update(@PathVariable Long id, @RequestBody FormaPago formaPago) {
        return service.update(id, formaPago);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
