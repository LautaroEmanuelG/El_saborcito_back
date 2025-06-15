package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.dto.DetalleFacturaRenderizadoDTO;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.DetallePedidoPromocion;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoPromocionRepository;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaRenderService {

    private final DetallePedidoPromocionRepository detallePedidoPromocionRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public void cargarDetallesPedido(Pedido pedido) {
        pedido.setDetalles(detallePedidoRepository.findByPedido(pedido));
    }

    public List<DetalleFacturaRenderizadoDTO> construirDetalleFactura(Pedido pedido) {
        // Asegura que los detalles estén cargados
        cargarDetallesPedido(pedido);
        List<DetalleFacturaRenderizadoDTO> items = new ArrayList<>();

        // Artículos individuales
        for (DetallePedido dp : pedido.getDetalles()) {
            if (dp.getOrigen() == OrigenDetalle.INDIVIDUAL && dp.getCantidadSinPromocion() > 0) {
                items.add(DetalleFacturaRenderizadoDTO.builder()
                        .descripcion(dp.getArticulo().getDenominacion())
                        .cantidad(dp.getCantidadSinPromocion())
                        .subtotal(dp.getSubtotal())
                        .esPromocion(false)
                        .build());
            }
        }

        // Promociones aplicadas
        List<DetallePedidoPromocion> promociones = detallePedidoPromocionRepository.findByPedidoId(pedido.getId());

        for (DetallePedidoPromocion promo : promociones) {
            List<String> nombresArticulos = promo.getPromocion().getPromocionDetalles().stream()
                .map(pd -> "- " + pd.getArticulo().getDenominacion() + " x" + pd.getCantidadRequerida())
                .toList();
            items.add(DetalleFacturaRenderizadoDTO.builder()
                    .descripcion("Promoción: " + promo.getPromocion().getDenominacion())
                    .cantidad(promo.getCantidadPromocion())
                    .subtotal(promo.getPrecioTotalPromocion())
                    .esPromocion(true)
                    .articulosIncluidos(nombresArticulos)
                    .build());
        }

        return items;
    }
}
