package utn.saborcito.El_saborcito_back.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRankingDTO {
    private Long idCliente;
    private String nombreCompleto;
    private Long cantidadPedidos;
    private Double totalImporte;
}