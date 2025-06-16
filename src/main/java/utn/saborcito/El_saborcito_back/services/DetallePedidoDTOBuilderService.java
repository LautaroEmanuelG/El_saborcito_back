package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.DetallePedidoPromocion;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoPromocionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoDTOBuilderService {

    private final DetallePedidoPromocionRepository detallePromocionRepo;

    public List<DetallePedidoDTO> construirDetalles(Pedido pedido) {
        List<DetallePedidoDTO> detallesDTO = new ArrayList<>();

        for (DetallePedido dp : pedido.getDetalles()) {
            if (dp.getOrigen() == OrigenDetalle.INDIVIDUAL) {
                detallesDTO.add(new DetallePedidoDTO(
                        dp.getId(),
                        dp.getCantidad(),
                        dp.getCantidadConPromocion(),
                        dp.getCantidadSinPromocion(),
                        dp.getSubtotal(),
                        dp.getOrigen(),
                        dp.getPromocionOrigenId(),
                        new ArticuloDTO(
                                dp.getArticulo().getId(),
                                dp.getArticulo().getDenominacion(),
                                dp.getArticulo().getPrecioVenta(),
                                null, null, false, null
                        )
                ));
            }
        }

        List<DetallePedidoPromocion> promociones = detallePromocionRepo.findByPedidoId(pedido.getId());

        for (DetallePedidoPromocion dpp : promociones) {
            Promocion promo = dpp.getPromocion();
            detallesDTO.add(new DetallePedidoDTO(
                    dpp.getId(),
                    dpp.getCantidadPromocion(),
                    null,
                    null,
                    dpp.getPrecioTotalPromocion(),
                    OrigenDetalle.PROMOCION,
                    promo.getId(),
                    new ArticuloDTO(
                            promo.getId(),
                            "🎁 " + promo.getDenominacion(),
                            promo.getPrecioPromocional(),
                            null, null, false, null
                    )
            ));
        }

        return detallesDTO;
    }
}
