package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Pais;
import utn.saborcito.El_saborcito_back.repositories.PaisRepository;

import java.util.List;

@Service
public class PaisService {
    private final PaisRepository repo;

    public PaisService(PaisRepository repo) {
        this.repo = repo;
    }

    public List<Pais> findAll() {
        return repo.findAll();
    }

    public Pais findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado con id " + id));
    }

    public Pais create(Pais pais) {
        return repo.save(pais);
    }

    public Pais update(Long id, Pais dto) {
        Pais existing = findById(id);
        existing.setNombre(dto.getNombre());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado con id " + id);
        repo.deleteById(id);
    }
}