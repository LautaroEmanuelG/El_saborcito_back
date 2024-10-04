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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ticket")
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
    public ResponseEntity<Ticket> crearTicket(@RequestBody TicketDto ticketDto) {
        List<TicketProducto> ticketProductos = ticketDto.getProducts().stream()
                .map(pq -> {
                    Producto producto = productoService.buscarProductoPorId(pq.getProductId());
                    if (producto == null || producto.getStock() < pq.getQuantity()) {
                        throw new IllegalArgumentException("Producto no encontrado o stock insuficiente");
                    }
                    producto.setStock(producto.getStock() - pq.getQuantity());
                    productoService.guardarProducto(producto);

                    TicketProducto ticketProducto = new TicketProducto();
                    ticketProducto.setProducto(producto);
                    ticketProducto.setCantidad(pq.getQuantity());
                    return ticketProducto;
                })
                .collect(Collectors.toList());

        Ticket ticket = new Ticket();
        ticket.setTicketProductos(ticketProductos);
        ticket = ticketService.guardarTicket(ticket);

        for (TicketProducto ticketProducto : ticketProductos) {
            ticketProducto.setTicket(ticket);
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setTicket(ticket);
        transaccionService.guardarTransaccion(transaccion);

        return ResponseEntity.ok(ticket);
    }
}