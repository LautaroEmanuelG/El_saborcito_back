// Agregar estos métodos al ArticuloManufacturadoRepository existente

@Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion)")
boolean existsByDenominacionIgnoreCase(@Param("denominacion") String denominacion);

@Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion) AND am.id != :id")
boolean existsByDenominacionIgnoreCaseAndIdNot(@Param("denominacion") String denominacion, @Param("id") Long id);

// Para validar solo entre no eliminados
@Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion) AND am.eliminado = false")
boolean existsByDenominacionIgnoreCaseAndNotDeleted(@Param("denominacion") String denominacion);

@Query("SELECT COUNT(am) > 0 FROM ArticuloManufacturado am WHERE LOWER(am.denominacion) = LOWER(:denominacion) AND am.id != :id AND am.eliminado = false")
boolean existsByDenominacionIgnoreCaseAndIdNotAndNotDeleted(@Param("denominacion") String denominacion, @Param("id") Long id);
