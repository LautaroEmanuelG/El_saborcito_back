package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.HistorialPedido;
import utn.saborcito.El_saborcito_back.models.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {

    /**
     * 🔍 Obtiene el historial de un cliente con joins optimizados
     * Evita consultas N+1 cargando todas las relaciones necesarias de una vez
     */
    @Query("""
            SELECT h FROM HistorialPedido h
            LEFT JOIN FETCH h.cliente c
            LEFT JOIN FETCH h.pedido p
            LEFT JOIN FETCH p.estado e
            LEFT JOIN FETCH p.tipoEnvio te
            LEFT JOIN FETCH p.formaPago fp
            LEFT JOIN FETCH p.sucursal s
            WHERE h.cliente.id = :clienteId
            ORDER BY h.fechaRegistro DESC
            """)
    List<HistorialPedido> findByClienteIdWithDetailsOptimized(@Param("clienteId") Long clienteId);

    // Métodos originales para mantener compatibilidad
    List<HistorialPedido> findByClienteOrderByFechaRegistroDesc(Cliente cliente);

    List<HistorialPedido> findByPedidoOrderByFechaRegistroDesc(Pedido pedido);

    Optional<HistorialPedido> findByClienteAndPedido(Cliente cliente, Pedido pedido);
}
