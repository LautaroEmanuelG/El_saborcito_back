package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import utn.saborcito.El_saborcito_back.dto.TicketDto;
import utn.saborcito.El_saborcito_back.models.Producto;
import utn.saborcito.El_saborcito_back.models.Ticket;
import utn.saborcito.El_saborcito_back.models.TicketProducto;
import utn.saborcito.El_saborcito_back.models.Transaccion;
import utn.saborcito.El_saborcito_back.services.ProductoService;
import utn.saborcito.El_saborcito_back.services.TicketService;
import utn.saborcito.El_saborcito_back.services.TransaccionService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> listarTickets() {
        return ResponseEntity.ok(ticketService.listarTickets());
    }

    @PostMapping("/nuevo")
    @Transactional
    public ResponseEntity<?> guardarTicket(@RequestBody TicketDto ticket) {
        return ResponseEntity.ok(ticketService.guardarTicket(ticket));
    }
}