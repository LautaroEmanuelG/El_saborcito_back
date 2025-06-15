package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Categoria;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByDenominacionIgnoreCase(String denominacion);

    // Subcategorías directas de una categoría padre
    List<Categoria> findAllByTipoCategoria_Id(Long tipoCategoriaId);

    // NUEVO: Solo categorías activas (no eliminadas)
    List<Categoria> findAllByEliminadoFalse();

    // NUEVO: Solo categorías eliminadas
    List<Categoria> findAllByEliminadoTrue();
}
