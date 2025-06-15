package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.DetallePedidoPromocion;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DetallePedidoPromocionRepository extends JpaRepository<DetallePedidoPromocion, Long> {

    List<DetallePedidoPromocion> findByPedidoId(Long pedidoId);

    List<DetallePedidoPromocion> findByPromocionId(Long promocionId);

    @Query("SELECT dpp FROM DetallePedidoPromocion dpp WHERE dpp.pedido.fechaPedido BETWEEN :desde AND :hasta")
    List<DetallePedidoPromocion> findAllByPedidoFechaPedidoBetween(
      @Param("desde") LocalDate desde,
      @Param("hasta") LocalDate hasta
    );
}
