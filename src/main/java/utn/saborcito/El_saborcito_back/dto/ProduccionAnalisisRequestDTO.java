package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para recibir la solicitud de análisis de producción
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduccionAnalisisRequestDTO {

    /**
     * Lista de artículos manufacturados y sus cantidades para analizar
     */
    private List<ArticuloCantidad> articulos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticuloCantidad {
        /**
         * ID del artículo manufacturado
         */
        private Long articuloId;

        /**
         * Cantidad deseada a producir
         */
        private Double cantidad;    // ✅ Cambio de Integer a Double
    }
}