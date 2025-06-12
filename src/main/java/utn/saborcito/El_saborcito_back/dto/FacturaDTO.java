package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaDTO {
    private Long id;
    private LocalDate fechaFacturacion;
    private Integer mpPaymentId;
    private Integer mpMerchantOrderId;
    private String mpPreferenceId;
    private String mpPaymentType;
    private Double montoTotalAcreditado;
    private Boolean ajustada;
    private FormaPagoDTO formaPago;
    private Double totalVenta;
    private PedidoDTO pedido; // Usamos PedidoDTO para evitar recursividad profunda
}
