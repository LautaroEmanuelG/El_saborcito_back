package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.HistorialPedido;
import utn.saborcito.El_saborcito_back.models.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {
    List<HistorialPedido> findByClienteOrderByFechaRegistroDesc(Cliente cliente);

    List<HistorialPedido> findByPedidoOrderByFechaRegistroDesc(Pedido pedido);

    Optional<HistorialPedido> findByClienteAndPedido(Cliente cliente, Pedido pedido);
}
