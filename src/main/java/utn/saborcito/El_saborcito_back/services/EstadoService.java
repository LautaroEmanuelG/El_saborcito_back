package utn.saborcito.El_saborcito_back.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Estado;
import utn.saborcito.El_saborcito_back.repositories.EstadoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository repo;

    /**
     * Inicializa la tabla de estados con los valores del enum Estado
     */
    @PostConstruct
    public void init() {
        // Solo inicializar si la tabla está vacía
        if (repo.count() == 0) {
            List<String> nombresEstados = Arrays.asList("A_CONFIRMAR", "EN_PREPARACION", "DEMORADO", "LISTO",
                    "EN_DELIVERY", "ENTREGADO", "CANCELADO"); // Agrega los nombres que correspondan
            nombresEstados.forEach(nombre -> {
                Estado estado = new Estado(nombre);
                repo.save(estado);
            });
        }
    }

    public List<Estado> findAll() {
        return repo.findAll();
    }

    public Estado findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estado no encontrado con ID: " + id));
    }

    public Estado findByNombre(String nombre) {
        return repo.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estado no encontrado con nombre: " + nombre));
    }

    public Estado save(Estado estado) {
        // Validar que no exista otro con el mismo nombre
        Optional<Estado> existente = repo.findByNombre(estado.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(estado.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un estado con el nombre: " + estado.getNombre());
        }

        return repo.save(estado);
    }

    public Estado update(Long id, Estado estado) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: Estado no encontrado con ID: " + id);
        }

        estado.setId(id);
        return save(estado);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Estado no encontrado con ID: " + id);
        }

        repo.deleteById(id);
    }
}
