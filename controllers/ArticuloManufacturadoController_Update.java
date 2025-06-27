// Agregar estos endpoints al ArticuloManufacturadoController existente

/**
 * Valida si existe un artículo manufacturado con la denominación dada
 */
@GetMapping("/validate-denominacion")
public ResponseEntity<Boolean> validateDenominacion(
        @RequestParam String denominacion,
        @RequestParam(required = false) Long excludeId) {
    boolean exists = service.existsByDenominacion(denominacion, excludeId);
    return ResponseEntity.ok(exists);
}

/**
 * Valida si existe un artículo manufacturado con la denominación dada (solo activos)
 */
@GetMapping("/validate-denominacion-active")
public ResponseEntity<Boolean> validateDenominacionActive(
        @RequestParam String denominacion,
        @RequestParam(required = false) Long excludeId) {
    boolean exists = service.existsByDenominacionActive(denominacion, excludeId);
    return ResponseEntity.ok(exists);
}
