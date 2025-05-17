package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.repositories.EmpleadoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;

    public List<Empleado> findAll() {
        return repo.findAll();
    }

    public Empleado findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con id " + id));
    }

    public Empleado save(Empleado e) {
        if (e.getSucursal() != null && e.getSucursal().getId() == 0) {
            e.setSucursal(null); // o lanzar error si no debe permitirse null
        }

        return repo.save(e);
    }

    public Empleado update(Long id, Empleado e) {
        if (e.getSucursal() != null && e.getSucursal().getId() == 0) {
            e.setSucursal(null);
        }

        e.setId(id);
        return repo.save(e);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con id " + id);
        }
        repo.deleteById(id);
    }
}
