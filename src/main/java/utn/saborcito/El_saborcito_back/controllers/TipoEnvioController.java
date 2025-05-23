package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.TipoEnvio;
import utn.saborcito.El_saborcito_back.services.TipoEnvioService;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-envio")
@RequiredArgsConstructor
public class TipoEnvioController {

    private final TipoEnvioService service;

    @GetMapping
    public List<TipoEnvio> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TipoEnvio getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/nombre/{nombre}")
    public TipoEnvio getByNombre(@PathVariable String nombre) {
        return service.findByNombre(nombre);
    }

    @PostMapping
    public TipoEnvio create(@RequestBody TipoEnvio tipoEnvio) {
        return service.save(tipoEnvio);
    }

    @PutMapping("/{id}")
    public TipoEnvio update(@PathVariable Long id, @RequestBody TipoEnvio tipoEnvio) {
        return service.update(id, tipoEnvio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
