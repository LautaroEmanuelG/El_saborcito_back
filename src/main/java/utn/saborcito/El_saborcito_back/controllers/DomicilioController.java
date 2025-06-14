package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;
import utn.saborcito.El_saborcito_back.services.DomicilioService;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
@RequiredArgsConstructor
public class DomicilioController {

    private final DomicilioService domicilioService;

    // POST → Crear domicilio
    @PostMapping("/usuarios/{usuarioId}/crear-domicilio")
    public ResponseEntity<DomicilioDTO> agregarDomicilio(
            @PathVariable Long usuarioId,
            @RequestBody DomicilioDTO dto) {
        DomicilioDTO creado = domicilioService.crearDomicilio(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT → Actualizar domicilio existente
    @PutMapping("/usuarios/{usuarioId}/update/{domicilioId}")
    public ResponseEntity<DomicilioDTO> actualizarDomicilio(
            @PathVariable Long usuarioId,
            @PathVariable Long domicilioId,
            @RequestBody DomicilioDTO dto) {
        DomicilioDTO actualizado = domicilioService.actualizarDomicilio(usuarioId, domicilioId, dto);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE → Eliminar domicilio
    @DeleteMapping("/usuarios/delete/{domicilioId}")
    public ResponseEntity<Void> eliminarDomicilio(
            @PathVariable Long usuarioId,
            @PathVariable Long domicilioId) {
        domicilioService.eliminarDomicilio(usuarioId, domicilioId);
        return ResponseEntity.noContent().build();
    }
}
