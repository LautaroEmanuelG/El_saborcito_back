package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionService {
    private final PromocionRepository repo;

    public List<Promocion> findAll() { return repo.findAll(); }
    public Promocion findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public Promocion save(Promocion p) {
        validarPromocion(p);
        return repo.save(p);
    }

    public Promocion update(Long id, Promocion p) {
        p.setId(id);
        validarPromocion(p);
        return repo.save(p);
    }

    private void validarPromocion(Promocion p) {
        boolean tieneDescuento = p.getDescuento() != null && p.getDescuento() > 0;
        boolean tienePrecioPromocional = p.getPrecioPromocional() != null && p.getPrecioPromocional() > 0;

        if (tieneDescuento && tienePrecioPromocional) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puede usar descuento y precio promocional al mismo tiempo.");
        }
    }
    public void delete(Long id) { repo.deleteById(id); }
}