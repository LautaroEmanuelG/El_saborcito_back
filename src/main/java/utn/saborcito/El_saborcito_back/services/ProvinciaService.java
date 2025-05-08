package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Provincia;
import utn.saborcito.El_saborcito_back.repositories.ProvinciaRepository;

import java.util.List;

@Service
public class ProvinciaService {
    private final ProvinciaRepository repo;

    public ProvinciaService(ProvinciaRepository repo) {
        this.repo = repo;
    }

    public List<Provincia> findAll() {
        return repo.findAll();
    }

    public Provincia findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provincia no encontrada con id " + id));
    }

    public Provincia create(Provincia provincia) {
        return repo.save(provincia);
    }

    public Provincia update(Long id, Provincia dto) {
        Provincia existing = findById(id);
        existing.setNombre(dto.getNombre());
        existing.setPais(dto.getPais());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provincia no encontrada con id " + id);
        repo.deleteById(id);
    }
}