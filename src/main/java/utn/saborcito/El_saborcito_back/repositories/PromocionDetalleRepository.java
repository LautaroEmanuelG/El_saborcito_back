package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.PromocionDetalle;

import java.util.List;

@Repository
public interface PromocionDetalleRepository extends JpaRepository<PromocionDetalle, Long> {

    List<PromocionDetalle> findByPromocionId(Long promocionId);

    List<PromocionDetalle> findByArticuloId(Long articuloId);
}
