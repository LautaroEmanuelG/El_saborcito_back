package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO;
import utn.saborcito.El_saborcito_back.services.PromocionService;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {
    private final PromocionService service;

    @GetMapping
    public List<PromocionDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PromocionDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromocionDTO create(@RequestBody PromocionDTO promocionDTO) {
        return service.save(promocionDTO);
    }

    @PutMapping("/{id}")
    public PromocionDTO update(@PathVariable Long id, @RequestBody PromocionDTO promocionDTO) {
        return service.update(id, promocionDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
