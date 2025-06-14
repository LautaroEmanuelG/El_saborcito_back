package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.mappers.DomicilioMapper;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DomicilioService {
    private final DomicilioRepository repo;
    private final UsuarioRepository usuarioRepository;
    private final LocalidadRepository localidadRepository;
    private final DomicilioMapper domicilioMapper;

    public DomicilioDTO crearDomicilio(Long usuarioId, DomicilioDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (dto.getLocalidad() == null || dto.getLocalidad().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localidad obligatoria");
        }

        Localidad localidad = localidadRepository.findById(dto.getLocalidad().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localidad inválida"));

        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(dto.getCalle());
        domicilio.setNumero(dto.getNumero());
        domicilio.setCp(dto.getCp());
        domicilio.setLatitud(dto.getLatitud());
        domicilio.setLongitud(dto.getLongitud());
        domicilio.setLocalidad(localidad);
        domicilio.setUsuario(usuario);
        // Asegúrate de que el usuario tenga la lista inicializada
        if (usuario.getDomicilios() == null) {
            usuario.setDomicilios(new ArrayList<>());
        }

        // Guardar domicilio
        Domicilio guardado = repo.save(domicilio);

        // Agregar a la lista del usuario
        usuario.getDomicilios().add(guardado);
        usuarioRepository.save(usuario); // Actualiza el usuario

        // Mapear y devolver
        return domicilioMapper.toDTO(guardado);
    }

    public DomicilioDTO actualizarDomicilio(Long usuarioId, Long domicilioId, DomicilioDTO dto) {
        Domicilio existente = repo.findById(domicilioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Domicilio no encontrado"));

        if (!existente.getUsuario().getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede editar este domicilio");
        }

        if (dto.getLocalidad() != null && dto.getLocalidad().getId() != null) {
            Localidad localidad = localidadRepository.findById(dto.getLocalidad().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localidad inválida"));
            existente.setLocalidad(localidad);
        }

        existente.setCalle(dto.getCalle());
        existente.setNumero(dto.getNumero());
        existente.setCp(dto.getCp());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());

        repo.save(existente);
        dto.setId(domicilioId);
        return dto;
    }

    public void eliminarDomicilio(Long usuarioId, Long domicilioId) {
        Domicilio domicilio = repo.findById(domicilioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Domicilio no encontrado"));
        if (!domicilio.getUsuario().getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede eliminar este domicilio");
        }

        repo.deleteById(domicilioId);
    }
}
