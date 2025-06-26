package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.PedidoCreacionDTO;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 🔍 Servicio para validar artículos y promociones
 * Maneja la lógica de detección de promociones y validación de tipos de
 * artículos
 */
@Service
@RequiredArgsConstructor
public class ArticuloValidacionService {

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final PromocionRepository promocionRepository;
    private final ProduccionAnalisisService produccionAnalisisService;

    /**
     * 🎯 Busca y valida un artículo por ID
     * Determina si es ArticuloInsumo o ArticuloManufacturado
     *
     * @param articuloId ID del artículo a buscar
     * @return El artículo encontrado
     */
    public Articulo buscarYValidarArticulo(Long articuloId) {
        if (articuloId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID del artículo no puede ser nulo");
        }

        // Primero intentar como ArticuloInsumo
        Optional<ArticuloInsumo> insumo = articuloInsumoRepository.findByIdNotDeleted(articuloId);
        if (insumo.isPresent()) {
            return insumo.get();
        }

        // Luego intentar como ArticuloManufacturado
        Optional<ArticuloManufacturado> manufacturado = articuloManufacturadoRepository.findByIdNotDeleted(articuloId);
        if (manufacturado.isPresent()) {
            return manufacturado.get();
        }

        // Si no se encuentra en ninguno, lanzar excepción
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Artículo no encontrado con ID: " + articuloId);
    }

    /**
     * 🏷️ Busca promociones activas para una combinación de artículos
     *
     * @param detalles   Lista de detalles del pedido
     * @param sucursalId ID de la sucursal
     * @return Lista de promociones aplicables
     */
    public List<Promocion> buscarPromocionesActivas(
            List<PedidoCreacionDTO.DetallePedidoCreacionDTO> detalles,
            Long sucursalId) {

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now(); // Por ahora, buscar todas las promociones activas
        // En el futuro se puede implementar lógica más compleja
        return promocionRepository.findAll().stream()
                .filter(promocion -> promocion.getSucursal() != null &&
                        promocion.getSucursal().getId().equals(sucursalId) &&
                        (promocion.getFechaDesde() == null || !promocion.getFechaDesde().isAfter(hoy)) &&
                        (promocion.getFechaHasta() == null || !promocion.getFechaHasta().isBefore(hoy)) &&
                        (promocion.getHoraDesde() == null || !promocion.getHoraDesde().isAfter(ahora)) &&
                        (promocion.getHoraHasta() == null || !promocion.getHoraHasta().isBefore(ahora)))
                .collect(Collectors.toList());
    }

    /**
     * ✅ Valida que un artículo insumo tenga stock suficiente
     *
     * @param insumo             El artículo insumo
     * @param cantidadSolicitada Cantidad solicitada
     */
    public void validarStockInsumo(ArticuloInsumo insumo, Double cantidadSolicitada) {    // ✅ Cambio Integer a Double
        if (insumo.getStockActual() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El insumo " + insumo.getDenominacion() + " no tiene stock definido");
        }

        if (insumo.getStockActual() < cantidadSolicitada) {    // ✅ Comparación con Double
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para " + insumo.getDenominacion() +
                            ". Stock disponible: " + insumo.getStockActual() +
                            ", cantidad solicitada: " + cantidadSolicitada);
        }

        // Validar si es para elaborar vs para venta directa
        if (insumo.getEsParaElaborar() != null && insumo.getEsParaElaborar()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El insumo " + insumo.getDenominacion() +
                            " es solo para elaboración, no puede venderse directamente");
        }
    }

    /**
     * 🏭 Valida que un artículo manufacturado se pueda producir
     * Usa el servicio unificado para evitar duplicación
     *
     * @param manufacturado      El artículo manufacturado
     * @param cantidadSolicitada Cantidad solicitada
     */
    public void validarStockManufacturado(ArticuloManufacturado manufacturado, Double cantidadSolicitada) {    // ✅ Cambio Integer a Double
        if (manufacturado.getArticuloManufacturadoDetalles() == null ||
                manufacturado.getArticuloManufacturadoDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El artículo manufacturado " + manufacturado.getDenominacion() +
                            " no tiene receta definida");
        }

        // Usar el servicio unificado para la validación
        boolean sePuedeProducir = produccionAnalisisService.puedeProducirseManufacturado(manufacturado,
                cantidadSolicitada);    // ✅ Ahora es compatible con Double

        if (!sePuedeProducir) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede fabricar " + cantidadSolicitada + " unidades de " +
                            manufacturado.getDenominacion() + " por falta de insumos");
        }
    }

    /**
     * 🎯 Determina si un artículo es ArticuloInsumo
     */
    public boolean esArticuloInsumo(Articulo articulo) {
        return articulo instanceof ArticuloInsumo;
    }

    /**
     * 🏭 Determina si un artículo es ArticuloManufacturado
     */
    public boolean esArticuloManufacturado(Articulo articulo) {
        return articulo instanceof ArticuloManufacturado;
    }

    /**
     * 🏷️ Aplica promoción si corresponde
     *
     * @param articulo  El artículo
     * @param promocion La promoción a aplicar
     * @return El precio con promoción aplicada
     */
    public Double aplicarPromocion(Articulo articulo, Promocion promocion) {
        if (promocion == null) {
            return articulo.getPrecioVenta().doubleValue();
        }

        // Si tiene precio promocional fijo, usarlo
        if (promocion.getPrecioPromocional() != null) {
            return promocion.getPrecioPromocional();
        }

        // Si tiene descuento porcentual, aplicarlo
        if (promocion.getDescuento() != null) {
            double precioOriginal = articulo.getPrecioVenta().doubleValue();
            double descuentoDecimal = promocion.getDescuento() / 100.0;
            return precioOriginal * (1 - descuentoDecimal);
        }

        // Si no tiene ninguno, precio original
        return articulo.getPrecioVenta().doubleValue();
    }

    // ✅ Métodos sobrecargados para mantener compatibilidad con código existente que use Integer

    /**
     * ✅ Sobrecarga para compatibilidad con Integer
     */
    public void validarStockInsumo(ArticuloInsumo insumo, Integer cantidadSolicitada) {
        validarStockInsumo(insumo, cantidadSolicitada.doubleValue());
    }

    /**
     * 🏭 Sobrecarga para compatibilidad con Integer
     */
    public void validarStockManufacturado(ArticuloManufacturado manufacturado, Integer cantidadSolicitada) {
        validarStockManufacturado(manufacturado, cantidadSolicitada.doubleValue());
    }
}