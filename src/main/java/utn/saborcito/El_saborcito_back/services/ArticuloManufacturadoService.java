package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.mappers.ArticuloManufacturadoMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoService {

    private final ArticuloManufacturadoRepository repo;
    private final ArticuloManufacturadoMapper mapper;
    private final ImagenRepository imagenRepository; // Añadido
    private final CategoriaRepository categoriaRepository; // Añadido
    private final UnidadMedidaRepository unidadMedidaRepository; // Añadido

    public List<ArticuloManufacturadoDTO> findAll() {
        return repo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ArticuloManufacturadoDTO findById(Long id) {
        ArticuloManufacturado articuloManufacturado = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado"));
        return mapper.toDTO(articuloManufacturado);
    }

    public ArticuloManufacturado findEntityById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado"));
    }

    public ArticuloManufacturadoDTO save(ArticuloManufacturadoDTO dto) {
        ArticuloManufacturado articuloManufacturado = mapper.toEntity(dto);

        // Manejo de la imagen (heredado de Articulo)
        if (dto.getImagen() != null) {
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

        // Manejo de UnidadMedida
        if (dto.getUnidadMedidaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe proporcionar una unidad de medida para el artículo manufacturado.");
        }
        UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unidad de medida no encontrada con ID: " + dto.getUnidadMedidaId()));
        articuloManufacturado.setUnidadMedida(unidadMedida);

        // Aquí podrías necesitar lógica adicional para manejar las relaciones, como
        // ArticuloManufacturadoDetalle
        return mapper.toDTO(repo.save(articuloManufacturado));
    }

    public ArticuloManufacturadoDTO update(Long id, ArticuloManufacturadoDTO dto) {
        ArticuloManufacturado articuloExistente = repo.findById(id)
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
        if (dto.getImagen() != null) {
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

        // Manejo de UnidadMedida
        if (dto.getUnidadMedidaId() != null) {
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Unidad de medida no encontrada con ID: " + dto.getUnidadMedidaId()));
            articuloExistente.setUnidadMedida(unidadMedida);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe proporcionar una unidad de medida para el artículo manufacturado.");
        }

        // Aquí podrías necesitar lógica adicional para manejar las relaciones
        return mapper.toDTO(repo.save(articuloExistente));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo manufacturado no encontrado");
        }
        repo.deleteById(id);
    }

    public List<ArticuloManufacturadoDTO> findAllByCategoriaId(Long categoriaId) {
        return repo.findAllByCategoria_Id(categoriaId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}