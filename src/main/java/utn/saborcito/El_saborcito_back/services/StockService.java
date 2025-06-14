package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * 📦 Servicio para el manejo de stock de insumos
 * Centraliza el descuento de stock, pero usa ProduccionAnalisisService para
 * validaciones
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ProduccionAnalisisService produccionAnalisisService;

    /**
     * Descuenta stock para un pedido completo
     * 
     * @param pedido El pedido del cual descontar stock
     */
    @Transactional
    public void descontarStockPorPedido(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede descontar stock: el pedido no tiene detalles");
        }

        // Validar que hay suficiente stock usando el servicio unificado
        validarStockSuficiente(pedido);

        // Luego descontar
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getArticulo() instanceof ArticuloInsumo insumo) {
                descontarStockInsumo(insumo, detalle.getCantidad());
            } else if (detalle.getArticulo() instanceof ArticuloManufacturado manufacturado) {
                descontarStockManufacturado(manufacturado, detalle.getCantidad());
            }
        }
    }

    /**
     * Valida que hay suficiente stock antes de procesar el pedido
     * Usa el servicio unificado de producción para evitar duplicación
     * 
     * @param pedido El pedido a validar
     */
    private void validarStockSuficiente(Pedido pedido) {
        // Convertir el pedido a formato Map para usar el servicio unificado
        Map<Long, Integer> articulosMap = new HashMap<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            articulosMap.merge(detalle.getArticulo().getId(), detalle.getCantidad(), Integer::sum);
        }

        // Usar el servicio unificado para la validación
        var analisis = produccionAnalisisService.analizarProduccionCompleta(articulosMap);

        if (!analisis.isSePuedeProducirCompleto()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede procesar el pedido: stock insuficiente. " +
                            "Productos con problemas: " + analisis.getProductosConProblemas().size());
        }
    }

    /**
     * Descuenta stock de un insumo
     * 
     * @param insumo   El insumo
     * @param cantidad Cantidad a descontar
     */
    private void descontarStockInsumo(ArticuloInsumo insumo, Integer cantidad) {
        insumo.setStockActual(insumo.getStockActual() - cantidad);
        articuloInsumoRepository.save(insumo);
    }

    /**
     * Descuenta stock para un artículo manufacturado
     * 
     * @param manufacturado El artículo manufacturado
     * @param cantidad      Cantidad de manufacturados a producir
     */
    private void descontarStockManufacturado(ArticuloManufacturado manufacturado, Integer cantidad) {
        if (manufacturado.getArticuloManufacturadoDetalles() == null) {
            return;
        }

        for (ArticuloManufacturadoDetalle detalle : manufacturado.getArticuloManufacturadoDetalles()) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();
            Integer cantidadADescontar = detalle.getCantidad() * cantidad;
            descontarStockInsumo(insumo, cantidadADescontar);
        }
    }
}
