package utn.saborcito.El_saborcito_back.controllers;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.services.DatosMercadoPagoService;
import utn.saborcito.El_saborcito_back.services.PedidoService;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class DatosMercadoPagoController {

    private final DatosMercadoPagoService datosMercadoPagoService;
    private final PedidoService pedidoService;

    @PostMapping("/crear-preferencia/{pedidoId}")
    public ResponseEntity<?> crearPreferencia(@PathVariable Long pedidoId) {
        try {
            var pedido = pedidoService.findEntityById(pedidoId);
            Preference preference = datosMercadoPagoService.crearPreferenciaMP(pedido);

            return ResponseEntity.ok().body(
                    java.util.Map.of(
                            "preferenceId", preference.getId(),
                            "initPoint", preference.getInitPoint()
                    )
            );

        } catch (MPException | MPApiException e) {
            return ResponseEntity.badRequest().body("Error con Mercado Pago: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> recibirWebhook(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "data.id", required = false) String paymentId
    ) {
        if ("payment".equals(type) && paymentId != null) {
            try {
                datosMercadoPagoService.procesarPago(paymentId);
                return ResponseEntity.ok().body("Webhook procesado correctamente");
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Error procesando el webhook: " + e.getMessage());
            }
        }

        return ResponseEntity.badRequest().body("Tipo de webhook no procesado o datos faltantes");
    }
}