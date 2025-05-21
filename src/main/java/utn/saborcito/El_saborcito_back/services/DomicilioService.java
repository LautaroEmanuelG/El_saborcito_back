package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;

import java.util.List;

@Service
public class DomicilioService {
    private final DomicilioRepository repo;
    private final LocalidadRepository localidadRepository;

    public DomicilioService(DomicilioRepository repo, LocalidadRepository localidadRepository) {
        this.repo = repo;
        this.localidadRepository = localidadRepository;
    }

    public List<Domicilio> findAll() {
        return repo.findAll();
    }

    public Domicilio findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Domicilio no encontrado con ID: " + id));
    }

    public Domicilio create(Domicilio dom) {
        // Aquí se podrían añadir validaciones antes de guardar, por ejemplo, campos
        // obligatorios.
        if (dom.getCalle() == null || dom.getCalle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calle no puede estar vacía.");
        }
        if (dom.getLocalidad() == null || dom.getLocalidad().getId_Localidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La localidad es obligatoria.");
        }
        return repo.save(dom);
    }

    public Domicilio update(Long id, Domicilio domicilioActualizado) {
        Domicilio domicilioExistente = findById(id); // findById ya lanza la excepción si no se encuentra

        // Actualizar campos. Se podrían añadir validaciones para cada campo.
        if (domicilioActualizado.getCalle() != null) {
            domicilioExistente.setCalle(domicilioActualizado.getCalle());
        }
        if (domicilioActualizado.getNumero() != null) {
            domicilioExistente.setNumero(domicilioActualizado.getNumero());
        }
        if (domicilioActualizado.getCp() != null) {
            domicilioExistente.setCp(domicilioActualizado.getCp());
        }
        // Validar que la localidad exista si se proporciona una nueva.
        if (domicilioActualizado.getLocalidad() != null && domicilioActualizado.getLocalidad().getId_Localidad() != null) {
            // Aquí se podría verificar si la localidad existe en la base de datos
            Localidad localidad = localidadRepository.findById(domicilioActualizado.getLocalidad().getId_Localidad())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localidad no válida."));
            domicilioExistente.setLocalidad(localidad);
            domicilioExistente.setLocalidad(domicilioActualizado.getLocalidad());
        } else if (domicilioActualizado.getLocalidad() != null && domicilioActualizado.getLocalidad().getId_Localidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID de Localidad no puede ser nulo si se proporciona el objeto Localidad.");
        }

        // La actualización del usuario asociado a un domicilio debe manejarse con
        // cuidado.
        // Generalmente, un domicilio pertenece a un usuario y no se cambia de
        // propietario fácilmente.
        // Si se permite, se necesitarían validaciones.
        if (domicilioActualizado.getUsuario() != null) {
            // Validar que el usuario exista, etc.
            domicilioExistente.setUsuario(domicilioActualizado.getUsuario());
        }

        return repo.save(domicilioExistente);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Domicilio no encontrado con ID: " + id);
        repo.deleteById(id);
    }
}