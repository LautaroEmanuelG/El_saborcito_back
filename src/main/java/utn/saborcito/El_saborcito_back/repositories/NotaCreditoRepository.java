package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.NotaCredito;

@Repository

public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Long> {}
