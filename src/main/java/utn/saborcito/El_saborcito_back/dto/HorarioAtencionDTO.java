package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioAtencionDTO {
    private Long id;
    private String diaSemana;
    private LocalTime apertura;
    private LocalTime cierre;
}
