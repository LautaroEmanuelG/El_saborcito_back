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
public class DatosMercadoPagoDto {
    private Long id;
    private LocalDate dateCreate;
    private LocalDate dateApproved;
    private LocalDate dateLastUpdate;
    // private String paymentType;
    // private String paymentMethod;
    private String status;
    private String statusDetail;
    private Long factura;
}
