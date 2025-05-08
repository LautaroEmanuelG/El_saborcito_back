package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.repositories.ClienteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repo;

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Cliente findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    public Cliente save(Cliente cliente) {
        return repo.save(cliente);
    }

    public Cliente update(Long id, Cliente dto) {
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        repo.deleteById(id);
    }
}
