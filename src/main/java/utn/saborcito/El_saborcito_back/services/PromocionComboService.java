package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.dto.PromocionSeleccionadaDTO;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PromocionComboService {

    private final PromocionRepository promocionRepository;

    /**
     * 🎯 Aplica promociones específicas seleccionadas por el cliente
     */
    public List<DetallePedidoPromocion> aplicarPromocionesSeleccionadas(
            Pedido pedido,
            List<DetallePedido> detalles,
            List<PromocionSeleccionadaDTO> promocionesSeleccionadas,
            Sucursal sucursal) {

        List<DetallePedidoPromocion> promocionesAplicadas = new ArrayList<>();

        for (PromocionSeleccionadaDTO promoSeleccionada : promocionesSeleccionadas) {
            Promocion promocion = promocionRepository.findById(promoSeleccionada.getPromocionId())
                    .orElseThrow(() -> new RuntimeException(
                            "Promoción no encontrada: " + promoSeleccionada.getPromocionId()));

            // Validar que la promoción esté vigente
            if (!estaVigente(promocion, LocalDate.now(), LocalTime.now())) {
                throw new RuntimeException("La promoción '" + promocion.getDenominacion() + "' no está vigente");
            }

            // Aplicar la promoción
            DetallePedidoPromocion detallePromo = aplicarPromocion(
                    pedido, promocion, promoSeleccionada.getCantidad(), detalles);
            promocionesAplicadas.add(detallePromo);
        }

        return promocionesAplicadas;
    }

    /**
     * ⏰ Verifica si una promoción está vigente
     */
    private boolean estaVigente(Promocion promocion, LocalDate fecha, LocalTime hora) {
        // Validar fechas
        if (promocion.getFechaDesde().isAfter(fecha)) {
            return false;
        }

        if (promocion.getFechaHasta().isBefore(fecha)) {
            return false;
        }

        // Validar horas (solo si están definidas)
        if (promocion.getHoraDesde() != null && promocion.getHoraHasta() != null) {
            return !hora.isBefore(promocion.getHoraDesde()) &&
                    !hora.isAfter(promocion.getHoraHasta());
        }

        return true;
    }

    /**
     * ✅ Aplica una promoción específica a un pedido creando AMBAS estructuras:
     * 1. DetallePedidoPromocion (info financiera)
     * 2. DetallePedido para cada artículo de la promoción (para stock)
     */
    private DetallePedidoPromocion aplicarPromocion(
            Pedido pedido,
            Promocion promocion,
            int veces,
            List<DetallePedido> detalles) {

        double precioOriginalTotal = calcularPrecioOriginalPromocion(promocion, veces);
        double precioPromocional = calcularPrecioPromocionalTotal(promocion, veces);
        double ahorro = precioOriginalTotal - precioPromocional;

        // 1. Crear DetallePedidoPromocion (registro financiero)
        DetallePedidoPromocion detallePedidoPromocion = DetallePedidoPromocion.builder()
                .pedido(pedido)
                .promocion(promocion)
                .cantidadPromocion(veces)
                .precioTotalPromocion(precioPromocional)
                .ahorroTotal(ahorro)
                .build();

        // 2. Crear/Actualizar DetallePedido para cada artículo de la promoción
        crearDetallesPedidoPromocion(pedido, promocion, veces, detalles);

        return detallePedidoPromocion;
    }

    /**
     * 🎁 Crea DetallePedido para artículos de promoción (para control de stock)
     */
    private void crearDetallesPedidoPromocion(Pedido pedido, Promocion promocion, int veces,
            List<DetallePedido> detalles) {
        for (PromocionDetalle promoDetalle : promocion.getPromocionDetalles()) {
            Articulo articulo = promoDetalle.getArticulo();
            int cantidadTotal = promoDetalle.getCantidadRequerida() * veces;

            // Buscar si ya existe un DetallePedido para este artículo
            DetallePedido detalleExistente = detalles.stream()
                    .filter(d -> d.getArticulo().getId().equals(articulo.getId()))
                    .findFirst()
                    .orElse(null);

            if (detalleExistente != null) {
                // Actualizar detalle existente
                detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidadTotal);
                detalleExistente.setCantidadConPromocion(
                        detalleExistente.getCantidadConPromocion() + cantidadTotal);
                // Recalcular subtotal (para artículos individuales)
                detalleExistente.calcularYEstablecerSubtotal();
            } else {
                // Crear nuevo DetallePedido para artículo de promoción
                DetallePedido nuevoDetalle = DetallePedido.builder()
                        .cantidad(cantidadTotal)
                        .cantidadConPromocion(cantidadTotal)
                        .cantidadSinPromocion(0)
                        .origen(OrigenDetalle.PROMOCION)
                        .promocionOrigenId(promocion.getId())
                        .articulo(articulo)
                        .pedido(pedido)
                        .subtotal(0.0) // Promociones no tienen subtotal individual
                        .build();

                detalles.add(nuevoDetalle);
            }
        }
    }

    /**
     * 💰 Calcula el precio original de la promoción (sin descuento)
     */
    private double calcularPrecioOriginalPromocion(Promocion promocion, int veces) {
        return promocion.getPromocionDetalles().stream()
                .mapToDouble(detalle -> {
                    double precioUnitario = detalle.getArticulo().getPrecioVenta() != null
                            ? detalle.getArticulo().getPrecioVenta().doubleValue()
                            : 0.0;
                    return detalle.getCantidadRequerida() * precioUnitario;
                })
                .sum() * veces;
    }

    /**
     * 🏷️ Calcula el precio promocional total
     */
    private double calcularPrecioPromocionalTotal(Promocion promocion, int veces) {
        if (promocion.getPrecioPromocional() != null) {
            return promocion.getPrecioPromocional() * veces;
        }

        if (promocion.getDescuento() != null) {
            double precioOriginal = calcularPrecioOriginalPromocion(promocion, 1);
            return precioOriginal * (1 - promocion.getDescuento() / 100) * veces;
        }

        return calcularPrecioOriginalPromocion(promocion, veces);
    }
}
