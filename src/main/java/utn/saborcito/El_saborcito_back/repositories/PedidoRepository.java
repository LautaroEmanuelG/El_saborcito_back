package utn.saborcito.El_saborcito_back.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Pedido;

import java.util.List;

@Repository

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByFechaPedidoBetween(LocalDate desde, LocalDate hasta);

    List<Pedido> findAllByCliente_IdAndFechaPedidoBetween(Long clienteId, LocalDate desde, LocalDate hasta);

    List<Pedido> findByEstadoNombreIn(List<String> nombres);

    List<Pedido> findByClienteId(Long clienteId);

}
