package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Estado;

import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNombre(String nombre);
}
