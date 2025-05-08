package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
import utn.saborcito.El_saborcito_back.repositories.DatosMercadoPagoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatosMercadoPagoService {
    private final DatosMercadoPagoRepository repo;

    public List<DatosMercadoPago> findAll() { return repo.findAll(); }
    public DatosMercadoPago findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public DatosMercadoPago save(DatosMercadoPago dmp) { return repo.save(dmp); }
    public DatosMercadoPago update(Long id, DatosMercadoPago dmp) {
        dmp.setId(id);
        return repo.save(dmp);
    }
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
