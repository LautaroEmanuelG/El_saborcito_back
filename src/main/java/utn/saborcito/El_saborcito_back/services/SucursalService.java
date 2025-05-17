package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;

import java.util.List;
@RequiredArgsConstructor

@Service
public class SucursalService {
    private final SucursalRepository repo;
    private final DomicilioRepository domicilioRepository;


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
        if (dto.getDomicilio() != null && dto.getDomicilio().getId() != null) {
            Domicilio domicilioExistente = domicilioRepository.findById(dto.getDomicilio().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Domicilio no encontrado"));
            dto.setDomicilio(domicilioExistente);
        }

        Sucursal existing = findById(id);
        existing.setNombre(dto.getNombre());
        existing.setHorarios(dto.getHorarios());
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