package utn.saborcito.El_saborcito_back.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.PedidoDTO;
import utn.saborcito.El_saborcito_back.dto.PedidoCreacionDTO;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.services.PedidoService;
import utn.saborcito.El_saborcito_back.services.PedidoServiceMejorado;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService service;
    private final PedidoServiceMejorado serviceMejorado;

    @GetMapping
    public List<PedidoDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<PedidoDTO> getPedidosByCliente(@PathVariable Long clienteId) {
        return service.findByClienteId(clienteId);
    }

    @GetMapping("/{id}")
    public PedidoDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PedidoServiceMejorado.PedidoConFacturaDTO create(@RequestBody PedidoCreacionDTO pedidoDTO) {
        return serviceMejorado.crearPedidoCompleto(pedidoDTO);
    }

    @PutMapping("/{id}")
    public PedidoDTO update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.update(id, pedido);
    }

    @PutMapping("/{id}/recalcular")
    public PedidoDTO recalcularTotal(@PathVariable Long id) {
        return service.recalcularTotal(id);
    }

    /**
     * Endpoint para cancelar un pedido
     * 
     //* @param id     ID del pedido a cancelar
     //* @param motivo Razón de la cancelación (opcional)
   //  * @return El pedido cancelado como DTO
     */
    // @PutMapping("/{id}/cancelar")
    // public PedidoDTO cancelarPedido(@PathVariable Long id, @RequestParam(required = false) String motivo) {
    //     return service.cancelarPedido(id, motivo);
    // }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * Endpoint para validar promociones antes de crear un pedido
     * 
     * @param promocionIds Lista de IDs de promociones a validar
     * @return Mapa con el estado de cada promoción
     */
    @PostMapping("/validar-promociones")
    public ResponseEntity<?> validarPromociones(@RequestBody List<Long> promocionIds) {
        try {
            return ResponseEntity.ok(serviceMejorado.validarPromociones(promocionIds));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error validando promociones: " + e.getMessage());
        }
    }
}
