package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.ClienteDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.mappers.ClienteMapper;
import utn.saborcito.El_saborcito_back.services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;
    private final ClienteMapper mapper;

    @GetMapping
    public List<ClienteDTO> getAll() {
        return service.findAll().stream().map(mapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ClienteDTO getById(@PathVariable Long id) {
        return mapper.toDTO(service.findById(id));
    }

    @PostMapping
    public ClienteDTO create(@RequestBody ClienteDTO dto) {
        Cliente cliente = mapper.toEntity(dto);
        return mapper.toDTO(service.save(cliente));
    }

    @PutMapping("/{id}")
    public ClienteDTO update(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        Cliente cliente = mapper.toEntity(dto);
        return mapper.toDTO(service.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
