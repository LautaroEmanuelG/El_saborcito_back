package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.CompraInsumoDTO;
import utn.saborcito.El_saborcito_back.services.CompraInsumoService;

import java.util.List;

@RestController
@RequestMapping("/api/compras-insumos")
@RequiredArgsConstructor
@Slf4j
public class CompraInsumoController {
    private final CompraInsumoService service;

    @GetMapping
    public List<CompraInsumoDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CompraInsumoDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompraInsumoDTO create(@RequestBody CompraInsumoDTO compraInsumoDTO) {
        return service.save(compraInsumoDTO);
    }

    @PutMapping("/{id}")
    public CompraInsumoDTO update(@PathVariable Long id, @RequestBody CompraInsumoDTO compraInsumoDTO) {
        return service.update(id, compraInsumoDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}