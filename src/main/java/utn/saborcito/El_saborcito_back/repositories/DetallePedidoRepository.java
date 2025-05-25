package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.Pedido;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido(Pedido pedido);

    void deleteByPedido(Pedido pedido);

    boolean existsByPedidoAndArticuloAndIdNot(Pedido pedido, Articulo articulo, Long id);

    Long countByPedidoAndArticulo(Pedido pedido, Articulo articulo);

    Long countByPedidoAndArticuloAndIdNot(Pedido pedido, Articulo articulo, Long id);

    List<DetallePedido> findAllByPedido_FechaPedidoBetween(LocalDate desde, LocalDate hasta);

}
