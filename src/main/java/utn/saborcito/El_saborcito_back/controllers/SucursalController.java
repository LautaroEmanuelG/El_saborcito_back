package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dtos.SucursalDTO;
import utn.saborcito.El_saborcito_back.services.SucursalService;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {
    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public List<SucursalDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SucursalDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public SucursalDTO create(@RequestBody SucursalDTO sucursalDTO) {
        return service.create(sucursalDTO);
    }

    @PutMapping("/{id}")
    public SucursalDTO update(@PathVariable Long id, @RequestBody SucursalDTO sucursalDTO) {
        return service.update(id, sucursalDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
