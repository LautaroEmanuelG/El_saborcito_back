package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;

import java.util.List;

@Repository

public interface ArticuloManufacturadoRepository extends JpaRepository<ArticuloManufacturado, Long> {
    List<ArticuloManufacturado> findAllByCategoria_Id(Long categoriaId);
}
