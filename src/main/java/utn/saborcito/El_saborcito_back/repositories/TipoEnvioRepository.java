package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.TipoEnvio;

import java.util.Optional;

@Repository
public interface TipoEnvioRepository extends JpaRepository<TipoEnvio, Long> {
    Optional<TipoEnvio> findByNombre(String nombre);
}
