package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.repositories.FacturaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private final FacturaRepository repo;

    public List<Factura> findAll() { return repo.findAll(); }
    public Factura findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public Factura save(Factura f) { return repo.save(f); }
    public Factura update(Long id, Factura f) {
        f.setId(id);
        return repo.save(f);
    }
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
