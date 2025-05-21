package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;

import java.util.List;

@RequiredArgsConstructor

@Service
public class SucursalService {
    private final SucursalRepository repo;
    private final DomicilioRepository domicilioRepository;

    public List<Sucursal> findAll() {
        return repo.findAll();
    }

    public Sucursal findById(Long id) {
        return repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id));
    }

    public Sucursal create(Sucursal suc) {
        if (suc.getDomicilio() != null && suc.getDomicilio().getId_Domicilio() != null) {
            Domicilio domicilioExistente = domicilioRepository.findById(suc.getDomicilio().getId_Domicilio())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Domicilio proporcionado para la nueva sucursal no encontrado con ID: "
                                    + suc.getDomicilio().getId_Domicilio()));
            suc.setDomicilio(domicilioExistente);
        } else if (suc.getDomicilio() != null) {
            // Si el domicilio no tiene ID, se asume que es nuevo y se guardará por cascada.
            // No se necesita hacer nada aquí si se quiere permitir la creación de un nuevo
            // domicilio.
            // Si se quisiera evitar la creación de un nuevo domicilio si no se proporciona
            // un ID existente,
            // se podría lanzar una excepción o limpiar el campo.
            // Por ahora, se permite la creación de un nuevo domicilio.
        }
        // Validar que la empresa exista si se proporciona
        if (suc.getEmpresa() != null && suc.getEmpresa().getId_Empresa() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La empresa asociada a la sucursal debe tener un ID válido.");
        }
        // Aquí se podrían añadir más validaciones, como verificar que la empresa exista
        // en la BD.

        return repo.save(suc);
    }

    public Sucursal update(Long id, Sucursal dto) {
        if (dto.getDomicilio() != null && dto.getDomicilio().getId_Domicilio() != null) {
            Domicilio domicilioExistente = domicilioRepository.findById(dto.getDomicilio().getId_Domicilio())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Domicilio no encontrado"));
            dto.setDomicilio(domicilioExistente);
        }

        Sucursal existing = findById(id);
        existing.setNombre(dto.getNombre());
        existing.setHorarios(dto.getHorarios());
        existing.setDomicilio(dto.getDomicilio());
        existing.setEmpresa(dto.getEmpresa());

        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id);
        repo.deleteById(id);
    }
}