package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.CompraInsumoDetalle;

import java.util.List;

@Repository
public interface CompraInsumoDetalleRepository extends JpaRepository<CompraInsumoDetalle, Long> {
    List<CompraInsumoDetalle> findByCompraInsumoId(Long compraInsumoId);
}
