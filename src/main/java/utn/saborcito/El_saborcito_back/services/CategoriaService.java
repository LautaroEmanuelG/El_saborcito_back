package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repo;

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public Categoria findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Categoría no encontrada con ID: " + id));
    }

    public Categoria save(Categoria categoria) {
        Optional<Categoria> existente = repo.findByDenominacionIgnoreCase(categoria.getDenominacion());
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe una categoría con la denominación: " + categoria.getDenominacion());
        }
        return repo.save(categoria);
    }

    public Categoria update(Long id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = findById(id); // findById ya lanza la excepción si no se encuentra

        // Validar que la nueva denominación no exista ya en otra categoría (excepto en
        // la actual)
        if (categoriaActualizada.getDenominacion() != null &&
                !categoriaActualizada.getDenominacion().equalsIgnoreCase(categoriaExistente.getDenominacion())) {
            Optional<Categoria> otraCategoriaConMismaDenominacion = repo
                    .findByDenominacionIgnoreCase(categoriaActualizada.getDenominacion());
            if (otraCategoriaConMismaDenominacion.isPresent()
                    && !otraCategoriaConMismaDenominacion.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe otra categoría con la denominación: " + categoriaActualizada.getDenominacion());
            }
            categoriaExistente.setDenominacion(categoriaActualizada.getDenominacion());
        }

        // Actualizar otros campos si los hubiera y fueran modificables
        // Ejemplo:
        // categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());

        return repo.save(categoriaExistente);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Categoría no encontrada con ID: " + id);
        }
        repo.deleteById(id);
    }
}