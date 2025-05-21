package utn.saborcito.El_saborcito_back.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoService {

    private final ArticuloManufacturadoRepository repo;

    public List<ArticuloManufacturado> findAll() {
        return repo.findAll();
    }

    public ArticuloManufacturado findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo manufacturado no encontrado"));
    }

    public ArticuloManufacturado save(ArticuloManufacturado articulo) {
        return repo.save(articulo);
    }

    public ArticuloManufacturado update(Long id, ArticuloManufacturado dto) {
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo manufacturado no encontrado");
        }
        repo.deleteById(id);
    }
}