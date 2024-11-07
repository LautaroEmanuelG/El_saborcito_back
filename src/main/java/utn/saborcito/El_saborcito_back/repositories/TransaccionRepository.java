package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Transaccion;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // Método para encontrar una transacción por preferenciaId
    Optional<Transaccion> findByPreferenciaId(String preferenciaId);
}