package utn.saborcito.El_saborcito_back.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.models.Transaccion;
import utn.saborcito.El_saborcito_back.models.UserPreticket;
import utn.saborcito.El_saborcito_back.repositories.TransaccionRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercado.pago.token}")
    private String mercadoPagoToken;

    private final TransaccionRepository transaccionRepository;

    public MercadoPagoService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    public String crearPreferenciaPago(UserPreticket preticket) throws MPException, MPApiException {
        // Configuramos el token de acceso
        MercadoPagoConfig.setAccessToken(mercadoPagoToken);

        // 1. Crear el ítem basado en el preticket
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(preticket.getDescripcionProducto())
                .quantity(preticket.getCantidad())
                .unitPrice(new BigDecimal(preticket.getPrecioUnitario()))  // Conversión de int a BigDecimal
                .currencyId("ARS")  // Moneda
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // 2. Configurar las URLs de retorno
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://pivigames.blog/") // URL de éxito
                .pending("https://www.zona-leros.com/") // URL de pendiente
                .failure("https://www.twitch.tv/") // URL de fallo
                .build();

        // 3. Crear la preferencia de pago
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        // 4. Guardar la transacción pendiente sin impactar Ticket aún
        Transaccion transaccion = new Transaccion();
        transaccion.setEstado("PENDIENTE");
        transaccion.setPreferenciaId(preference.getId());
        transaccionRepository.save(transaccion);

        // 5. Retornar el init_point para redirigir al usuario al checkout
        return preference.getInitPoint();  // Aquí retornamos el init_point
    }
}