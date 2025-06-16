import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.PromocionDTO;
import utn.saborcito.El_saborcito_back.entities.Promocion;
import utn.saborcito.El_saborcito_back.entities.PromocionDetalle;
import utn.saborcito.El_saborcito_back.mappers.PromocionMapper;
import utn.saborcito.El_saborcito_back.repositories.ArticuloRepository;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PromocionService {

    private final PromocionRepository repo;
    private final PromocionMapper promocionMapper;
    private final ArticuloRepository articuloRepository;

    public PromocionDTO save(PromocionDTO promocionDTO) {
        Promocion promocion = promocionMapper.toEntity(promocionDTO);
        validarPromocion(promocion, false, promocionDTO);

        // Setear referencia inversa y asignar artículo concreto en los detalles
        if (promocion.getPromocionDetalles() != null && promocionDTO.getPromocionDetalles() != null) {
            for (int i = 0; i < promocion.getPromocionDetalles().size(); i++) {
                PromocionDetalle detalle = promocion.getPromocionDetalles().get(i);
                var detalleDTO = promocionDTO.getPromocionDetalles().get(i);
                if (detalleDTO.getArticulo() != null && detalleDTO.getArticulo().getId() != null) {
                    Articulo articulo = articuloRepository.findById(detalleDTO.getArticulo().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado con ID: " + detalleDTO.getArticulo().getId()));
                    detalle.setArticulo(articulo);
                }
                detalle.setPromocion(promocion);
            }
        }

        // Calcular y asignar descuento antes de guardar
        promocion.setDescuento(calcularDescuento(promocion));

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
        existingPromocion.setPrecioPromocional(promocionActualizada.getPrecioPromocional());
        existingPromocion.setSucursal(promocionActualizada.getSucursal());
        existingPromocion.setImagen(promocionActualizada.getImagen());
        existingPromocion.setEliminado(promocionActualizada.isEliminado());

        // Manejo correcto de la colección para evitar error Hibernate
        existingPromocion.getPromocionDetalles().clear();
        if (promocionActualizada.getPromocionDetalles() != null && promocionDTO.getPromocionDetalles() != null) {
            for (int i = 0; i < promocionActualizada.getPromocionDetalles().size(); i++) {
                PromocionDetalle detalle = promocionActualizada.getPromocionDetalles().get(i);
                var detalleDTO = promocionDTO.getPromocionDetalles().get(i);
                if (detalleDTO.getArticulo() != null && detalleDTO.getArticulo().getId() != null) {
                    Articulo articulo = articuloRepository.findById(detalleDTO.getArticulo().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado con ID: " + detalleDTO.getArticulo().getId()));
                    detalle.setArticulo(articulo);
                }
                detalle.setPromocion(existingPromocion);
                existingPromocion.getPromocionDetalles().add(detalle);
            }
        }

        // Calcular y asignar descuento antes de guardar
        existingPromocion.setDescuento(calcularDescuento(existingPromocion));

        Promocion savedPromocion = repo.save(existingPromocion);
        return promocionMapper.toDTO(savedPromocion);
    }

    private void validarPromocion(Promocion promocion, boolean esActualizacion, PromocionDTO promocionDTO) {
        // Lógica de validación
        if (promocion.getFechaDesde() != null && promocion.getFechaHasta() != null &&
                promocion.getFechaDesde().isAfter(promocion.getFechaHasta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        if (promocion.getHoraDesde() != null && promocion.getHoraHasta() != null &&
                promocion.getHoraDesde().isAfter(promocion.getHoraHasta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora de inicio no puede ser posterior a la hora de fin.");
        }

        if (Objects.isNull(promocion.getDenominacion()) || promocion.getDenominacion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La denominación no puede estar vacía.");
        }

        if (promocion.getDescuento() == null || promocion.getDescuento().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El descuento debe ser mayor a cero.");
        }

        if (promocion.getPrecioPromocional() == null || promocion.getPrecioPromocional().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio promocional debe ser mayor a cero.");
        }

        if (promocion.getSucursal() == null || promocion.getSucursal().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sucursal es obligatoria.");
        }

        if (!esActualizacion && repo.existsByDenominacionAndEliminadoFalse(promocion.getDenominacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una promoción activa con esa denominación.");
        }
    }

    private Double calcularDescuento(Promocion promocion) {
        if (promocion.getPromocionDetalles() == null || promocion.getPromocionDetalles().isEmpty()) return null;
        double sumaArticulos = promocion.getPromocionDetalles().stream()
            .mapToDouble(detalle -> {
                Double precio = detalle.getArticulo() != null && detalle.getArticulo().getPrecioVenta() != null
                    ? detalle.getArticulo().getPrecioVenta()
                    : 0.0;
                return precio * (detalle.getCantidadRequerida() != null ? detalle.getCantidadRequerida() : 1);
            })
            .sum();
        if (sumaArticulos == 0 || promocion.getPrecioPromocional() == null) return null;
        double descuento = 100 - (promocion.getPrecioPromocional() / sumaArticulos) * 100;
        return descuento > 0 ? descuento : 0;
    }
}