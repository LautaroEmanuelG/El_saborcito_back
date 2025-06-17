package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.Pedido;

import java.time.LocalTime;

/**
 * Servicio dedicado al cálculo de valores en pedidos.
 * Este servicio centraliza la lógica de cálculo para los pedidos y sus
 * detalles.
 */
@Service
@RequiredArgsConstructor
public class CalculadoraPedidoService {

    // Constantes para lógicas de negocio
    private static final String TIPO_ENVIO_TAKE_AWAY = "TAKE_AWAY";
    private static final Long TIPO_ENVIO_TAKE_AWAY_ID = 2L;
    private static final Double DESCUENTO_TAKE_AWAY = 0.10; // 10%
    private static final String TIPO_ENVIO_DELIVERY = "DELIVERY";
    private static final Integer MINUTOS_DELIVERY = 30;

    /**
     * Calcula el subtotal para un detalle de pedido.
     * 
     * @param detalle El detalle de pedido para calcular su subtotal
     * @return El valor calculado del subtotal
     */
    public Double calcularSubtotalDetalle(DetallePedido detalle) {
        if (detalle == null || detalle.getArticulo() == null || detalle.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede calcular el subtotal: detalle, artículo o cantidad son nulos");
        }

        Articulo articulo = detalle.getArticulo();
        if (articulo.getPrecioVenta() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El artículo " + articulo.getDenominacion() + " no tiene precio de venta asignado");
        }

        return detalle.getCantidad() * articulo.getPrecioVenta().doubleValue();
    }

    /**
     * Calcula el total de un pedido sumando los subtotales de todos sus detalles.
     * 
     * @param pedido El pedido para calcular su total
     * @return El valor calculado del total
     */
    public Double calcularTotalPedido(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede calcular el total: el pedido no tiene detalles");
        }

        return pedido.getDetalles().stream()
                .mapToDouble(this::calcularSubtotalDetalle)
                .sum();
    }

    /**
     * Calcula el costo total de un pedido, basado en los costos de los artículos.
     * 
     * @param pedido El pedido para calcular su costo total
     * @return El valor calculado del costo total
     */
    public Double calcularTotalCostoPedido(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede calcular el costo total: el pedido no tiene detalles");
        }

        double totalCosto = 0.0;

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getArticulo() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Detalle de pedido inválido: artículo o cantidad nulos o cantidad no positiva");
            }

            Articulo articulo = detalle.getArticulo();

            // Cálculo diferente según el tipo de artículo
            if (articulo instanceof ArticuloInsumo insumo) {
                if (insumo.getPrecioCompra() != null) {
                    totalCosto += insumo.getPrecioCompra() * detalle.getCantidad();
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "El insumo " + insumo.getDenominacion() + " no tiene precio de compra asignado");
                }
            } else if (articulo instanceof ArticuloManufacturado manufacturado) {
                if (manufacturado.getArticuloManufacturadoDetalles() != null) {
                    double costoManufacturado = 0.0;
                    for (ArticuloManufacturadoDetalle amd : manufacturado.getArticuloManufacturadoDetalles()) {
                        if (amd.getArticuloInsumo() != null && amd.getArticuloInsumo().getPrecioCompra() != null
                                && amd.getCantidad() != null) {
                            costoManufacturado += amd.getArticuloInsumo().getPrecioCompra() * amd.getCantidad();
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Un insumo del artículo manufacturado " + manufacturado.getDenominacion() +
                                            " no tiene precio de compra o cantidad asignados");
                        }
                    }
                    totalCosto += costoManufacturado * detalle.getCantidad();
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "El artículo manufacturado " + manufacturado.getDenominacion() +
                                    " no tiene detalles de componentes");
                }
            }
        }

        return totalCosto;
    }

    /**
     * Actualiza los totales de un pedido incluyendo promociones y descuentos.
     * 🆕 Incluye descuento del 10% para TAKE_AWAY
     * 
     * @param pedido El pedido a actualizar
     */
    public void actualizarTotalesPedido(Pedido pedido) {
        Double totalSinDescuento = calcularTotalPedidoConPromociones(pedido);
        Double totalConDescuento = aplicarDescuentoTakeAway(totalSinDescuento, pedido);

        pedido.setTotal(totalConDescuento);
        pedido.setTotalCosto(calcularTotalCostoPedido(pedido));

        // 🆕 Actualizar hora estimada con nueva lógica
        pedido.setHorasEstimadaFinalizacion(calcularHoraEstimadaFinalizacion(pedido));
    }

    /**
     * 🎁 Calcula el total del pedido con la nueva estructura híbrida:
     * - Suma subtotales de DetallePedido (solo artículos INDIVIDUALES)
     * - Suma precios de DetallePedidoPromocion (combos completos)
     */
    private Double calcularTotalPedidoConPromociones(Pedido pedido) {
        double totalDetallesIndividuales = 0.0;
        double totalPromociones = 0.0;

        // Sumar solo detalles individuales (promociones tienen subtotal = 0.0)
        if (pedido.getDetalles() != null) {
            totalDetallesIndividuales = pedido.getDetalles().stream()
                    .mapToDouble(detalle -> detalle.getSubtotal() != null ? detalle.getSubtotal() : 0.0)
                    .sum();
        }

        // Sumar promociones completas
        if (pedido.getPromociones() != null) {
            totalPromociones = pedido.getPromociones().stream()
                    .mapToDouble(
                            promo -> promo.getPrecioTotalPromocion() != null ? promo.getPrecioTotalPromocion() : 0.0)
                    .sum();
        }

        return totalDetallesIndividuales + totalPromociones;
    }

    /**
     * 🆕 Verifica si un pedido contiene solo artículos insumo
     */
    public Boolean esPedidoSoloInsumos(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            return false;
        }

        return pedido.getDetalles().stream()
                .allMatch(detalle -> detalle.getArticulo() instanceof ArticuloInsumo);
    }

    /**
     * 🆕 Calcula la hora estimada de finalización mejorada según las nuevas reglas
     * de negocio
     */
    public LocalTime calcularHoraEstimadaFinalizacion(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            return LocalTime.now();
        }

        // Si todos son insumos, hora actual (sin preparación)
        if (esPedidoSoloInsumos(pedido)) {
            return LocalTime.now();
        }

        // Buscar el tiempo máximo de preparación entre ArticuloManufacturado
        Integer maxTiempoPreparacion = pedido.getDetalles().stream()
                .filter(detalle -> detalle.getArticulo() instanceof ArticuloManufacturado)
                .map(detalle -> (ArticuloManufacturado) detalle.getArticulo())
                .filter(manufacturado -> manufacturado.getTiempoEstimadoMinutos() != null)
                .mapToInt(ArticuloManufacturado::getTiempoEstimadoMinutos)
                .max()
                .orElse(0);

        // Agregar tiempo de delivery si corresponde
        Integer minutosDelivery = 0;
        if (pedido.getTipoEnvio() != null && TIPO_ENVIO_DELIVERY.equals(pedido.getTipoEnvio().getNombre())) {
            minutosDelivery = MINUTOS_DELIVERY;
        }

        return LocalTime.now().plusMinutes(maxTiempoPreparacion + minutosDelivery);
    }

    /**
     * 🆕 Aplica descuento del 10% para pedidos TAKE_AWAY
     */
    public Double aplicarDescuentoTakeAway(Double total, Pedido pedido) {
        if (total == null || pedido == null || pedido.getTipoEnvio() == null) {
            return total != null ? total : 0.0;
        }

        // Verificar si es TAKE_AWAY por ID o nombre
        boolean esTakeAway = TIPO_ENVIO_TAKE_AWAY_ID.equals(pedido.getTipoEnvio().getId()) ||
                TIPO_ENVIO_TAKE_AWAY.equals(pedido.getTipoEnvio().getNombre());

        if (esTakeAway) {
            return total * (1.0 - DESCUENTO_TAKE_AWAY);
        }

        return total;
    }

    // ...existing code...
}
