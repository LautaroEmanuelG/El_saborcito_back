package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.models.Transaccion;
import utn.saborcito.El_saborcito_back.repositories.TransaccionRepository;

import java.util.List;

@Service
public class TransaccionService {
    @Autowired
    private TransaccionRepository transaccionRepository;

    public List<Transaccion> listarTransacciones() {
        return transaccionRepository.findAll();
    }

    @Transactional
    public Transaccion guardarTransaccion(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }
}