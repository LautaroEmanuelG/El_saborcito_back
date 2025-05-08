package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;

import java.util.List;

@Service
public class LocalidadService {
    private final LocalidadRepository repo;

    public LocalidadService(LocalidadRepository repo) {
        this.repo = repo;
    }

    public List<Localidad> findAll() {
        return repo.findAll();
    }

    public Localidad findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada con id " + id));
    }

    public Localidad create(Localidad loc) {
        return repo.save(loc);
    }

    public Localidad update(Long id, Localidad dto) {
        Localidad existing = findById(id);
        existing.setNombre(dto.getNombre());
        existing.setProvincia(dto.getProvincia());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada con id " + id);
        repo.deleteById(id);
    }
}