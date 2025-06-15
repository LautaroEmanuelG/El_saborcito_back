package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO; // Asegurada la importación correcta
import utn.saborcito.El_saborcito_back.mappers.PromocionMapper;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
import utn.saborcito.El_saborcito_back.models.Sucursal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromocionService {
    private final PromocionRepository repo;
    private final SucursalRepository sucursalRepository;
    private final ImagenRepository imagenRepository;
    private final PromocionMapper promocionMapper;

    public List<PromocionDTO> findAll() {
        return repo.findAll().stream()
                .map(promocionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PromocionDTO findById(Long id) {
        Promocion promocion = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return promocionMapper.toDTO(promocion);
    }

    public PromocionDTO save(PromocionDTO promocionDTO) {
        Promocion promocion = promocionMapper.toEntity(promocionDTO);
        validarPromocion(promocion, false, promocionDTO);
        Promocion savedPromocion = repo.save(promocion);
        return promocionMapper.toDTO(savedPromocion);
    }

    public PromocionDTO update(Long id, PromocionDTO promocionDTO) {
        Promocion existingPromocion = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Promoción no encontrada con ID: " + id));

        Promocion promocionActualizada = promocionMapper.toEntity(promocionDTO);
        promocionActualizada.setId(id);

        validarPromocion(promocionActualizada, true, promocionDTO);
        existingPromocion.setDenominacion(promocionActualizada.getDenominacion());
        existingPromocion.setFechaDesde(promocionActualizada.getFechaDesde());
        existingPromocion.setFechaHasta(promocionActualizada.getFechaHasta());
        existingPromocion.setHoraDesde(promocionActualizada.getHoraDesde());
        existingPromocion.setHoraHasta(promocionActualizada.getHoraHasta());
        existingPromocion.setDescuento(promocionActualizada.getDescuento());
        existingPromocion.setPrecioPromocional(promocionActualizada.getPrecioPromocional());
        existingPromocion.setSucursal(promocionActualizada.getSucursal());
        existingPromocion.setImagen(promocionActualizada.getImagen());
        existingPromocion.setPromocionDetalles(promocionActualizada.getPromocionDetalles());

        Promocion savedPromocion = repo.save(existingPromocion);
        return promocionMapper.toDTO(savedPromocion);
    }

    private void validarPromocion(Promocion p, boolean isUpdate, PromocionDTO dto) {
        if (p.getDenominacion() == null || p.getDenominacion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La denominación de la promoción no puede estar vacía.");
        }

        if (p.getFechaDesde() == null || p.getFechaHasta() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las fechas desde y hasta son obligatorias.");
        }
        if (p.getFechaDesde().isAfter(p.getFechaHasta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha desde no puede ser posterior a la fecha hasta.");
        }
        if (!isUpdate && p.getFechaHasta().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha hasta no puede ser una fecha pasada.");
        }

        if (p.getHoraDesde() != null && p.getHoraHasta() != null) {
            if (p.getHoraDesde().isAfter(p.getHoraHasta())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La hora desde no puede ser posterior a la hora hasta.");
            }
        } else if ((p.getHoraDesde() != null && p.getHoraHasta() == null)
                || (p.getHoraDesde() == null && p.getHoraHasta() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Si se especifica una hora (desde o hasta), ambas deben ser especificadas.");
        }

        boolean tieneDescuento = p.getDescuento() != null && p.getDescuento() > 0;
        boolean tienePrecioPromocional = p.getPrecioPromocional() != null && p.getPrecioPromocional() > 0;

        if (tieneDescuento && tienePrecioPromocional) { // Corregido 'y' a '&&'
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No puede usar descuento y precio promocional al mismo tiempo.");
        }
        if (!tieneDescuento && !tienePrecioPromocional) { // Corregido 'y' a '&&'
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar un descuento o un precio promocional.");
        }
        if (tieneDescuento && (p.getDescuento() <= 0 || p.getDescuento() >= 100)) { // Corregido 'y' a '&&'
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El descuento debe ser un porcentaje entre 1 y 99.");
        }
        if (tienePrecioPromocional && p.getPrecioPromocional() <= 0) { // Corregido 'y' a '&&'
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio promocional debe ser mayor a cero.");
        } // Validación de artículos se maneja ahora a través de PromocionDetalles
        // TODO: Implementar validación específica para el nuevo modelo

        if (tienePrecioPromocional && p.getPrecioPromocional() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El precio promocional debe ser mayor a cero.");
        }

        if (dto.getSucursal() != null && dto.getSucursal().getId() != null) { // Corregido 'y' a '&&'
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursal().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Sucursal asociada no encontrada con ID: " + dto.getSucursal().getId()));
            p.setSucursal(sucursal);
        } else {
            p.setSucursal(null);
        }

        // 🖼️ Validar imagen si está especificada
        if (dto.getImagen() != null && dto.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Imagen no encontrada con ID: " + dto.getImagen().getId()));
            p.setImagen(imagen);
        } else {
            p.setImagen(null);
        }
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promoción no encontrada con id: " + id);
        }
        repo.deleteById(id);
    }

    /**
     * 🎁 Busca promociones vigentes para una sucursal específica
     */
    public List<PromocionDTO> findPromocionesVigentes(Long sucursalId) {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return repo.findAll().stream()
                .filter(promocion -> {
                    // Filtrar por sucursal
                    if (promocion.getSucursal() != null &&
                            !promocion.getSucursal().getId().equals(sucursalId)) {
                        return false;
                    }

                    // Filtrar por fechas
                    if (promocion.getFechaDesde() != null && promocion.getFechaDesde().isAfter(hoy)) {
                        return false;
                    }
                    if (promocion.getFechaHasta() != null && promocion.getFechaHasta().isBefore(hoy)) {
                        return false;
                    }

                    // Filtrar por horarios
                    if (promocion.getHoraDesde() != null && promocion.getHoraHasta() != null) {
                        return !ahora.isBefore(promocion.getHoraDesde()) &&
                                !ahora.isAfter(promocion.getHoraHasta());
                    }

                    return true;
                })
                .map(promocionMapper::toDTO)
                .collect(Collectors.toList());
    }
}