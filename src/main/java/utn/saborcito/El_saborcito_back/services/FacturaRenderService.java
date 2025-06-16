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
        // Solución: limpiar y agregar, no reemplazar la lista
        List<DetallePedido> nuevosDetalles = detallePedidoRepository.findByPedido(pedido);
        pedido.getDetalles().clear();
        pedido.getDetalles().addAll(nuevosDetalles);
    }

    public List<DetalleFacturaRenderizadoDTO> construirDetalleFactura(Pedido pedido) {
        // Asegura que los detalles estén cargados
        cargarDetallesPedido(pedido);
        List<DetalleFacturaRenderizadoDTO> items = new ArrayList<>();

        // Detalles individuales
        for (DetallePedido dp : pedido.getDetalles()) {
            if (dp.getOrigen() == OrigenDetalle.INDIVIDUAL) {
                items.add(DetalleFacturaRenderizadoDTO.builder()
                        .descripcion(dp.getArticulo().getDenominacion())
                        .cantidad(dp.getCantidadSinPromocion())
                        .subtotal(dp.getSubtotal())
                        .esPromocion(false)
                        .build());
            }
        }

        // Detalles de promociones
        List<DetallePedidoPromocion> promociones = detallePedidoPromocionRepository.findByPedidoId(pedido.getId());

        for (DetallePedidoPromocion dpp : promociones) {
            List<String> nombresArticulos = dpp.getPromocion().getPromocionDetalles().stream()
                    .map(pd -> "- " + pd.getArticulo().getDenominacion() + " x" + pd.getCantidadRequerida())
                    .toList();

            items.add(DetalleFacturaRenderizadoDTO.builder()
                    .descripcion("🎁 Promoción: " + dpp.getPromocion().getDenominacion())
                    .cantidad(dpp.getCantidadPromocion())
                    .subtotal(dpp.getPrecioTotalPromocion())
                    .esPromocion(true)
                    .articulosIncluidos(nombresArticulos)
                    .build());
        }

        return items;
    }
}
