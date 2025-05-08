package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.NotaCredito;
import utn.saborcito.El_saborcito_back.repositories.NotaCreditoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotaCreditoService {
    private final NotaCreditoRepository repo;

    public List<NotaCredito> findAll() { return repo.findAll(); }
    public NotaCredito findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public NotaCredito save(NotaCredito n) { return repo.save(n); }
    public NotaCredito update(Long id, NotaCredito n) {
        n.setId(id);
        return repo.save(n);
    }
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
