package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloManufacturadoRepository extends JpaRepository<ArticuloManufacturado, Long> {

    // Métodos existentes actualizados para filtrar eliminados
    @Query("SELECT am FROM ArticuloManufacturado am WHERE am.categoria.id = :categoriaId AND am.eliminado = false")
    List<ArticuloManufacturado> findAllByCategoria_Id(@Param("categoriaId") Long categoriaId);

    // Métodos para elementos no eliminados
    @Query("SELECT am FROM ArticuloManufacturado am WHERE am.eliminado = false")
    List<ArticuloManufacturado> findAllNotDeleted();

    @Query("SELECT am FROM ArticuloManufacturado am WHERE am.id = :id AND am.eliminado = false")
    Optional<ArticuloManufacturado> findByIdNotDeleted(@Param("id") Long id);

    // Métodos para elementos eliminados
    @Query("SELECT am FROM ArticuloManufacturado am WHERE am.eliminado = true")
    List<ArticuloManufacturado> findAllDeleted();

    @Query("SELECT am FROM ArticuloManufacturado am WHERE am.id = :id AND am.eliminado = true")
    Optional<ArticuloManufacturado> findByIdDeleted(@Param("id") Long id);

    // Agregar estos métodos al final de la clase
    @Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion) AND am.eliminado = false")
    boolean existsByDenominacionIgnoreCase(@Param("denominacion") String denominacion);

    @Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion) AND am.id != :id AND am.eliminado = false")
    boolean existsByDenominacionIgnoreCaseAndIdNot(@Param("denominacion") String denominacion, @Param("id") Long id);

    // Opcional: Para validar contra todos (incluso eliminados)
    @Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion)")
    boolean existsByDenominacionIgnoreCaseIncludingDeleted(@Param("denominacion") String denominacion);

    @Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion) AND am.id != :id")
    boolean existsByDenominacionIgnoreCaseAndIdNotIncludingDeleted(@Param("denominacion") String denominacion, @Param("id") Long id);
}
