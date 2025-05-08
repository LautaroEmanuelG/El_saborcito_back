package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.services.EmpleadoService;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService service;

    @GetMapping
    public List<Empleado> getAll() { return service.findAll(); }
    @GetMapping("/{id}") public Empleado getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Empleado create(@RequestBody Empleado e) { return service.save(e); }
    @PutMapping("/{id}") public Empleado update(@PathVariable Long id, @RequestBody Empleado e) { return service.update(id, e); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
