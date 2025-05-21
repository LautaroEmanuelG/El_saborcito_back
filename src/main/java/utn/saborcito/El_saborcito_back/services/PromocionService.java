package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.Sucursal;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionService {
    private final PromocionRepository repo;
    private final ArticuloRepository articuloRepository; // Inyectar ArticuloRepository
    private final SucursalRepository sucursalRepository; // Inyectar SucursalRepository

    public List<Promocion> findAll() {
        return repo.findAll();
    }

    public Promocion findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Promocion save(Promocion p) {
        validarPromocion(p, false);
        return repo.save(p);
    }

    public Promocion update(Long id, Promocion p) {
        Promocion existingPromocion = findById(id);
        p.setId(id);
        validarPromocion(p, true);
        // Copiar campos actualizables de p a existingPromocion
        existingPromocion.setDenominacion(p.getDenominacion());
        existingPromocion.setFechaDesde(p.getFechaDesde());
        existingPromocion.setFechaHasta(p.getFechaHasta());
        existingPromocion.setHoraDesde(p.getHoraDesde());
        existingPromocion.setHoraHasta(p.getHoraHasta());
        existingPromocion.setDescuento(p.getDescuento());
        existingPromocion.setPrecioPromocional(p.getPrecioPromocional());
        existingPromocion.setArticulo(p.getArticulo());
        existingPromocion.setSucursal(p.getSucursal());

        return repo.save(existingPromocion);
    }

    private void validarPromocion(Promocion p, boolean isUpdate) {
        if (p.getDenominacion() == null || p.getDenominacion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La denominación de la promoción no puede estar vacía.");
        }

        // Validaciones de Fechas
        if (p.getFechaDesde() == null || p.getFechaHasta() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las fechas desde y hasta son obligatorias.");
        }
        if (p.getFechaDesde().isAfter(p.getFechaHasta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha desde no puede ser posterior a la fecha hasta.");
        }
        // Opcional: Validar que la fechaHasta no sea en el pasado (solo para nuevas
        // promociones)
        if (!isUpdate && p.getFechaHasta().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha hasta no puede ser una fecha pasada.");
        }

        // Validaciones de Horas (si están presentes)
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

        // Validaciones de Descuento y Precio Promocional
        boolean tieneDescuento = p.getDescuento() != null && p.getDescuento() > 0;
        boolean tienePrecioPromocional = p.getPrecioPromocional() != null && p.getPrecioPromocional() > 0;

        if (tieneDescuento && tienePrecioPromocional) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No puede usar descuento y precio promocional al mismo tiempo.");
        }
        if (!tieneDescuento && !tienePrecioPromocional) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar un descuento o un precio promocional.");
        }
        if (tieneDescuento && (p.getDescuento() <= 0 || p.getDescuento() >= 100)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El descuento debe ser un porcentaje entre 1 y 99.");
        }
        if (tienePrecioPromocional && p.getPrecioPromocional() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio promocional debe ser mayor a cero.");
        }

        // Validación de Artículo
        if (p.getArticulo() == null || p.getArticulo().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La promoción debe estar asociada a un artículo.");
        }
        Articulo articulo = articuloRepository.findById(p.getArticulo().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo asociado no encontrado con ID: " + p.getArticulo().getId()));
        p.setArticulo(articulo); // Asegurar que el objeto completo esté en la promoción

        if (tienePrecioPromocional && articulo.getPrecioVenta() != null
                && p.getPrecioPromocional() >= articulo.getPrecioVenta()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El precio promocional debe ser menor al precio de venta original del artículo.");
        }

        // Validación de Sucursal (si aplica, puede ser opcional si la promoción es
        // general)
        if (p.getSucursal() != null && p.getSucursal().getId() != null) {
            Sucursal sucursal = sucursalRepository.findById(p.getSucursal().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Sucursal asociada no encontrada con ID: " + p.getSucursal().getId()));
            p.setSucursal(sucursal); // Asegurar que el objeto completo esté en la promoción
        }
        // Si la sucursal es opcional y no se provee, se podría dejar
        // p.setSucursal(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}