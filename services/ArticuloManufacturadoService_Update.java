// Agregar estos métodos al ArticuloManufacturadoService existente

/**
 * Valida si existe un artículo manufacturado con la denominación dada
 * @param denominacion denominación a validar
 * @param excludeId ID a excluir de la validación (para edición)
 * @return true si existe, false si no existe
 */
public boolean existsByDenominacion(String denominacion, Long excludeId) {
    if (denominacion == null || denominacion.trim().isEmpty()) {
        return false;
    }
    
    String cleanDenominacion = denominacion.trim();
    
    if (excludeId != null) {
        return repo.existsByDenominacionIgnoreCaseAndIdNot(cleanDenominacion, excludeId);
    } else {
        return repo.existsByDenominacionIgnoreCase(cleanDenominacion);
    }
}

/**
 * Valida si existe un artículo manufacturado activo con la denominación dada
 * @param denominacion denominación a validar
 * @param excludeId ID a excluir de la validación (para edición)
 * @return true si existe, false si no existe
 */
public boolean existsByDenominacionActive(String denominacion, Long excludeId) {
    if (denominacion == null || denominacion.trim().isEmpty()) {
        return false;
    }
    
    String cleanDenominacion = denominacion.trim();
    
    if (excludeId != null) {
        return repo.existsByDenominacionIgnoreCaseAndIdNotAndNotDeleted(cleanDenominacion, excludeId);
    } else {
        return repo.existsByDenominacionIgnoreCaseAndNotDeleted(cleanDenominacion);
    }
}

/**
 * Valida denominación antes de guardar o actualizar
 */
private void validateDenominacion(String denominacion, Long excludeId) {
    if (denominacion == null || denominacion.trim().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "La denominación es obligatoria");
    }
    
    if (existsByDenominacionActive(denominacion, excludeId)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, 
            "Ya existe un artículo manufacturado con esta denominación");
    }
}
