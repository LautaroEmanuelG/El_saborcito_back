package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.DatosMercadoPagoDto;
import utn.saborcito.El_saborcito_back.mappers.DatosMercadoPagoMapper;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
import utn.saborcito.El_saborcito_back.services.DatosMercadoPagoService;

import java.util.List;

@RestController
@RequestMapping("/api/datos-mercadopago")
@RequiredArgsConstructor
public class DatosMercadoPagoController {
    private final DatosMercadoPagoService service;
    private final DatosMercadoPagoMapper mapper;

    @GetMapping
    public List<DatosMercadoPagoDto> getAll() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public DatosMercadoPagoDto getById(@PathVariable Long id) {
        return mapper.toDto(service.findById(id));
    }

    @PostMapping
    public DatosMercadoPagoDto create(@RequestBody DatosMercadoPagoDto dto) {
        DatosMercadoPago dmp = mapper.toEntity(dto);
        return mapper.toDto(service.save(dmp));
    }

    @PutMapping("/{id}")
    public DatosMercadoPagoDto update(@PathVariable Long id, @RequestBody DatosMercadoPagoDto dto) {
        DatosMercadoPago dmp = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, dmp));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
