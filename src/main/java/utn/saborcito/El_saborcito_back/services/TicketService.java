package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.models.Ticket;
import utn.saborcito.El_saborcito_back.repositories.TicketRepository;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> listarTickets() {
        return ticketRepository.findAll();
    }
    @Transactional
    public Ticket guardarTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
}