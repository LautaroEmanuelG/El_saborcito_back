package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.ArticuloRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;
import utn.saborcito.El_saborcito_back.mappers.ImagenMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticuloService {

    private final ArticuloRepository repo;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ImagenRepository imagenRepository;
    private final ImagenMapper imagenMapper;

    public Articulo findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del artículo no puede ser nulo.");
        }
        return repo.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo no encontrado con ID: " + id));
    }

    public List<ArticuloDTO> findAll() {
        return repo.findAllNotDeleted().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ArticuloDTO toDto(Articulo articulo) {
        if (articulo == null) {
            return null;
        }
        ArticuloDTO dto = new ArticuloDTO();
        dto.setId(articulo.getId());
        dto.setDenominacion(articulo.getDenominacion());
        dto.setPrecioVenta(articulo.getPrecioVenta());

        if (articulo.getImagen() != null) {
            dto.setImagen(imagenMapper.toDTO(articulo.getImagen()));
        }
        if (articulo.getCategoria() != null && articulo.getCategoria().getId() != null) {
            dto.setCategoriaId(articulo.getCategoria().getId());
        }

        return dto;
    }

    private ArticuloDTO toDtoWithDeletedFields(Articulo articulo) {
        ArticuloDTO dto = toDto(articulo);
        if (dto != null) {
            dto.setEliminado(articulo.getEliminado());
            dto.setFechaEliminacion(articulo.getFechaEliminacion());
        }
        return dto;
    }

    public Articulo save(Articulo articulo) {
        if (articulo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El artículo a guardar no puede ser nulo.");
        }

        // Validar campos obligatorios antes de guardar
        if (articulo.getDenominacion() == null || articulo.getDenominacion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La denominación del artículo es obligatoria.");
        }
        if (articulo.getPrecioVenta() == null || articulo.getPrecioVenta() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El precio de venta del artículo es obligatorio y no puede ser negativo.");
        }

        // Validar y asegurar la existencia de Categoria
        if (articulo.getCategoria() == null || articulo.getCategoria().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría del artículo es obligatoria.");
        }

        Categoria categoria = categoriaRepository.findById(articulo.getCategoria().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Categoría no encontrada con ID: " + articulo.getCategoria().getId()));
        articulo.setCategoria(categoria);

        // Validar y asegurar la existencia de UnidadMedida SOLO para ArticuloInsumo
        if (articulo instanceof ArticuloInsumo) {
            if (articulo.getUnidadMedida() == null || articulo.getUnidadMedida().getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La unidad de medida es obligatoria para artículos insumo.");
            }
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(articulo.getUnidadMedida().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Unidad de medida no encontrada con ID: " + articulo.getUnidadMedida().getId()));
            articulo.setUnidadMedida(unidadMedida);
        } else {
            // Para ArticuloManufacturado, la unidad de medida no es requerida
            if (articulo.getUnidadMedida() != null && articulo.getUnidadMedida().getId() != null) {
                UnidadMedida unidadMedidaOpcional = unidadMedidaRepository.findById(articulo.getUnidadMedida().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Unidad de medida no encontrada con ID: " + articulo.getUnidadMedida().getId()));
                articulo.setUnidadMedida(unidadMedidaOpcional);
            }
        }

        // Validar y asegurar la existencia de Imagen si se proporciona
        if (articulo.getImagen() != null && articulo.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(articulo.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Imagen no encontrada con ID: " + articulo.getImagen().getId()));
            articulo.setImagen(imagen);
        } else if (articulo.getImagen() != null && articulo.getImagen().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID de la imagen es necesario si se proporciona el objeto imagen.");
        }

        // Validaciones específicas para ArticuloInsumo
        if (articulo instanceof ArticuloInsumo insumo) {
            if (insumo.getPrecioCompra() == null || insumo.getPrecioCompra() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El precio de compra del insumo es obligatorio y no puede ser negativo.");
            }
            if (insumo.getStockActual() == null || insumo.getStockActual() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El stock actual del insumo es obligatorio y no puede ser negativo.");
            }
            if (insumo.getStockMaximo() == null || insumo.getStockMaximo() < insumo.getStockActual()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El stock máximo debe ser mayor o igual al stock actual.");
            }
            if (insumo.getEsParaElaborar() == null) {
                insumo.setEsParaElaborar(false);
            }
        }

        return repo.save(articulo);
    }

    public Articulo update(Long id, Articulo articuloActualizado) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID del artículo para actualizar no puede ser nulo.");
        }
        if (articuloActualizado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El objeto artículo para actualizar no puede ser nulo.");
        }

        Articulo articuloExistente = findById(id);

        // Actualizar campos básicos
        if (articuloActualizado.getDenominacion() != null && !articuloActualizado.getDenominacion().trim().isEmpty()) {
            articuloExistente.setDenominacion(articuloActualizado.getDenominacion());
        }
        if (articuloActualizado.getPrecioVenta() != null && articuloActualizado.getPrecioVenta() >= 0) {
            articuloExistente.setPrecioVenta(articuloActualizado.getPrecioVenta());
        }

        // Actualizar Imagen
        if (articuloActualizado.getImagen() != null && articuloActualizado.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(articuloActualizado.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Imagen para actualizar no encontrada con ID: "
                                    + articuloActualizado.getImagen().getId()));
            articuloExistente.setImagen(imagen);
        } else if (articuloActualizado.getImagen() == null && articuloExistente.getImagen() != null) {
            articuloExistente.setImagen(null);
        }

        // Actualizar Categoria si se proporciona
        if (articuloActualizado.getCategoria() != null && articuloActualizado.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(articuloActualizado.getCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Categoría para actualizar no encontrada con ID: "
                                    + articuloActualizado.getCategoria().getId()));
            articuloExistente.setCategoria(categoria);
        }

        // Actualizar UnidadMedida si se proporciona
        if (articuloActualizado.getUnidadMedida() != null && articuloActualizado.getUnidadMedida().getId() != null) {
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(articuloActualizado.getUnidadMedida().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Unidad de medida para actualizar no encontrada con ID: "
                                    + articuloActualizado.getUnidadMedida().getId()));
            articuloExistente.setUnidadMedida(unidadMedida);
        }

        // Manejo específico para ArticuloInsumo
        if (articuloExistente instanceof ArticuloInsumo insumoExistente
                && articuloActualizado instanceof ArticuloInsumo insumoActualizado) {
            if (insumoActualizado.getPrecioCompra() != null && insumoActualizado.getPrecioCompra() >= 0) {
                insumoExistente.setPrecioCompra(insumoActualizado.getPrecioCompra());
            }
            if (insumoActualizado.getStockActual() != null && insumoActualizado.getStockActual() >= 0) {
                insumoExistente.setStockActual(insumoActualizado.getStockActual());
            }
            if (insumoActualizado.getStockMaximo() != null && insumoActualizado
                    .getStockMaximo() >= (insumoExistente.getStockActual() != null ? insumoExistente.getStockActual()
                            : 0)) {
                insumoExistente.setStockMaximo(insumoActualizado.getStockMaximo());
            }
            if (insumoActualizado.getEsParaElaborar() != null) {
                insumoExistente.setEsParaElaborar(insumoActualizado.getEsParaElaborar());
            }
        }

        return repo.save(articuloExistente);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID del artículo para eliminar no puede ser nulo.");
        }

        Articulo articulo = findById(id); // Ya valida que existe y no está eliminado

        // Delete lógico: marcar como eliminado y establecer fecha
        articulo.setEliminado(true);
        articulo.setFechaEliminacion(LocalDateTime.now());

        repo.save(articulo);
    }

    // Métodos adicionales para manejar elementos eliminados

    public List<ArticuloDTO> findAllDeleted() {
        return repo.findAllDeleted().stream()
                .map(this::toDtoWithDeletedFields)
                .collect(Collectors.toList());
    }

    public Articulo findByIdDeleted(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del artículo no puede ser nulo.");
        }
        return repo.findByIdDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo eliminado no encontrado con ID: " + id));
    }

    public void restoreDeleted(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID del artículo para restaurar no puede ser nulo.");
        }

        Articulo articulo = findByIdDeleted(id);

        // Restaurar: quitar marca de eliminado y limpiar fecha
        articulo.setEliminado(false);
        articulo.setFechaEliminacion(null);

        repo.save(articulo);
    }

    public void permanentDelete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID del artículo para eliminar permanentemente no puede ser nulo.");
        }

        // Verificar que el artículo esté marcado como eliminado antes de eliminarlo
        // permanentemente
        Articulo articulo = findByIdDeleted(id);

        repo.deleteById(id);
    }
}