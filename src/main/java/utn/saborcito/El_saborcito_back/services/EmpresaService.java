package utn.saborcito.El_saborcito_back.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Empresa;
import utn.saborcito.El_saborcito_back.repositories.EmpresaRepository;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmpresaService {
    private final EmpresaRepository repo;

    // Patrón para validar CUIL/CUIT: XX-XXXXXXXX-X
    private static final Pattern CUIL_PATTERN = Pattern.compile("^\\d{2}-\\d{8}-\\d{1}$");

    public EmpresaService(EmpresaRepository repo) {
        this.repo = repo;
    }

    private boolean isValidCuilFormat(String cuil) {
        if (cuil == null || cuil.trim().isEmpty()) {
            return false; // O considerar si un CUIL vacío es válido en algún contexto
        }
        return CUIL_PATTERN.matcher(cuil).matches();
    }

    public List<Empresa> findAll() {
        return repo.findAll();
    }

    public Empresa findById(Long id) {
        return repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada con id " + id));
    }

    public Empresa create(Empresa emp) {
        if (!isValidCuilFormat(emp.getCuil())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato de CUIL inválido: " + emp.getCuil() + ". Debe ser XX-XXXXXXXX-X.");
        }
        // Validar si ya existe una empresa con el mismo CUIL
        if (repo.existsByCuil(emp.getCuil())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una empresa con el CUIL: " + emp.getCuil());
        }
        return repo.save(emp);
    }

    public Empresa update(Long id, Empresa dto) {
        Empresa existing = findById(id);
        if (!isValidCuilFormat(dto.getCuil())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato de CUIL inválido: " + dto.getCuil() + ". Debe ser XX-XXXXXXXX-X.");
        }

        // Validar si el nuevo CUIL ya existe en otra empresa diferente a la actual
        if (!existing.getCuil().equals(dto.getCuil()) && repo.existsByCuil(dto.getCuil())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otra empresa con el CUIL: " + dto.getCuil());
        }

        existing.setNombre(dto.getNombre());
        existing.setRazonSocial(dto.getRazonSocial());
        existing.setCuil(dto.getCuil());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada con id " + id);
        repo.deleteById(id);
    }
}