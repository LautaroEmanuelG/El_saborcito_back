package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {
    private final DetallePedidoRepository repo;

    public List<DetallePedido> findAll() {
        return repo.findAll();
    }

    public DetallePedido findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetallePedido no encontrado"));
    }

    public DetallePedido save(DetallePedido detalle) {
        return repo.save(detalle);
    }

    public DetallePedido update(Long id, DetallePedido dto) {
        dto.setId(id);
        return repo.save(dto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DetallePedido no encontrado");
        repo.deleteById(id);
    }
}
