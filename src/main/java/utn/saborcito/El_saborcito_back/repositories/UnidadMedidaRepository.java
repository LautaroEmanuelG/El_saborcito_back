package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Long> {

    // Buscar solo las no eliminadas
    @Query("SELECT u FROM UnidadMedida u WHERE u.eliminado = false")
    List<UnidadMedida> findAllActive();

    // Buscar por ID solo si no está eliminada
    @Query("SELECT u FROM UnidadMedida u WHERE u.id = :id AND u.eliminado = false")
    Optional<UnidadMedida> findByIdActive(Long id);

    // Buscar por denominación (case insensitive) excluyendo eliminadas
    @Query("SELECT u FROM UnidadMedida u WHERE LOWER(u.denominacion) = LOWER(:denominacion) AND u.eliminado = false")
    Optional<UnidadMedida> findByDenominacionIgnoreCaseAndNotDeleted(String denominacion);

    // Buscar por denominación excluyendo una ID específica (para updates)
    @Query("SELECT u FROM UnidadMedida u WHERE LOWER(u.denominacion) = LOWER(:denominacion) AND u.id != :id AND u.eliminado = false")
    Optional<UnidadMedida> findByDenominacionIgnoreCaseAndNotDeletedAndNotId(String denominacion, Long id);

    // Buscar todas (incluyendo eliminadas)
    List<UnidadMedida> findAll();
}