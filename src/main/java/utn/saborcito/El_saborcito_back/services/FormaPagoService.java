package utn.saborcito.El_saborcito_back.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.FormaPago;
import utn.saborcito.El_saborcito_back.repositories.FormaPagoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormaPagoService {

    private final FormaPagoRepository repo;

    /**
     * Inicializa la tabla de formas de pago con los valores del enum FormaPago
     */
    @PostConstruct
    public void init() {
        // Solo inicializar si la tabla está vacía
        if (repo.count() == 0) {
            List<String> nombresFormasPago = Arrays.asList("EFECTIVO", "MERCADO_PAGO"); // Agrega los nombres que correspondan
            nombresFormasPago.forEach(nombre -> {
                FormaPago formaPago = new FormaPago(nombre);
                repo.save(formaPago);
            });
        }
    }

    public List<FormaPago> findAll() {
        return repo.findAll();
    }

    public FormaPago findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "FormaPago no encontrada con ID: " + id));
    }

    public FormaPago findByNombre(String nombre) {
        return repo.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "FormaPago no encontrada con nombre: " + nombre));
    }

    public FormaPago save(FormaPago formaPago) {
        // Validar que no exista otra con el mismo nombre
        Optional<FormaPago> existente = repo.findByNombre(formaPago.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(formaPago.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe una forma de pago con el nombre: " + formaPago.getNombre());
        }

        return repo.save(formaPago);
    }

    public FormaPago update(Long id, FormaPago formaPago) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede actualizar: FormaPago no encontrada con ID: " + id);
        }

        formaPago.setId(id);
        return save(formaPago);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: FormaPago no encontrada con ID: " + id);
        }

        repo.deleteById(id);
    }
}
