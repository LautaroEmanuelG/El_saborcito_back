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

    public List<DetallePedidoPromocion> aplicarPromocionesSeleccionadas(
            Pedido pedido, List<DetallePedido> detalles,
            List<PromocionSeleccionadaDTO> promocionesSeleccionadas, Sucursal sucursal) {

        return promocionesSeleccionadas.stream()
                .map(promoSeleccionada -> {
                    Promocion promocion = obtenerPromocionValida(promoSeleccionada.getPromocionId());
                    return aplicarPromocion(pedido, promocion, promoSeleccionada.getCantidad(), detalles);
                })
                .toList();
    }

    private Promocion obtenerPromocionValida(Long promocionId) {
        Promocion promocion = promocionRepository.findById(promocionId)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada: " + promocionId));

        if (!estaVigente(promocion)) {
            throw new RuntimeException("La promoción '" + promocion.getDenominacion() + "' no está vigente");
        }

        return promocion;
    }

    private boolean estaVigente(Promocion promocion) {
        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();

        if (promocion.getFechaDesde().isAfter(fecha) || promocion.getFechaHasta().isBefore(fecha)) {
            return false;
        }

        if (promocion.getHoraDesde() != null && promocion.getHoraHasta() != null) {
            return !hora.isBefore(promocion.getHoraDesde()) && !hora.isAfter(promocion.getHoraHasta());
        }

        return true;
    }

    private DetallePedidoPromocion aplicarPromocion(Pedido pedido, Promocion promocion, int veces,
            List<DetallePedido> detalles) {
        double precioOriginal = calcularPrecioOriginal(promocion, veces);
        double precioPromocional = calcularPrecioPromocional(promocion, veces);

        crearDetallesPedidoPromocion(pedido, promocion, veces, detalles);

        return DetallePedidoPromocion.builder()
                .pedido(pedido)
                .promocion(promocion)
                .cantidadPromocion(veces)
                .precioTotalPromocion(precioPromocional)
                .ahorroTotal(precioOriginal - precioPromocional)
                .build();
    }

    private void crearDetallesPedidoPromocion(Pedido pedido, Promocion promocion, int veces,
            List<DetallePedido> detalles) {
        for (PromocionDetalle promoDetalle : promocion.getPromocionDetalles()) {
            Articulo articulo = promoDetalle.getArticulo();
            int cantidadTotal = promoDetalle.getCantidadRequerida() * veces;

            DetallePedido detalleExistente = buscarDetalleExistente(detalles, articulo.getId());

            if (detalleExistente != null) {
                actualizarDetalleExistente(detalleExistente, cantidadTotal);
            } else {
                detalles.add(crearNuevoDetallePromocion(pedido, articulo, cantidadTotal, promocion.getId()));
            }
        }
    }

    private DetallePedido buscarDetalleExistente(List<DetallePedido> detalles, Long articuloId) {
        return detalles.stream()
                .filter(d -> d.getArticulo().getId().equals(articuloId))
                .findFirst()
                .orElse(null);
    }

    private void actualizarDetalleExistente(DetallePedido detalle, int cantidadAdicional) {
        detalle.setCantidad(detalle.getCantidad() + cantidadAdicional);
        detalle.setCantidadConPromocion(detalle.getCantidadConPromocion() + cantidadAdicional);
        detalle.calcularYEstablecerSubtotal();
    }

    private DetallePedido crearNuevoDetallePromocion(Pedido pedido, Articulo articulo, int cantidad, Long promocionId) {
        return DetallePedido.builder()
                .cantidad(cantidad)
                .cantidadConPromocion(cantidad)
                .cantidadSinPromocion(0)
                .origen(OrigenDetalle.PROMOCION)
                .promocionOrigenId(promocionId)
                .articulo(articulo)
                .pedido(pedido)
                .subtotal(0.0)
                .build();
    }

    private double calcularPrecioOriginal(Promocion promocion, int veces) {
        return promocion.getPromocionDetalles().stream()
                .mapToDouble(detalle -> {
                    double precio = detalle.getArticulo().getPrecioVenta() != null
                            ? detalle.getArticulo().getPrecioVenta().doubleValue()
                            : 0.0;
                    return detalle.getCantidadRequerida() * precio;
                })
                .sum() * veces;
    }

    private double calcularPrecioPromocional(Promocion promocion, int veces) {
        if (promocion.getPrecioPromocional() != null) {
            return promocion.getPrecioPromocional() * veces;
        }

        if (promocion.getDescuento() != null) {
            double precioOriginal = calcularPrecioOriginal(promocion, 1);
            return precioOriginal * (1 - promocion.getDescuento() / 100) * veces;
        }

        return calcularPrecioOriginal(promocion, veces);
    }
}
