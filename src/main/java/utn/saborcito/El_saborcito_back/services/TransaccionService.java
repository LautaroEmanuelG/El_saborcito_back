package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.repositories.TransaccionRepository;

@Service
public class TransaccionService {
    @Autowired
    private TransaccionRepository transaccionRepository;

    @Transactional
    public Transaccion guardarTransaccion(Transaccion transaccion) {
        if (transaccion.getTicket().getTotal() > 0) {
            transaccion.setTipo(TransaccionTipo.INGRESO);
        } else {
            transaccion.setTipo(TransaccionTipo.GASTO);
        }
        return transaccionRepository.save(transaccion);
    }
}