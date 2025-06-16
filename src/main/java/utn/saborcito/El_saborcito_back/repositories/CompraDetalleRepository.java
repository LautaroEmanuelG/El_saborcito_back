package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.saborcito.El_saborcito_back.models.CompraDetalle;

public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, Long> {
}
