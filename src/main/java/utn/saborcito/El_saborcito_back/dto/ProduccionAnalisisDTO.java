package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO para devolver el resultado del análisis de producción de varios artículos
 * manufacturados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduccionAnalisisDTO {

    /**
     * Indica si se pueden producir todos los artículos solicitados
     */
    private boolean sePuedeProducirCompleto;

    /**
     * Lista de IDs de productos que no se pueden fabricar
     */
    private List<ProductoProblemaDTO> productosConProblemas;

    /**
     * Mapa con la cantidad máxima que se puede producir de cada artículo
     * Key: ID del artículo manufacturado
     * Value: Cantidad máxima que se puede producir
     */
    private Map<Long, Integer> maximoProducible;

    /**
     * Insumos insuficientes que están causando las limitaciones
     */
    private List<InsumoInsuficienteDTO> insumosInsuficientes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductoProblemaDTO {
        private Long id;
        private String denominacion;
        private Integer cantidadSolicitada;
        private Integer cantidadProducible;
        private List<InsumoInsuficienteDTO> insumosInsuficientes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InsumoInsuficienteDTO {
        private Long id;
        private String denominacion;
        private Integer stockActual;
        private Integer stockRequerido;
        private Integer stockFaltante;
    }
}
