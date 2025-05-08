package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;

import java.util.List;

@Service
public class SucursalService {
    private final SucursalRepository repo;

    public SucursalService(SucursalRepository repo) {
        this.repo = repo;
    }

    public List<Sucursal> findAll() {
        return repo.findAll();
    }

    public Sucursal findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id));
    }

    public Sucursal create(Sucursal suc) {
        return repo.save(suc);
    }

    public Sucursal update(Long id, Sucursal dto) {
        Sucursal existing = findById(id);
        existing.setNombre(dto.getNombre());
        existing.setHorarioApertura(dto.getHorarioApertura());
        existing.setHorarioCierre(dto.getHorarioCierre());
        existing.setDomicilio(dto.getDomicilio());
        existing.setEmpresa(dto.getEmpresa());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id);
        repo.deleteById(id);
    }
}