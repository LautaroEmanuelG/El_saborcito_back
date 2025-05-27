package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ArticuloDTO;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo; // Necesario para instanceof
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.ArticuloRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;
import utn.saborcito.El_saborcito_back.mappers.ImagenMapper; // Asegúrate de importar ImagenMapper

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticuloService {

    private final ArticuloRepository repo;
    private final CategoriaRepository categoriaRepository; // Inyectar CategoriaRepository
    private final UnidadMedidaRepository unidadMedidaRepository; // Inyectar UnidadMedidaRepository
    private final ImagenRepository imagenRepository; // Inyectar ImagenRepository (si aplica para ArticuloInsumo)
    private final ImagenMapper imagenMapper; // Inyectar ImagenMapper

    public Articulo findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del artículo no puede ser nulo.");
        }
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo no encontrado con ID: " + id));
    }

    public List<ArticuloDTO> findAll() {
        return repo.findAll().stream()
                .map(this::toDto) // Cuidado con NPEs dentro de toDto si hay datos corruptos
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

        if (articulo.getImagen() != null) { // No es necesario chequear el ID de la imagen aquí
            dto.setImagen(imagenMapper.toDTO(articulo.getImagen())); // Mapear Imagen a ImagenDTO
        }

        if (articulo.getCategoria() != null && articulo.getCategoria().getId() != null) {
            dto.setCategoriaId(articulo.getCategoria().getId());
        } else {
            // Opción 1: Dejarlo null en el DTO
            // Opción 2: Lanzar advertencia/error si la categoría es mandatoria
            // Opción 3: Asignar un ID por defecto o indicativo de "sin categoría"
        }

        if (articulo.getUnidadMedida() != null && articulo.getUnidadMedida().getId() != null) {
            dto.setUnidadMedidaId(articulo.getUnidadMedida().getId());
        }

        // Si ArticuloDTO necesita más campos (ej. tipo de artículo, imagenUrl para
        // insumos)
        // se añadirían aquí con las validaciones correspondientes.

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

        // Validar y asegurar la existencia de UnidadMedida
        if (articulo.getUnidadMedida() == null || articulo.getUnidadMedida().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La unidad de medida del artículo es obligatoria.");
        }
        UnidadMedida unidadMedida = unidadMedidaRepository.findById(articulo.getUnidadMedida().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad de medida no encontrada con ID: " + articulo.getUnidadMedida().getId()));
        articulo.setUnidadMedida(unidadMedida);

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
            // Si ArticuloInsumo tiene requerimientos *adicionales* para la imagen,
            // se pondrían aquí. Por ejemplo, si para Insumos la imagen es siempre
            // obligatoria.
            // if (insumo.getImagen() == null) {
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            // "La imagen es obligatoria para un artículo de tipo insumo.");
            // }
            // También se podrían validar otros campos específicos de ArticuloInsumo aquí
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
                insumo.setEsParaElaborar(false); // Valor por defecto si es nulo
            }
        }
        // Aquí irían validaciones para ArticuloManufacturado si las tuviera específicas

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

        Articulo articuloExistente = findById(id); // Ya maneja NPE si no se encuentra

        // Actualizar campos básicos (evitar NPEs si vienen nulos en el DTO/entidad
        // actualizada)
        if (articuloActualizado.getDenominacion() != null && !articuloActualizado.getDenominacion().trim().isEmpty()) {
            articuloExistente.setDenominacion(articuloActualizado.getDenominacion());
        }
        if (articuloActualizado.getPrecioVenta() != null && articuloActualizado.getPrecioVenta() >= 0) {
            articuloExistente.setPrecioVenta(articuloActualizado.getPrecioVenta());
        }

        // Actualizar Imagen (aplica a cualquier Articulo)
        if (articuloActualizado.getImagen() != null && articuloActualizado.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(articuloActualizado.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Imagen para actualizar no encontrada con ID: "
                                    + articuloActualizado.getImagen().getId()));
            articuloExistente.setImagen(imagen);
        } else if (articuloActualizado.getImagen() == null && articuloExistente.getImagen() != null) {
            // Permitir desasociar la imagen estableciéndola a null
            articuloExistente.setImagen(null);
        } else if (articuloActualizado.getImagen() != null && articuloActualizado.getImagen().getId() == null
                && articuloActualizado.getImagen().getUrl() != null) {
            // Si se envía un objeto imagen con URL pero sin ID en la actualización,
            // se podría interpretar como un intento de crear y asociar una nueva imagen.
            // Esta lógica dependerá de si se permite la creación de imágenes "al vuelo"
            // aquí.
            // Por ahora, se asume que la imagen debe existir o ser null para desasociar.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Para asignar una nueva imagen en la actualización, esta debe existir previamente y proveer su ID.");
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

        // Manejo específico si son ArticuloInsumo o ArticuloManufacturado
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
            // La lógica de actualización de imagen ya está cubierta arriba de forma
            // genérica para Articulo.
        }
        // Faltaría el manejo para ArticuloManufacturado si tiene campos específicos
        // actualizables
        // ej.
        // articuloExistente.setTiempoEstimadoMinutos(articuloActualizado.getTiempoEstimadoMinutos());
        // y la gestión de su lista de ArticuloManufacturadoDetalle (similar a
        // PedidoService.update)

        return repo.save(articuloExistente);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El ID del artículo para eliminar no puede ser nulo.");
        }
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Artículo no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }
}