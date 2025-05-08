package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoDetalleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoDetalleService {

    private final ArticuloManufacturadoDetalleRepository repo;

    public List<ArticuloManufacturadoDetalle> findAll() {
        return repo.findAll();
    }

    public ArticuloManufacturadoDetalle findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado"));
    }

    public ArticuloManufacturadoDetalle save(ArticuloManufacturadoDetalle detalle) {
        return repo.save(detalle);
    }

    public ArticuloManufacturadoDetalle update(Long id, ArticuloManufacturadoDetalle dto) {
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado");
        }
        repo.deleteById(id);
    }
}