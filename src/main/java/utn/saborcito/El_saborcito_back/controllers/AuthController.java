package utn.saborcito.El_saborcito_back.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Este endpoint es público.";
    }

    @GetMapping("/private")
    public String privateEndpoint() {
        return "Este endpoint es privado.";
    }
}


