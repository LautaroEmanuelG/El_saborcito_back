package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;

/**
 * 📦 Servicio para el manejo de stock de insumos
 * Se enfoca únicamente en el descuento de stock.
 * Las validaciones se realizan previamente en PedidoServiceMejorado.
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final ArticuloInsumoRepository articuloInsumoRepository;

    /**
     * Descuenta stock para un pedido completo incluyendo promociones
     * NOTA: Se asume que la validación de stock ya fue realizada previamente
     *
     * @param pedido El pedido del cual descontar stock
     */
    @Transactional
    public void descontarStockPorPedido(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede descontar stock: el pedido no tiene detalles");
        }

        // Descontar stock de detalles normales
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getArticulo() instanceof ArticuloInsumo insumo) {
                descontarStockInsumo(insumo, detalle.getCantidad().doubleValue()); // ✅ Conversión a Double
            } else if (detalle.getArticulo() instanceof ArticuloManufacturado manufacturado) {
                descontarStockManufacturado(manufacturado, detalle.getCantidad().doubleValue()); // ✅ Conversión a
                                                                                                 // Double
            }
        }

        // Descontar stock adicional por promociones (si aplica)
        if (pedido.getPromociones() != null) {
            for (DetallePedidoPromocion promocion : pedido.getPromociones()) {
                descontarStockPorPromocion(promocion);
            }
        }
    }

    /**
     * 🎁 Descuenta stock específico por una promoción aplicada
     * NOTA: Actualmente las promociones no requieren descuento adicional
     * ya que el stock se maneja correctamente a través de los detalles normales
     *
     * @param promocion La promoción aplicada
     */
    private void descontarStockPorPromocion(DetallePedidoPromocion promocion) {
        // Placeholder para futuras necesidades de descuento específico por promociones
        // Actualmente el stock se descuenta correctamente a través de los detalles
        // normales del pedido
    }

    /**
     * Descuenta stock de un insumo
     *
     * @param insumo   El insumo
     * @param cantidad Cantidad a descontar
     */
    private void descontarStockInsumo(ArticuloInsumo insumo, Double cantidad) { // ✅ Cambio Integer a Double
        insumo.setStockActual(insumo.getStockActual() - cantidad);
        articuloInsumoRepository.save(insumo);
    }

    /**
     * Descuenta stock para un artículo manufacturado
     *
     * @param manufacturado El artículo manufacturado
     * @param cantidad      Cantidad de manufacturados a producir
     */
    private void descontarStockManufacturado(ArticuloManufacturado manufacturado, Double cantidad) { // ✅ Cambio Integer
                                                                                                     // a Double
        if (manufacturado.getArticuloManufacturadoDetalles() == null) {
            return;
        }

        for (ArticuloManufacturadoDetalle detalle : manufacturado.getArticuloManufacturadoDetalles()) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();
            Double cantidadADescontar = detalle.getCantidad() * cantidad; // ✅ Ya son Double ambos
            descontarStockInsumo(insumo, cantidadADescontar);
        }
    }
}