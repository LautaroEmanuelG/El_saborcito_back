package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;

import java.util.List;

@Repository

public interface ArticuloManufacturadoDetalleRepository extends JpaRepository<ArticuloManufacturadoDetalle, Long> {

    /**

     Busca todos los detalles de artículos manufacturados que usan un insumo específico
     Solo incluye artículos manufacturados no eliminados*/@Query("SELECT d FROM ArticuloManufacturadoDetalle d " +"WHERE d.articuloInsumo.id = :insumoId " +"AND d.articuloManufacturado.eliminado = false")
    List<ArticuloManufacturadoDetalle> findByArticuloInsumoIdAndEliminadoFalse(@Param("insumoId") Long insumoId);
}