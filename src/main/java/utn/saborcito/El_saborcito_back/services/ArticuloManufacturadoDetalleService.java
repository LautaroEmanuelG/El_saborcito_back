package utn.saborcito.El_saborcito_back.services;

import jakarta.persistence.EntityNotFoundException; // Usar EntityNotFoundException de jakarta
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDetalleDTO;
import utn.saborcito.El_saborcito_back.mappers.ArticuloManufacturadoDetalleMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoDetalleRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoDetalleService {

    private final ArticuloManufacturadoDetalleRepository detalleRepository;
    private final ArticuloManufacturadoDetalleMapper detalleMapper;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository; // Para asignar el padre

    @Transactional(readOnly = true)
    public List<ArticuloManufacturadoDetalleDTO> findAll() {
        return detalleMapper.toDTOList(detalleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ArticuloManufacturadoDetalleDTO findById(Long id) {
        return detalleRepository.findById(id)
                .map(detalleMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Detalle de artículo manufacturado no encontrado con id: " + id));
    }

    // Para guardar un detalle, necesitamos saber a qué ArticuloManufacturado
    // pertenece.
    // Se añade el parámetro articuloManufacturadoId.
    @Transactional
    public ArticuloManufacturadoDetalleDTO save(ArticuloManufacturadoDetalleDTO dto, Long articuloManufacturadoId) {
        ArticuloManufacturadoDetalle detalle = detalleMapper.toEntity(dto);

        // Cargar y asignar ArticuloInsumo
        if (dto.getArticuloInsumo() == null || dto.getArticuloInsumo().getId() == null) {
            throw new IllegalArgumentException("El ID del ArticuloInsumo es obligatorio.");
        }
        ArticuloInsumo insumo = articuloInsumoRepository.findById(dto.getArticuloInsumo().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "ArticuloInsumo no encontrado con id: " + dto.getArticuloInsumo().getId()));
        detalle.setArticuloInsumo(insumo);

        // Cargar y asignar ArticuloManufacturado (padre)
        ArticuloManufacturado manufacturado = articuloManufacturadoRepository.findById(articuloManufacturadoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ArticuloManufacturado no encontrado con id: " + articuloManufacturadoId));
        detalle.setArticuloManufacturado(manufacturado);

        // Validar cantidad
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser un entero positivo.");
        }
        detalle.setCantidad(dto.getCantidad());

        ArticuloManufacturadoDetalle savedDetalle = detalleRepository.save(detalle);
        return detalleMapper.toDTO(savedDetalle);
    }

    @Transactional
    public ArticuloManufacturadoDetalleDTO update(Long id, ArticuloManufacturadoDetalleDTO dto) {
        ArticuloManufacturadoDetalle existingDetalle = detalleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Detalle de artículo manufacturado no encontrado con id: " + id));

        // Actualizar cantidad
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser un entero positivo.");
        }
        existingDetalle.setCantidad(dto.getCantidad());

        // Actualizar ArticuloInsumo si se proporciona y es diferente
        if (dto.getArticuloInsumo() != null && dto.getArticuloInsumo().getId() != null) {
            if (existingDetalle.getArticuloInsumo() == null ||
                    !existingDetalle.getArticuloInsumo().getId().equals(dto.getArticuloInsumo().getId())) {
                ArticuloInsumo insumo = articuloInsumoRepository.findById(dto.getArticuloInsumo().getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "ArticuloInsumo no encontrado con id: " + dto.getArticuloInsumo().getId()));
                existingDetalle.setArticuloInsumo(insumo);
            }
        } else {
            throw new IllegalArgumentException("El ArticuloInsumo no puede ser nulo al actualizar.");
        }
        // Nota: No se permite cambiar el ArticuloManufacturado padre a través de esta
        // actualización de detalle.

        ArticuloManufacturadoDetalle updatedDetalle = detalleRepository.save(existingDetalle);
        return detalleMapper.toDTO(updatedDetalle);
    }

    @Transactional
    public void delete(Long id) {
        if (!detalleRepository.existsById(id)) {
            throw new EntityNotFoundException("Detalle de artículo manufacturado no encontrado con id: " + id);
        }
        detalleRepository.deleteById(id);
    }
}