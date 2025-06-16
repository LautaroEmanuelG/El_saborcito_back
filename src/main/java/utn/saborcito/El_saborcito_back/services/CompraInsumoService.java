package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import utn.saborcito.El_saborcito_back.dto.CompraInsumoDTO;
import utn.saborcito.El_saborcito_back.mappers.CompraInsumoMapper;
import utn.saborcito.El_saborcito_back.models.CompraInsumo;
import utn.saborcito.El_saborcito_back.models.CompraInsumoDetalle;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.repositories.CompraInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.CompraInsumoDetalleRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraInsumoService {
    private final CompraInsumoRepository compraInsumoRepository;
    private final CompraInsumoDetalleRepository compraInsumoDetalleRepository;
    private final ArticuloRepository articuloRepository;
    private final CompraInsumoMapper compraInsumoMapper;

    public List<CompraInsumoDTO> findAll() {
        return compraInsumoRepository.findAll().stream()
                .map(compraInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CompraInsumoDTO findById(Long id) {
        CompraInsumo compraInsumo = compraInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        return compraInsumoMapper.toDTO(compraInsumo);
    }

    public CompraInsumoDTO save(CompraInsumoDTO compraInsumoDTO) {
        CompraInsumo compraInsumo = compraInsumoMapper.toEntity(compraInsumoDTO);

        // Validar y asociar artículos en los detalles
        if (compraInsumo.getDetalles() != null && compraInsumoDTO.getDetalles() != null) {
            for (int i = 0; i < compraInsumo.getDetalles().size(); i++) {
                CompraInsumoDetalle detalle = compraInsumo.getDetalles().get(i);
                var detalleDTO = compraInsumoDTO.getDetalles().get(i);
                if (detalleDTO.getArticulo() != null && detalleDTO.getArticulo().getId() != null) {
                    Articulo articulo = articuloRepository.findById(detalleDTO.getArticulo().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Artículo no encontrado con ID: " + detalleDTO.getArticulo().getId()));
                    detalle.setArticulo(articulo);
                }
                detalle.setCompraInsumo(compraInsumo);
                detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioCosto());
            }
        }

        // Calcular total antes de guardar
        compraInsumo.setTotal(compraInsumo.getDetalles().stream()
                .mapToDouble(CompraInsumoDetalle::getSubtotal)
                .sum());

        CompraInsumo savedCompraInsumo = compraInsumoRepository.save(compraInsumo);
        return compraInsumoMapper.toDTO(savedCompraInsumo);
    }

    public CompraInsumoDTO update(Long id, CompraInsumoDTO compraInsumoDTO) {
        CompraInsumo existingCompraInsumo = compraInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));

        CompraInsumo compraInsumoActualizada = compraInsumoMapper.toEntity(compraInsumoDTO);
        compraInsumoActualizada.setId(id);

        existingCompraInsumo.setFecha(compraInsumoActualizada.getFecha());
        existingCompraInsumo.setProveedor(compraInsumoActualizada.getProveedor());
        existingCompraInsumo.setSucursal(compraInsumoActualizada.getSucursal());

        // Manejo correcto de la colección para evitar error Hibernate
        existingCompraInsumo.getDetalles().clear();
        if (compraInsumoActualizada.getDetalles() != null && compraInsumoDTO.getDetalles() != null) {
            for (int i = 0; i < compraInsumoActualizada.getDetalles().size(); i++) {
                CompraInsumoDetalle detalle = compraInsumoActualizada.getDetalles().get(i);
                var detalleDTO = compraInsumoDTO.getDetalles().get(i);
                if (detalleDTO.getArticulo() != null && detalleDTO.getArticulo().getId() != null) {
                    Articulo articulo = articuloRepository.findById(detalleDTO.getArticulo().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Artículo no encontrado con ID: " + detalleDTO.getArticulo().getId()));
                    detalle.setArticulo(articulo);
                }
                detalle.setCompraInsumo(existingCompraInsumo);
                detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioCosto());
                existingCompraInsumo.getDetalles().add(detalle);
            }
        }

        // Calcular total antes de guardar
        existingCompraInsumo.setTotal(existingCompraInsumo.getDetalles().stream()
                .mapToDouble(CompraInsumoDetalle::getSubtotal)
                .sum());

        CompraInsumo savedCompraInsumo = compraInsumoRepository.save(existingCompraInsumo);
        return compraInsumoMapper.toDTO(savedCompraInsumo);
    }

    public void delete(Long id) {
        CompraInsumo compraInsumo = compraInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        compraInsumoRepository.delete(compraInsumo);
    }
}