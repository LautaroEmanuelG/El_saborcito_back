package utn.saborcito.El_saborcito_back.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {
    private Long id;
    private String nombre;
    private DomicilioDTO domicilio;
    private EmpresaDTO empresa;
    private List<HorarioAtencionDTO> horarios;
}
