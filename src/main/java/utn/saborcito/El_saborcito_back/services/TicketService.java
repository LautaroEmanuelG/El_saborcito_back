package utn.saborcito.El_saborcito_back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.TicketDto;
import utn.saborcito.El_saborcito_back.enums.TransaccionTipo;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.ProductoRepository;
import utn.saborcito.El_saborcito_back.repositories.TicketRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TransaccionService transaccionService;

    public List<Ticket> listarTickets() {
        return ticketRepository.findAll();
    }

    @Transactional
    public Ticket guardarTicket(TicketDto ticketDto) {
        List<TicketProducto> ticketProductos = ticketDto.getProductos().stream()
                .map(pq -> {
                    Producto producto = productoService.buscarProductoPorId(pq.getProductoId());
                    if (producto == null || producto.getStock() < pq.getCantidad()) {
                        throw new IllegalArgumentException("Producto no encontrado o stock insuficiente");
                    }
                    producto.setStock(producto.getStock() - pq.getCantidad());
                    productoRepository.save(producto);

                    TicketProducto ticketProducto = new TicketProducto();
                    ticketProducto.setProducto(producto);
                    ticketProducto.setCantidad(pq.getCantidad());
                    return ticketProducto;
                })
                .collect(Collectors.toList());

        double total = ticketProductos.stream()
                .mapToDouble(tp -> tp.getProducto().getValor().getPrecio() * tp.getCantidad())
                .sum();

        Ticket ticket = new Ticket();
        ticket.setFecha(new Date());
        ticket.setTotal(total);
        ticket.setTicketProductos(ticketProductos);
        ticket = ticketRepository.save(ticket);

        for (TicketProducto ticketProducto : ticketProductos) {
            ticketProducto.setTicket(ticket);
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setTicket(ticket);
        transaccion.setTipo(TransaccionTipo.valueOf(ticketDto.getPago().toUpperCase())); // Set the transaction type based on payment method
        transaccionService.guardarTransaccion(transaccion);

        return ticket;
    }
}