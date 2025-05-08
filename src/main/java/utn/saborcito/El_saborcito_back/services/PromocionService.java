package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionService {
    private final PromocionRepository repo;

    public List<Promocion> findAll() { return repo.findAll(); }
    public Promocion findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public Promocion save(Promocion p) { return repo.save(p); }
    public Promocion update(Long id, Promocion p) {
        Promocion existing = findById(id);
        p.setId(id);
        return repo.save(p);
    }
    public void delete(Long id) { repo.deleteById(id); }
}