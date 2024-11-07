package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.UserPreticket;
import utn.saborcito.El_saborcito_back.services.MercadoPagoService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mp")
@CrossOrigin(origins = "http://localhost:5173")  // Asegúrate de que el frontend esté autorizado
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/crear-preferencia")
    public Map<String, String> crearPreferencia(@RequestBody UserPreticket preticket) {
        Map<String, String> response = new HashMap<>();
        try {
            // Obtenemos el init_point y lo agregamos a la respuesta
            String initPoint = mercadoPagoService.crearPreferenciaPago(preticket);
            response.put("init_point", initPoint);
        } catch (Exception e) {
            response.put("error", "Error al crear la preferencia de pago: " + e.getMessage());
        }
        return response;
    }
}