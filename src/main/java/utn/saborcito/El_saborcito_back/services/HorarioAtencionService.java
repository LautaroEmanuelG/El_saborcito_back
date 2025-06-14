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
        System.out.println("Horarios para sucursal " + sucursalId + ": " + horarios);
        return horarios.stream()
                .map(horarioAtencionMapper::toDTO)
                .collect(Collectors.toList());
    }
    public boolean estaEnHorarioLaboral(List<HorarioAtencionDTO> horariosDTO) {
        String diaHoy = traducirDiaAlEspanol(LocalDate.now().getDayOfWeek());
        LocalTime ahora = LocalTime.now();
        return horariosDTO.stream()
                .filter(h -> h.getDiaSemana().equalsIgnoreCase(diaHoy))
                .anyMatch(h -> {
                    LocalTime apertura = h.getApertura();
                    LocalTime cierre = h.getCierre();

                    // Validar campos nulos
                    if (apertura == null || cierre == null) return false;
                    System.out.println("Ahora aca");
                    // Si cierra al día siguiente o justo a la medianoche
                    boolean cruzaMedianoche = cierre.isBefore(apertura) || cierre.equals(LocalTime.MIDNIGHT);
                    if (cruzaMedianoche) {
                        return ahora.isAfter(apertura) || ahora.isBefore(cierre.equals(LocalTime.MIDNIGHT) ? LocalTime.of(5, 0) : cierre);
                    } else {
                        return !ahora.isBefore(apertura) && !ahora.isAfter(cierre);
                    }
                });
    }
    private String traducirDiaAlEspanol(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "LUNES";
            case TUESDAY -> "MARTES";
            case WEDNESDAY -> "MIERCOLES";
            case THURSDAY -> "JUEVES";
            case FRIDAY -> "VIERNES";
            case SATURDAY -> "SABADO";
            case SUNDAY -> "DOMINGO";
        };
    }





}
