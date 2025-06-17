package utn.saborcito.El_saborcito_back.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.saborcito.El_saborcito_back.dto.FacturaDTO;
import utn.saborcito.El_saborcito_back.dto.FormaPagoDTO;
import utn.saborcito.El_saborcito_back.mappers.PedidoMapper;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.Estado;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.DatosMercadoPagoRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DatosMercadoPagoService {

    private final PedidoService pedidoService;
    private final FacturaService facturaService;
    private final PedidoRepository pedidoRepository;
    private final DatosMercadoPagoRepository datosMercadoPagoRepository;
    private final EstadoService estadoService;
    private final PedidoMapper pedidoMapper;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public Preference crearPreferenciaMP(Pedido pedido) throws MPException, MPApiException {
        PreferenceClient client = new PreferenceClient();
        List<PreferenceItemRequest> items = new ArrayList<>();

        // Agregar artículos del pedido
        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.getArticulo() != null && detalle.getArticulo().getPrecioVenta() != null) {
                    PreferenceItemRequest item = PreferenceItemRequest.builder()
                            .title(detalle.getArticulo().getDenominacion())
                            .quantity(detalle.getCantidad())
                            .unitPrice(BigDecimal.valueOf(detalle.getArticulo().getPrecioVenta()))
                            .currencyId("ARS")
                            .build();
                    items.add(item);
                }
            }
        }

        // Agregar promociones si existen
        if (pedido.getPromociones() != null && !pedido.getPromociones().isEmpty()) {
            for (var promocion : pedido.getPromociones()) {
                try {
                    String denominacion = null;
                    Double precio = null;

                    if (promocion.getPromocion() != null) {
                        denominacion = promocion.getPromocion().getDenominacion();
                        precio = promocion.getPromocion().getPrecioPromocional();
                    }

                    if (denominacion != null && precio != null) {
                        PreferenceItemRequest item = PreferenceItemRequest.builder()
                                .title("🎁 " + denominacion)
                                .quantity(1)
                                .unitPrice(BigDecimal.valueOf(precio))
                                .currencyId("ARS")
                                .build();
                        items.add(item);
                    }
                } catch (Exception e) {
                    // Silenciar errores de promociones individuales
                }
            }
        }

        if (items.isEmpty()) {
            throw new RuntimeException("No hay items válidos para crear la preferencia de MP");
        }

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:5173/pedido-exitoso")
                .failure("http://localhost:5173/pedido-fallido")
                .pending("http://localhost:5173/pedido-pendiente")
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .externalReference(pedido.getId().toString())
                .backUrls(backUrls)
                .build();

        return client.create(request);
    }

    @Transactional
    public void procesarPago(String paymentIdStr) throws MPException, MPApiException, IOException {
        PaymentClient client = new PaymentClient();
        Payment pago = client.get(Long.parseLong(paymentIdStr));

        if (!"approved".equals(pago.getStatus())) {
            throw new RuntimeException("El pago no fue aprobado");
        }

        Long pedidoId = Long.parseLong(pago.getExternalReference());
        Pedido pedido = pedidoService.findEntityById(pedidoId);

        // Guardar info del pago
        DatosMercadoPago datos = DatosMercadoPago.builder()
                .dateCreate(pago.getDateCreated().toLocalDate())
                .dateApproved(pago.getDateApproved().toLocalDate())
                .dateLastUpdate(pago.getDateLastUpdated().toLocalDate())
                .paymentType(pago.getPaymentTypeId())
                .paymentMethod(pago.getPaymentMethodId())
                .status(pago.getStatus())
                .statusDetail(pago.getStatusDetail())
                .build();

        // Generar la factura
        Long facturaId = facturaService.save(
                FacturaDTO.builder()
                        .pedido(pedidoMapper.toDTO(pedido))
                        .formaPago(FormaPagoDTO.builder().id(pedido.getFormaPago().getId()).build())
                        .totalVenta(pedido.getTotal())
                        .fechaFacturacion(LocalDate.now())
                        .clienteEmail(pedido.getCliente().getEmail())
                        .build()
        ).getId();

        Factura factura = new Factura();
        factura.setId(facturaId);
        datos.setFactura(factura);

        // Cambiar estado del pedido
        Estado confirmado = estadoService.findByNombre("CONFIRMADO");
        pedido.setEstado(confirmado);
        pedidoRepository.save(pedido);

        datosMercadoPagoRepository.save(datos);
    }
}