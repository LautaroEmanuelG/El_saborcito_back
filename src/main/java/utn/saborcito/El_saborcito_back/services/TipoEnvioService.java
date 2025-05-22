package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.TipoEnvio;
// import utn.saborcito.El_saborcito_back.enums.TipoEnvio;
import utn.saborcito.El_saborcito_back.repositories.TipoEnvioRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoEnvioService {

    private final TipoEnvioRepository repo;

    /**
     * Inicializa la tabla de tipos de envío con los valores del enum TipoEnvio
     */
    @PostConstruct
    public void init() {
        // Solo inicializar si la tabla está vacía
        if (repo.count() == 0) {
            List<String> nombres = Arrays.asList("DELIVERY", "TAKE_AWAY"); // Agrega aquí los nombres que correspondan
            nombres.forEach(nombre -> {
                TipoEnvio tipoEnvio = new TipoEnvio(nombre);
                repo.save(tipoEnvio);
            });
        }
    }

    public List<TipoEnvio> findAll() {
        return repo.findAll();
    }

    public TipoEnvio findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "TipoEnvio no encontrado con ID: " + id));
    }

    public TipoEnvio findByNombre(String nombre) {
        return repo.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "TipoEnvio no encontrado con nombre: " + nombre));
    }

    public TipoEnvio save(TipoEnvio tipoEnvio) {
        // Validar que no exista otro con el mismo nombre
        Optional<TipoEnvio> existente = repo.findByNombre(tipoEnvio.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(tipoEnvio.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un tipo de envío con el nombre: " + tipoEnvio.getNombre());
        }

        return repo.save(tipoEnvio);
    }

    public TipoEnvio update(Long id, TipoEnvio tipoEnvio) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: TipoEnvio no encontrado con ID: " + id);
        }

        tipoEnvio.setId(id);
        return save(tipoEnvio);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: TipoEnvio no encontrado con ID: " + id);
        }

        repo.deleteById(id);
    }
}
