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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticuloService {

    private final ArticuloRepository repo;
    private final CategoriaRepository categoriaRepository; // Inyectar CategoriaRepository
    private final UnidadMedidaRepository unidadMedidaRepository; // Inyectar UnidadMedidaRepository
    private final ImagenRepository imagenRepository; // Inyectar ImagenRepository (si aplica para ArticuloInsumo)

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
            // Considerar si devolver null, un DTO vacío o lanzar excepción
            // Por ahora, se asume que 'articulo' no será null si viene de repo.findAll()
            // pero es buena práctica considerarlo.
            return null;
        }
        ArticuloDTO dto = new ArticuloDTO();
        dto.setId(articulo.getId()); // getId() en Long no debería ser null si la entidad está gestionada
        dto.setDenominacion(articulo.getDenominacion());
        dto.setPrecioVenta(articulo.getPrecioVenta());

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

        // Validaciones específicas para ArticuloInsumo (ej. imagen)
        if (articulo instanceof ArticuloInsumo insumo) {
            // CORRECCIÓN 24: Si la imagen es obligatoria para ArticuloInsumo
            if (insumo.getImagen() == null || insumo.getImagen().getId() == null) {
                // Si es opcional, se puede quitar esta validación o manejarla diferente
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La imagen es obligatoria para un artículo de tipo insumo.");
            }
            Imagen imagen = imagenRepository.findById(insumo.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Imagen no encontrada con ID: " + insumo.getImagen().getId()));
            insumo.setImagen(imagen);

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
            // Actualizar Imagen para ArticuloInsumo si se proporciona
            if (insumoActualizado.getImagen() != null && insumoActualizado.getImagen().getId() != null) {
                Imagen imagen = imagenRepository.findById(insumoActualizado.getImagen().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Imagen para actualizar no encontrada con ID: "
                                        + insumoActualizado.getImagen().getId()));
                insumoExistente.setImagen(imagen);
            } else if (insumoActualizado.getImagen() == null && insumoExistente.getImagen() != null) { // Solo si se
                                                                                                       // intenta poner
                                                                                                       // a null una
                                                                                                       // imagen
                                                                                                       // existente
                // Si se quiere permitir desasociar la imagen, se podría hacer:
                // insumoExistente.setImagen(null);
                // O lanzar error si la imagen es obligatoria y se intenta poner a null.
                // Actualmente, se considera obligatoria, por lo que no se permite poner a null
                // si ya tiene una.
                // Si la intención es cambiar la imagen, se debe proveer una nueva imagen con
                // ID.
                // Si la intención es quitarla y la imagen fuera opcional, esta lógica
                // cambiaría.
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La imagen es obligatoria para un artículo de tipo insumo y no puede ser nula en la actualización si ya existe una.");
            }
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