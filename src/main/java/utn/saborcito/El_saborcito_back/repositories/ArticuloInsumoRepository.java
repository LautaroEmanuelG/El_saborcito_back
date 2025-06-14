package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloInsumoRepository extends JpaRepository<ArticuloInsumo, Long> {

    // Métodos existentes actualizados para filtrar eliminados
    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.esParaElaborar = true AND ai.eliminado = false")
    List<ArticuloInsumo> findAllByEsParaElaborarTrue();

    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.esParaElaborar = false AND ai.eliminado = false")
    List<ArticuloInsumo> findAllByEsParaElaborarFalse();

    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.categoria.id = :categoriaId AND ai.eliminado = false")
    List<ArticuloInsumo> findAllByCategoria_Id(@Param("categoriaId") Long categoriaId);

    // NUEVO: Traer todos los insumos de una categoría (sin importar si están eliminados)
    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.categoria.id = :categoriaId")
    List<ArticuloInsumo> findAllByCategoria_IdAll(@Param("categoriaId") Long categoriaId);

    // Métodos para elementos no eliminados
    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.eliminado = false")
    List<ArticuloInsumo> findAllNotDeleted();

    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.id = :id AND ai.eliminado = false")
    Optional<ArticuloInsumo> findByIdNotDeleted(@Param("id") Long id);

    // Métodos para elementos eliminados
    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.eliminado = true")
    List<ArticuloInsumo> findAllDeleted();

    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.id = :id AND ai.eliminado = true")
    Optional<ArticuloInsumo> findByIdDeleted(@Param("id") Long id);
}
