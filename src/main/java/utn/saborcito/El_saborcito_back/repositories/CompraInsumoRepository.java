// src/main/java/utn/saborcito/El_saborcito_back/repositories/CompraInsumoRepository.java
package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.saborcito.El_saborcito_back.models.CompraInsumo;

public interface CompraInsumoRepository extends JpaRepository<CompraInsumo, Long> {
}
