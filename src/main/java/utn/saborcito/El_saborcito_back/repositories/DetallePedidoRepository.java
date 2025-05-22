package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.DetallePedidoId;
import utn.saborcito.El_saborcito_back.models.Pedido;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> {
    List<DetallePedido> findByPedido(Pedido pedido);

    void deleteByPedido(Pedido pedido);
}
