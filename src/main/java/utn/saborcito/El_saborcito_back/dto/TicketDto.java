package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketDto {
    private List<TicketProductoDto> productos;
}
