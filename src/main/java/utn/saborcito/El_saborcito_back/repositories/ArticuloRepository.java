package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Articulo;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    // Métodos para elementos no eliminados
    @Query("SELECT a FROM Articulo a WHERE a.eliminado = false")
    List<Articulo> findAllNotDeleted();

    @Query("SELECT a FROM Articulo a WHERE a.id = :id AND a.eliminado = false")
    Optional<Articulo> findByIdNotDeleted(@Param("id") Long id);

    // Métodos para elementos eliminados
    @Query("SELECT a FROM Articulo a WHERE a.eliminado = true")
    List<Articulo> findAllDeleted();

    @Query("SELECT a FROM Articulo a WHERE a.id = :id AND a.eliminado = true")
    Optional<Articulo> findByIdDeleted(@Param("id") Long id);
}
