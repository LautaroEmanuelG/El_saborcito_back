package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadMedidaService {

    private final UnidadMedidaRepository repo;

    public List<UnidadMedida> findAll() {
        return repo.findAll();
    }

    public UnidadMedida findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unidad de medida no encontrada"));
    }

    public UnidadMedida save(UnidadMedida unidad) {
        return repo.save(unidad);
    }

    public UnidadMedida update(Long id, UnidadMedida dto) {
        UnidadMedida existing = findById(id);
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unidad de medida no encontrada");
        }
        repo.deleteById(id);
    }
}