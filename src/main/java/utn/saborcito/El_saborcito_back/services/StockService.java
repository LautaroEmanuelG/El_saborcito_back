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
 * Centraliza toda la lógica de descuento y validación de stock
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final ArticuloInsumoRepository articuloInsumoRepository;

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

        // Primero validar que hay suficiente stock
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
     * 
     * @param pedido El pedido a validar
     */
    private void validarStockSuficiente(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getArticulo() instanceof ArticuloInsumo insumo) {
                if (insumo.getStockActual() < detalle.getCantidad()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Stock insuficiente para " + insumo.getDenominacion() +
                                    ". Stock actual: " + insumo.getStockActual() +
                                    ", cantidad solicitada: " + detalle.getCantidad());
                }
            } else if (detalle.getArticulo() instanceof ArticuloManufacturado manufacturado) {
                validarStockManufacturado(manufacturado, detalle.getCantidad());
            }
        }
    }

    /**
     * Valida stock para artículo manufacturado
     * 
     * @param manufacturado      El artículo manufacturado
     * @param cantidadSolicitada Cantidad solicitada del manufacturado
     */
    private void validarStockManufacturado(ArticuloManufacturado manufacturado, Integer cantidadSolicitada) {
        if (manufacturado.getArticuloManufacturadoDetalles() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El artículo manufacturado " + manufacturado.getDenominacion() +
                            " no tiene componentes definidos");
        }

        for (ArticuloManufacturadoDetalle detalle : manufacturado.getArticuloManufacturadoDetalles()) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();
            Integer cantidadNecesaria = detalle.getCantidad() * cantidadSolicitada;

            if (insumo.getStockActual() < cantidadNecesaria) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente del insumo " + insumo.getDenominacion() +
                                " para fabricar " + manufacturado.getDenominacion() +
                                ". Stock actual: " + insumo.getStockActual() +
                                ", cantidad necesaria: " + cantidadNecesaria);
            }
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
