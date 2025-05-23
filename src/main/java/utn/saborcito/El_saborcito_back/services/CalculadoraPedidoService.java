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

/**
 * Servicio dedicado al cálculo de valores en pedidos.
 * Este servicio centraliza la lógica de cálculo para los pedidos y sus
 * detalles.
 */
@Service
@RequiredArgsConstructor
public class CalculadoraPedidoService {

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
     * Actualiza los totales de un pedido (total y totalCosto).
     * 
     * @param pedido El pedido a actualizar
     */
    public void actualizarTotalesPedido(Pedido pedido) {
        pedido.setTotal(calcularTotalPedido(pedido));
        pedido.setTotalCosto(calcularTotalCostoPedido(pedido));
    }
}
