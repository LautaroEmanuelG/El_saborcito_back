package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private final ImagenRepository repo;

    public List<Imagen> findAll() {
        return repo.findAll();
    }

    public Imagen findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada"));
    }

    public Imagen save(Imagen imagen) {
        return repo.save(imagen);
    }

    public Imagen update(Long id, Imagen dto) {
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada");
        }
        repo.deleteById(id);
    }
}