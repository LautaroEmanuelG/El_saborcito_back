package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.UnidadMedidaDTO;
import utn.saborcito.El_saborcito_back.mappers.UnidadMedidaMapper;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadMedidaService {

    private final UnidadMedidaRepository repo;
    private final UnidadMedidaMapper mapper;

    /**
     * Obtener solo las unidades de medida activas (no eliminadas)
     * Este método es para uso general en la aplicación
     */
    public List<UnidadMedidaDTO> findAll() {
        return repo.findAllActive().stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Obtener todas las unidades (incluyendo eliminadas)
     * Este método es para el frontend que maneja vista con eliminados
     */
    public List<UnidadMedidaDTO> findAllIncludingDeleted() {
        return repo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Buscar unidad de medida por ID (solo activas)
     */
    public UnidadMedidaDTO findById(Long id) {
        UnidadMedida unidad = repo.findByIdActive(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unidad de medida no encontrada"));
        return mapper.toDTO(unidad);
    }

    /**
     * Crear nueva unidad de medida
     * Incluye validación de duplicados por denominación (case insensitive)
     */
    public UnidadMedidaDTO save(UnidadMedidaDTO dto) {
        // Validar que la denominación no esté vacía
        if (dto.getDenominacion() == null || dto.getDenominacion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La denominación es requerida");
        }

        // Validar duplicados (case insensitive)
        String denominacionTrimmed = dto.getDenominacion().trim();
        Optional<UnidadMedida> existente = repo.findByDenominacionIgnoreCaseAndNotDeleted(denominacionTrimmed);
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una unidad de medida con la denominación: " + denominacionTrimmed);
        }

        // Crear la entidad
        UnidadMedida unidad = mapper.toEntity(dto);
        unidad.setDenominacion(denominacionTrimmed); // Guardar sin espacios extra
        unidad.setEliminado(false); // Asegurar que esté activa

        UnidadMedida saved = repo.save(unidad);
        return mapper.toDTO(saved);
    }

    /**
     * Actualizar unidad de medida existente
     * Incluye validación de duplicados excluyendo la unidad actual
     */
    public UnidadMedidaDTO update(Long id, UnidadMedidaDTO dto) {
        // Buscar la unidad existente
        UnidadMedida existing = repo.findByIdActive(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unidad de medida no encontrada"));

        // Validar que la denominación no esté vacía
        if (dto.getDenominacion() == null || dto.getDenominacion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La denominación es requerida");
        }

        // Validar duplicados excluyendo la unidad actual (case insensitive)
        String denominacionTrimmed = dto.getDenominacion().trim();
        Optional<UnidadMedida> existente = repo.findByDenominacionIgnoreCaseAndNotDeletedAndNotId(
                denominacionTrimmed, id);
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una unidad de medida con la denominación: " + denominacionTrimmed);
        }

        // Actualizar campos
        existing.setDenominacion(denominacionTrimmed);
        existing.setEliminado(dto.getEliminado() != null ? dto.getEliminado() : false);

        UnidadMedida updated = repo.save(existing);
        return mapper.toDTO(updated);
    }

    /**
     * Baja lógica de unidad de medida
     * Marca la unidad como eliminada sin borrarla físicamente
     */
    public void bajaLogicaUnidadMedida(Long id) {
        UnidadMedida unidad = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unidad de medida no encontrada"));

        // Verificar si ya está eliminada
        if (unidad.getEliminado()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La unidad de medida ya está eliminada");
        }

        unidad.setEliminado(true);
        repo.save(unidad);
    }

    /**
     * Restaurar unidad de medida (alta lógica)
     * Reactiva una unidad que fue eliminada lógicamente
     */
    public void restaurarUnidadMedida(Long id) {
        UnidadMedida unidad = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unidad de medida no encontrada"));

        // Verificar si ya está activa
        if (!unidad.getEliminado()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La unidad de medida ya está activa");
        }

        // Validar que no haya conflictos al restaurar
        Optional<UnidadMedida> existente = repo.findByDenominacionIgnoreCaseAndNotDeleted(
                unidad.getDenominacion());
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede restaurar: ya existe una unidad activa con la denominación '" +
                            unidad.getDenominacion() + "'");
        }

        unidad.setEliminado(false);
        repo.save(unidad);
    }

    /**
     * Eliminación física de unidad de medida
     * ⚠️ CUIDADO: Esta operación es irreversible
     * Solo usar cuando sea absolutamente necesario
     */
    public void delete(Long id) {
        UnidadMedida unidad = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unidad de medida no encontrada"));

        // Aquí podrías agregar validaciones adicionales, como verificar
        // que no esté siendo usada en insumos activos
        // Ejemplo:
        // if (insumoRepository.existsByUnidadMedidaIdAndEliminadoFalse(id)) {
        //     throw new ResponseStatusException(HttpStatus.CONFLICT,
        //         "No se puede eliminar: la unidad está siendo usada por insumos activos");
        // }

        repo.deleteById(id);
    }

    /**
     * Verificar si una denominación ya existe (case insensitive)
     * Método auxiliar para validaciones desde otros servicios
     */
    public boolean existsByDenominacion(String denominacion) {
        return repo.findByDenominacionIgnoreCaseAndNotDeleted(denominacion.trim()).isPresent();
    }

    /**
     * Verificar si una denominación ya existe excluyendo una ID específica
     * Útil para validaciones en updates desde otros servicios
     */
    public boolean existsByDenominacionAndNotId(String denominacion, Long excludeId) {
        return repo.findByDenominacionIgnoreCaseAndNotDeletedAndNotId(
                denominacion.trim(), excludeId).isPresent();
    }
}