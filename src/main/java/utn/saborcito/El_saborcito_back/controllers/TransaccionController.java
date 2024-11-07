package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.models.Transaccion;
import utn.saborcito.El_saborcito_back.services.TransaccionService;

import java.util.List;

@RestController
@RequestMapping("/api/transaccion")
@CrossOrigin(origins = "http://localhost:5173")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/all")
    public ResponseEntity<List<Transaccion>> listarTransacciones() {
        return ResponseEntity.ok(transaccionService.listarTransacciones());
    }
}