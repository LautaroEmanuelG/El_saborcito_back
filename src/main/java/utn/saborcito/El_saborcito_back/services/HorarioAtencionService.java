package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.dto.HorarioAtencionDTO;
import utn.saborcito.El_saborcito_back.mappers.HorarioAtencionMapper;
import utn.saborcito.El_saborcito_back.models.HorarioAtencion;
import utn.saborcito.El_saborcito_back.repositories.HorarioAtencionRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioAtencionService {

    private final HorarioAtencionRepository horarioAtencionRepository;
    private final HorarioAtencionMapper horarioAtencionMapper;

    public List<HorarioAtencionDTO> getHorariosDeSucursal(Long sucursalId) {
        List<HorarioAtencion> horarios = horarioAtencionRepository.findBySucursalId(sucursalId);
        return horarios.stream()
                .map(horarioAtencionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean estaEnHorarioLaboral(List<HorarioAtencionDTO> horariosDTO) {
        DayOfWeek diaActual = LocalDate.now().getDayOfWeek();
        LocalTime horaActual = LocalTime.now();

        for (HorarioAtencionDTO dto : horariosDTO) {
            if (diaActual.name().equalsIgnoreCase(dto.getDiaSemana())) {
                return horaActual.isAfter(dto.getApertura()) && horaActual.isBefore(dto.getCierre());
            }
        }

        return false; // Si no hay horario definido para ese día
    }
}
