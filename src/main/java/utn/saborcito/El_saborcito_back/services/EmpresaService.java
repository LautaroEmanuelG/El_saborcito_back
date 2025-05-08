package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Empresa;
import utn.saborcito.El_saborcito_back.repositories.EmpresaRepository;

import java.util.List;

@Service
public class EmpresaService {
    private final EmpresaRepository repo;

    public EmpresaService(EmpresaRepository repo) {
        this.repo = repo;
    }

    public List<Empresa> findAll() {
        return repo.findAll();
    }

    public Empresa findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada con id " + id));
    }

    public Empresa create(Empresa emp) {
        return repo.save(emp);
    }

    public Empresa update(Long id, Empresa dto) {
        Empresa existing = findById(id);
        existing.setNombre(dto.getNombre());
        existing.setRazonSocial(dto.getRazonSocial());
        existing.setCuil(dto.getCuil());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada con id " + id);
        repo.deleteById(id);
    }
}