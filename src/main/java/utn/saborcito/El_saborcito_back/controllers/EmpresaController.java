package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Empresa;
import utn.saborcito.El_saborcito_back.services.EmpresaService;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {
    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Empresa> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Empresa getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Empresa create(@RequestBody Empresa empresa) {
        return service.create(empresa);
    }

    @PutMapping("/{id}")
    public Empresa update(@PathVariable Long id, @RequestBody Empresa empresa) {
        return service.update(id, empresa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}