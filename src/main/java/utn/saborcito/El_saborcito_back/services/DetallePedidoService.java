package utn.saborcito.El_saborcito_back.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO; // Corregido: dto en lugar de dtos
import utn.saborcito.El_saborcito_back.mappers.DetallePedidoMapper;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.ArticuloRepository;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final DetallePedidoMapper detallePedidoMapper;
    private final ArticuloRepository articuloRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public List<DetallePedidoDTO> findAll() {
        return detallePedidoMapper.toDTOList(detallePedidoRepository.findAll()); // Corregido: toDTOList en lugar de
                                                                                 // toDTOs
    }

    @Transactional(readOnly = true)
    public DetallePedidoDTO findById(Long id) {
        return detallePedidoRepository.findById(id)
                .map(detallePedidoMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("DetallePedido no encontrado con id: " + id));
    }

    @Transactional
    public DetallePedidoDTO save(DetallePedidoDTO dto, Long pedidoId) {
        DetallePedido detallePedido = detallePedidoMapper.toEntity(dto);

        if (dto.getArticulo() == null || dto.getArticulo().getId() == null) {
            throw new IllegalArgumentException("El ID del artículo es obligatorio para crear un DetallePedido.");
        }
        Articulo articulo = articuloRepository.findById(dto.getArticulo().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artículo no encontrado con id: " + dto.getArticulo().getId()));
        detallePedido.setArticulo(articulo);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con id: " + pedidoId));
        detallePedido.setPedido(pedido);

        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser un entero positivo.");
        }
        detallePedido.setCantidad(dto.getCantidad());

        DetallePedido savedDetallePedido = detallePedidoRepository.save(detallePedido);
        return detallePedidoMapper.toDTO(savedDetallePedido);
    }

    @Transactional
    public DetallePedidoDTO update(Long id, DetallePedidoDTO dto) {
        DetallePedido existingDetallePedido = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DetallePedido no encontrado con id: " + id));

        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser un entero positivo.");
        }
        existingDetallePedido.setCantidad(dto.getCantidad());

        if (dto.getArticulo() != null && dto.getArticulo().getId() != null) {
            if (existingDetallePedido.getArticulo() == null ||
                    !existingDetallePedido.getArticulo().getId().equals(dto.getArticulo().getId())) {
                Articulo articulo = articuloRepository.findById(dto.getArticulo().getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Artículo no encontrado con id: " + dto.getArticulo().getId()));
                existingDetallePedido.setArticulo(articulo);
            }
        } else {
            throw new IllegalArgumentException("El ID del artículo no puede ser nulo al actualizar un DetallePedido.");
        }

        DetallePedido updatedDetallePedido = detallePedidoRepository.save(existingDetallePedido);
        return detallePedidoMapper.toDTO(updatedDetallePedido);
    }

    @Transactional
    public void delete(Long id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new EntityNotFoundException("DetallePedido no encontrado con id: " + id);
        }
        detallePedidoRepository.deleteById(id);
    }
}
