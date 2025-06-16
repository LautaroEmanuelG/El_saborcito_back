package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.services.CompraInsumoService;

import java.util.List;

@RestController
@RequestMapping("/api/compras-insumos")
@RequiredArgsConstructor
public class CompraInsumoController {

    private final CompraInsumoService service;

    @PostMapping
    public ResponseEntity<CompraInsumoDTO> create(@RequestBody NuevaCompraDTO dto) {
        CompraInsumoDTO created = service.registrarCompra(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CompraInsumoDTO>> all() {
        return ResponseEntity.ok(service.listarCompras());
    }
}
