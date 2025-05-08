package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;

import java.util.List;

@Service
public class DomicilioService {
    private final DomicilioRepository repo;

    public DomicilioService(DomicilioRepository repo) {
        this.repo = repo;
    }

    public List<Domicilio> findAll() {
        return repo.findAll();
    }

    public Domicilio findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Domicilio no encontrado con id " + id));
    }

    public Domicilio create(Domicilio dom) {
        return repo.save(dom);
    }

    public Domicilio update(Long id, Domicilio dto) {
        Domicilio existing = findById(id);
        existing.setCalle(dto.getCalle());
        existing.setNumero(dto.getNumero());
        existing.setCp(dto.getCp());
        existing.setLocalidad(dto.getLocalidad());
        existing.setUsuario(dto.getUsuario());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Domicilio no encontrado con id " + id);
        repo.deleteById(id);
    }
}