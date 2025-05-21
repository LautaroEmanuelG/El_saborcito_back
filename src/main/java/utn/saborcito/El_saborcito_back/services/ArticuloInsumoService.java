package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloInsumoService {
    private final ArticuloInsumoRepository repo;
    private final ImagenRepository imagenRepository;

    public List<ArticuloInsumo> findAll() { return repo.findAll(); }
    public ArticuloInsumo findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public ArticuloInsumo save(ArticuloInsumo insumo) {
        if (insumo.getImagen() != null && insumo.getImagen().getId_Imagen() != null) {
            Imagen img = imagenRepository.findById(insumo.getImagen().getId_Imagen())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada"));
            insumo.setImagen(img);
        }

        return repo.save(insumo);
    }
    public ArticuloInsumo update(Long id, ArticuloInsumo i) {
        i.setId_articulo(id);
        return repo.save(i);
    }
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
