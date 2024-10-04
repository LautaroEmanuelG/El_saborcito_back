package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.models.Valor;
import utn.saborcito.El_saborcito_back.repositories.ValorRepository;

import java.util.List;

@Service
public class ValorService {
    @Autowired
    private ValorRepository valorRepository;

    public Valor buscarValorPorId(Long id) {
        return valorRepository.findById(id).orElse(null);
    }

    @Transactional
    public Valor guardarValor(Valor valor) {
        return valorRepository.save(valor);
    }

    @Transactional
    public void eliminarValorPorId(Long id) {
        valorRepository.deleteById(id);
    }
}
