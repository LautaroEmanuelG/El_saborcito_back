package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDetalleDTO;
import utn.saborcito.El_saborcito_back.dto.ImagenDTO;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.mappers.ArticuloManufacturadoMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 🍴 Servicio para gestión de artículos manufacturados
 * Responsable de operaciones CRUD y manejo de lógica de negocio
 */
@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoService {
    private final ArticuloManufacturadoRepository repo;
    private final ArticuloManufacturadoMapper mapper;
    private final ImagenRepository imagenRepository;
    private final CategoriaRepository categoriaRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final CloudinaryService cloudinaryService;

    // Métodos principales con delete lógico
    public List<ArticuloManufacturadoDTO> findAll() {
        return repo.findAllNotDeleted().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ArticuloManufacturadoDTO findById(Long id) {
        ArticuloManufacturado articuloManufacturado = repo.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado"));
        return mapper.toDTO(articuloManufacturado);
    }

    public ArticuloManufacturado findEntityById(Long id) {
        return repo.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado"));
    }

    @Transactional
    public ArticuloManufacturadoDTO save(ArticuloManufacturadoDTO dto) {
        ArticuloManufacturado articuloManufacturado = mapper.toEntity(dto);

        // Inicializar campos de delete lógico si no están establecidos
        if (articuloManufacturado.getEliminado() == null) {
            articuloManufacturado.setEliminado(false);
        }
        if (articuloManufacturado.getFechaEliminacion() == null && articuloManufacturado.getEliminado()) {
            articuloManufacturado.setFechaEliminacion(LocalDateTime.now());
        }

        // Manejo de la imagen (heredado de Articulo)
        if (dto.getImagen() != null && dto.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Imagen no encontrada con ID: " + dto.getImagen().getId()));
            articuloManufacturado.setImagen(imagen);
        } else {
            articuloManufacturado.setImagen(null);
        }

        // Manejo de Categoria
        if (dto.getCategoriaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe proporcionar una categoría para el artículo manufacturado.");
        }
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Categoría no encontrada con ID: " + dto.getCategoriaId()));
        articuloManufacturado.setCategoria(categoria);

        // Los artículos manufacturados NO requieren unidad de medida
        // Dejar unidadMedida como null o con valor por defecto

        // Guardar primero el artículo manufacturado para obtener el ID
        ArticuloManufacturado savedArticuloManufacturado = repo.save(articuloManufacturado);

        // Procesar los detalles si están presentes
        if (dto.getArticuloManufacturadoDetalles() != null && !dto.getArticuloManufacturadoDetalles().isEmpty()) {
            Set<ArticuloManufacturadoDetalle> detalles = new HashSet<>();

            for (ArticuloManufacturadoDetalleDTO detalleDTO : dto.getArticuloManufacturadoDetalles()) {
                ArticuloManufacturadoDetalle detalle = new ArticuloManufacturadoDetalle();
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setArticuloManufacturado(savedArticuloManufacturado);

                // Obtener el ArticuloInsumo por ID
                if (detalleDTO.getArticuloInsumo() != null && detalleDTO.getArticuloInsumo().getId() != null) {
                    ArticuloInsumo articuloInsumo = articuloInsumoRepository
                            .findById(detalleDTO.getArticuloInsumo().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "ArticuloInsumo no encontrado con ID: " + detalleDTO.getArticuloInsumo().getId()));
                    detalle.setArticuloInsumo(articuloInsumo);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Debe proporcionar un ArticuloInsumo válido para cada detalle.");
                }

                detalles.add(detalle);
            }

            savedArticuloManufacturado.setArticuloManufacturadoDetalles(detalles);
            // Guardar nuevamente con los detalles
            savedArticuloManufacturado = repo.save(savedArticuloManufacturado);
        }

        return mapper.toDTO(savedArticuloManufacturado);
    }

    @Transactional
    public ArticuloManufacturadoDTO update(Long id, ArticuloManufacturadoDTO dto) {
        ArticuloManufacturado articuloExistente = repo.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado para actualizar"));

        // Actualizar campos básicos (heredados de Articulo)
        articuloExistente.setDenominacion(dto.getDenominacion());
        articuloExistente.setPrecioVenta(dto.getPrecioVenta());

        // Actualizar campos específicos de ArticuloManufacturado
        articuloExistente.setDescripcion(dto.getDescripcion());
        articuloExistente.setTiempoEstimadoMinutos(dto.getTiempoEstimadoMinutos());
        articuloExistente.setPreparacion(dto.getPreparacion());

        // Manejo de la imagen (heredado de Articulo)
        if (dto.getImagen() != null && dto.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Imagen no encontrada con ID: " + dto.getImagen().getId()));
            articuloExistente.setImagen(imagen);
        } else {
            articuloExistente.setImagen(null);
        }

        // Manejo de Categoria
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Categoría no encontrada con ID: " + dto.getCategoriaId()));
            articuloExistente.setCategoria(categoria);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe proporcionar una categoría para el artículo manufacturado.");
        }

        // Actualizar detalles si están presentes
        if (dto.getArticuloManufacturadoDetalles() != null) {
            // Obtener la colección actual de detalles
            Set<ArticuloManufacturadoDetalle> detallesExistentes = articuloExistente.getArticuloManufacturadoDetalles();

            // Limpiar detalles existentes de forma segura
            detallesExistentes.clear();

            // Forzar flush para eliminar los registros de la base de datos
            repo.flush();

            // Agregar nuevos detalles
            for (ArticuloManufacturadoDetalleDTO detalleDTO : dto.getArticuloManufacturadoDetalles()) {
                ArticuloManufacturadoDetalle detalle = new ArticuloManufacturadoDetalle();
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setArticuloManufacturado(articuloExistente);

                // Obtener el ArticuloInsumo por ID
                if (detalleDTO.getArticuloInsumo() != null && detalleDTO.getArticuloInsumo().getId() != null) {
                    ArticuloInsumo articuloInsumo = articuloInsumoRepository
                            .findById(detalleDTO.getArticuloInsumo().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "ArticuloInsumo no encontrado con ID: " + detalleDTO.getArticuloInsumo().getId()));
                    detalle.setArticuloInsumo(articuloInsumo);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Debe proporcionar un ArticuloInsumo válido para cada detalle.");
                }

                detallesExistentes.add(detalle);
            }
        }

        // Guardar la entidad actualizada
        ArticuloManufacturado savedArticulo = repo.save(articuloExistente);

        // Crear una respuesta DTO simplificada para evitar referencias circulares
        return createSimplifiedDTO(savedArticulo);
    }

    // Método de delete lógico
    @Transactional
    public void delete(Long id) {
        ArticuloManufacturado articulo = repo.findByIdNotDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado"));

        articulo.setEliminado(true);
        articulo.setFechaEliminacion(LocalDateTime.now());
        repo.save(articulo);
    }

    // Métodos adicionales para gestión de elementos eliminados
    public List<ArticuloManufacturadoDTO> findAllDeleted() {
        return repo.findAllDeleted().stream()
                .map(this::toDtoWithDeletedFields)
                .collect(Collectors.toList());
    }

    public ArticuloManufacturadoDTO findByIdDeleted(Long id) {
        ArticuloManufacturado articulo = repo.findByIdDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado eliminado no encontrado"));
        return toDtoWithDeletedFields(articulo);
    }

    @Transactional
    public ArticuloManufacturadoDTO restoreDeleted(Long id) {
        ArticuloManufacturado articulo = repo.findByIdDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado eliminado no encontrado"));

        articulo.setEliminado(false);
        articulo.setFechaEliminacion(null);
        ArticuloManufacturado restored = repo.save(articulo);
        return mapper.toDTO(restored);
    }

    @Transactional
    public void permanentDelete(Long id) {
        ArticuloManufacturado articulo = repo.findByIdDeleted(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado eliminado no encontrado"));
        repo.delete(articulo);
    } // Métodos auxiliares

    public List<ArticuloManufacturadoDTO> findAllByCategoriaId(Long categoriaId) {
        return repo.findAllByCategoria_Id(categoriaId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte entidad a DTO incluyendo campos de eliminación cuando sea necesario
     */
    private ArticuloManufacturadoDTO toDtoWithDeletedFields(ArticuloManufacturado entity) {
        ArticuloManufacturadoDTO dto = mapper.toDTO(entity);
        // Los campos eliminado y fechaEliminacion se incluirán si el DTO los tiene
        // definidos
        return dto;
    }

    /**
     * Crea un DTO simplificado para evitar referencias circulares y JSON extenso
     */
    private ArticuloManufacturadoDTO createSimplifiedDTO(ArticuloManufacturado entity) {
        ArticuloManufacturadoDTO dto = new ArticuloManufacturadoDTO();

        // Campos básicos heredados de Articulo
        dto.setId(entity.getId());
        dto.setDenominacion(entity.getDenominacion());
        dto.setPrecioVenta(entity.getPrecioVenta());

        // Campos específicos de ArticuloManufacturado
        dto.setDescripcion(entity.getDescripcion());
        dto.setTiempoEstimadoMinutos(entity.getTiempoEstimadoMinutos());
        dto.setPreparacion(entity.getPreparacion());

        // Categoría (solo ID)
        if (entity.getCategoria() != null) {
            dto.setCategoriaId(entity.getCategoria().getId());
        }

        // Imagen
        if (entity.getImagen() != null) {
            ImagenDTO imagenDTO = new ImagenDTO();
            imagenDTO.setId(entity.getImagen().getId());
            imagenDTO.setUrl(entity.getImagen().getUrl());
            dto.setImagen(imagenDTO);
        }

        // Detalles simplificados
        if (entity.getArticuloManufacturadoDetalles() != null && !entity.getArticuloManufacturadoDetalles().isEmpty()) {
            Set<ArticuloManufacturadoDetalleDTO> detallesDTO = new HashSet<>();

            for (ArticuloManufacturadoDetalle detalle : entity.getArticuloManufacturadoDetalles()) {
                ArticuloManufacturadoDetalleDTO detalleDTO = new ArticuloManufacturadoDetalleDTO();
                detalleDTO.setId(detalle.getId());
                detalleDTO.setCantidad(detalle.getCantidad());

                // ArticuloInsumo simplificado
                if (detalle.getArticuloInsumo() != null) {
                    ArticuloInsumoDTO insumoDTO = new ArticuloInsumoDTO();
                    insumoDTO.setId(detalle.getArticuloInsumo().getId());
                    insumoDTO.setDenominacion(detalle.getArticuloInsumo().getDenominacion());
                    insumoDTO.setPrecioVenta(detalle.getArticuloInsumo().getPrecioVenta());
                    insumoDTO.setPrecioCompra(detalle.getArticuloInsumo().getPrecioCompra());
                    insumoDTO.setStockActual(detalle.getArticuloInsumo().getStockActual());
                    insumoDTO.setEsParaElaborar(detalle.getArticuloInsumo().getEsParaElaborar());

                    // Categoría del insumo (solo ID)
                    if (detalle.getArticuloInsumo().getCategoria() != null) {
                        insumoDTO.setCategoriaId(detalle.getArticuloInsumo().getCategoria().getId());
                    }

                    detalleDTO.setArticuloInsumo(insumoDTO);
                }

                detallesDTO.add(detalleDTO);
            }

            dto.setArticuloManufacturadoDetalles(detallesDTO);
        }
        return dto;
    }

    /**
     * Asocia una imagen a un artículo manufacturado
     */
    @Transactional
    public void updateImagenArticuloManufacturado(Long articuloId, Long imagenId) {
        ArticuloManufacturado articulo = repo.findByIdNotDeleted(articuloId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado con ID: " + articuloId));

        Imagen imagen = imagenRepository.findById(imagenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Imagen no encontrada con ID: " + imagenId));

        articulo.setImagen(imagen);
        repo.save(articulo);
    }

    /**
     * Sube imagen a Cloudinary y la asocia al artículo manufacturado
     */
    @Transactional
    public utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto uploadAndAssignImagen(Long articuloId,
            org.springframework.web.multipart.MultipartFile file) {
        try {
            // Verificar que el artículo existe
            ArticuloManufacturado articulo = repo.findByIdNotDeleted(articuloId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Artículo manufacturado no encontrado con ID: " + articuloId));

            // Subir imagen a Cloudinary
            utn.saborcito.El_saborcito_back.dto.CloudinaryResponseDto cloudinaryResponse = cloudinaryService
                    .uploadImage(file);

            // Crear entidad Imagen
            Imagen imagen = Imagen.builder()
                    .url(cloudinaryResponse.getSecureUrl())
                    .build();

            Imagen imagenGuardada = imagenRepository.save(imagen);

            // Asociar imagen al artículo
            articulo.setImagen(imagenGuardada);
            repo.save(articulo);

            return utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto.builder()
                    .imagenId(imagenGuardada.getId())
                    .url(imagenGuardada.getUrl())
                    .publicId(cloudinaryResponse.getPublicId())
                    .message("Imagen asociada exitosamente al artículo manufacturado")
                    .success(true)
                    .build();

        } catch (Exception e) {
            return utn.saborcito.El_saborcito_back.dto.ImagenUploadResponseDto.builder()
                    .message("Error al procesar imagen: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }
}