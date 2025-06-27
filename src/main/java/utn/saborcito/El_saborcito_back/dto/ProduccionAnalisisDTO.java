package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 📊 DTO para el análisis de producción de artículos manufacturados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduccionAnalisisDTO {

    /**
     * ✅ Indica si se puede producir todo lo solicitado
     */
    private boolean sePuedeProducirCompleto;

    /**
     * 📝 Mensaje descriptivo del análisis
     */
    private String mensaje;

    /**
     * 📋 Lista de productos analizados
     */
    private List<ProductoAnalisis> productos;

    /**
     * ⚠️ Lista de productos que tienen problemas de producción
     */
    private List<ProductoConProblema> productosConProblemas;

    /**
     * 📉 Lista de insumos insuficientes
     */
    private List<InsumoInsuficiente> insumosInsuficientes;

    /**
     * 🛠️ Análisis de un producto específico
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductoAnalisis {
        private Long articuloId;
        private String denominacion;
        private Double cantidadSolicitada;    // ✅ Cambio de Integer a Double
        private Double cantidadPosible;       // ✅ Cambio de Integer a Double
        private boolean sePuedeProducir;
        private List<InsumoNecesario> insumosNecesarios;
    }

    /**
     * 🚫 Producto con problemas de producción
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductoConProblema {
        private Long articuloId;
        private String denominacion;
        private String motivoProblema;
        private Double cantidadSolicitada;      // ✅ Cambio de Integer a Double
        private Double cantidadMaximaPosible;   // ✅ Cambio de Integer a Double
    }

    /**
     * 📦 Insumo necesario para producción
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InsumoNecesario {
        private Long insumoId;
        private String denominacion;
        private Double cantidadNecesaria;
        private Double stockDisponible;
        private String unidadMedida;
        private boolean esSuficiente;
    }

    /**
     * ⚠️ Insumo con stock insuficiente
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InsumoInsuficiente {
        private Long insumoId;
        private String denominacion;
        private Double cantidadNecesaria;
        private Double stockDisponible;
        private Double stockFaltante;
        private String unidadMedida;
    }

    /**
     * 🎁 DTO para validación de promociones específicas
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromocionValidacionDTO {
        private Long promocionId;
        private String promocionDenominacion;
        private Double cantidadSolicitada;        // ✅ Cambio de Integer a Double
        private Double cantidadMaximaAplicable;   // ✅ Cambio de Integer a Double
        private boolean esValida;
        private String motivoNoValida;
        private List<ProductoConProblema> productosConProblemas;
        private List<InsumoInsuficiente> insumosInsuficientes;
        private Double precioTotalPromocion;
        private Double ahorroTotal;
    }
}