package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ArticuloInsumoDTO;
import utn.saborcito.El_saborcito_back.mappers.ArticuloInsumoMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticuloInsumoService { // No extiende BaseServiceImpl

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository; // Repositorio específico

    @Autowired
    private ArticuloInsumoMapper articuloInsumoMapper;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Transactional
    public List<ArticuloInsumoDTO> findAll() {
        return articuloInsumoRepository.findAll().stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ArticuloInsumoDTO findById(Long id) throws Exception {
        ArticuloInsumo articuloInsumo = articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontró el artículo insumo con el ID: " + id));
        return articuloInsumoMapper.toDTO(articuloInsumo);
    }

    @Transactional
    public ArticuloInsumo findEntityById(Long id) throws Exception {
        return articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontró el artículo insumo con el ID: " + id));
    }

    @Transactional
    public ArticuloInsumoDTO save(ArticuloInsumoDTO dto) throws Exception {
        try {
            ArticuloInsumo insumo = articuloInsumoMapper.toEntity(dto);

            if (dto.getPrecioCompra() == null || dto.getPrecioCompra() <= 0) {
                throw new Exception("El precio de compra debe ser mayor que cero.");
            }
            if (dto.getStockActual() == null || dto.getStockActual() < 0) {
                throw new Exception("El stock actual no puede ser negativo.");
            }
            if (dto.getStockMaximo() == null || dto.getStockMaximo() <= 0) {
                throw new Exception("El stock máximo debe ser mayor que cero.");
            }
            if (dto.getEsParaElaborar() == null) {
                throw new Exception("Debe indicar si el insumo es para elaborar.");
            }

            if (dto.getImagen() != null) {
                Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                        .orElseThrow(() -> new Exception("No se encontró la imagen con el ID: " + dto.getImagen().getId()));
                insumo.setImagen(imagen);
            } else {
                insumo.setImagen(null);
            }

            if (dto.getCategoria() == null || dto.getCategoria().getId() == null) {
                throw new Exception("Debe proporcionar una categoría para el artículo insumo.");
            }
            Categoria categoria = categoriaRepository.findById(dto.getCategoria().getId())
                    .orElseThrow(() -> new Exception(
                            "No se encontró la categoría con el ID: " + dto.getCategoria().getId()));
            insumo.setCategoria(categoria);

            if (dto.getUnidadMedida() == null || dto.getUnidadMedida().getId() == null) {
                throw new Exception("Debe proporcionar una unidad de medida para el artículo insumo.");
            }
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedida().getId())
                    .orElseThrow(() -> new Exception(
                            "No se encontró la unidad de medida con el ID: " + dto.getUnidadMedida().getId()));
            insumo.setUnidadMedida(unidadMedida);

            return articuloInsumoMapper.toDTO(articuloInsumoRepository.save(insumo));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public ArticuloInsumoDTO update(Long id, ArticuloInsumoDTO dto) throws Exception {
        try {
            ArticuloInsumo insumoExistente = articuloInsumoRepository.findById(id)
                    .orElseThrow(() -> new Exception("No se encontró el artículo insumo con el ID: " + id));

            insumoExistente.setDenominacion(dto.getDenominacion());
            insumoExistente.setPrecioVenta(dto.getPrecioVenta());

            if (dto.getPrecioCompra() == null || dto.getPrecioCompra() <= 0) {
                throw new Exception("El precio de compra debe ser mayor que cero.");
            }
            insumoExistente.setPrecioCompra(dto.getPrecioCompra());

            if (dto.getStockActual() == null || dto.getStockActual() < 0) {
                throw new Exception("El stock actual no puede ser negativo.");
            }
            insumoExistente.setStockActual(dto.getStockActual());

            if (dto.getStockMaximo() == null || dto.getStockMaximo() <= 0) {
                throw new Exception("El stock máximo debe ser mayor que cero.");
            }
            insumoExistente.setStockMaximo(dto.getStockMaximo());

            if (dto.getEsParaElaborar() == null) {
                throw new Exception("Debe indicar si el insumo es para elaborar.");
            }
            insumoExistente.setEsParaElaborar(dto.getEsParaElaborar());

            if (dto.getImagen() != null) {
                Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                        .orElseThrow(() -> new Exception("No se encontró la imagen con el ID: " + dto.getImagen().getId()));
                insumoExistente.setImagen(imagen);
            } else {
                insumoExistente.setImagen(null);
            }

            if (dto.getCategoria() != null && dto.getCategoria().getId() != null) {
                Categoria categoria = categoriaRepository.findById(dto.getCategoria().getId())
                        .orElseThrow(() -> new Exception(
                                "No se encontró la categoría con el ID: " + dto.getCategoria().getId()));
                insumoExistente.setCategoria(categoria);
            } else {
                throw new Exception("Debe proporcionar una categoría para el artículo insumo.");
            }

            if (dto.getUnidadMedida() != null && dto.getUnidadMedida().getId() != null) {
                UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedida().getId())
                        .orElseThrow(() -> new Exception(
                                "No se encontró la unidad de medida con el ID: " + dto.getUnidadMedida().getId()));
                insumoExistente.setUnidadMedida(unidadMedida);
            } else {
                throw new Exception("Debe proporcionar una unidad de medida para el artículo insumo.");
            }

            return articuloInsumoMapper.toDTO(articuloInsumoRepository.save(insumoExistente));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) throws Exception {
        if (!articuloInsumoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró el artículo insumo con el ID: " + id);
        }
        articuloInsumoRepository.deleteById(id);
    }

    @Transactional
    public List<ArticuloInsumoDTO> findAllByEsParaElaborarTrue() {
        return articuloInsumoRepository.findAllByEsParaElaborarTrue().stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ArticuloInsumoDTO> findAllByEsParaElaborarFalse() {
        return articuloInsumoRepository.findAllByEsParaElaborarFalse().stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ArticuloInsumoDTO> findAllByCategoriaId(Long categoriaId) {
        return articuloInsumoRepository.findAllByCategoria_Id(categoriaId).stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
