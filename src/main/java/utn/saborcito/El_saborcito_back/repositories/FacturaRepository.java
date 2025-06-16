package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Factura;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByPedidoId(Long pedidoId);

    @Query("""
    SELECT f FROM Factura f
    JOIN FETCH f.pedido p
    LEFT JOIN FETCH p.detalles d
    LEFT JOIN FETCH d.articulo a
    WHERE f.id = :id
    """)
    Optional<Factura> findByIdWithPedidoAndDetalles(@Param("id") Long id);
}
