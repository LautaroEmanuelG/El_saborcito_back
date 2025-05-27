package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ArticuloInsumoService {
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloInsumoMapper articuloInsumoMapper;
    private final ImagenRepository imagenRepository;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;

    public List<ArticuloInsumoDTO> findAll() {
        return articuloInsumoRepository.findAll()
                .stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ArticuloInsumoDTO findById(Long id) {
        ArticuloInsumo articuloInsumo = articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ArticuloInsumo no encontrado con id: " + id));
        return articuloInsumoMapper.toDTO(articuloInsumo);
    }

    public ArticuloInsumo findEntityById(Long id) { // Método para obtener la entidad
        return articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ArticuloInsumo no encontrado con id: " + id));
    }

    public ArticuloInsumoDTO save(ArticuloInsumoDTO dto) {
        ArticuloInsumo articuloInsumo = articuloInsumoMapper.toEntity(dto);

        // Manejar relaciones
        if (dto.getImagen() != null && dto.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Imagen no encontrada con id: " + dto.getImagen().getId()));
            articuloInsumo.setImagen(imagen);
        } else {
            // Considerar lanzar error si la imagen es obligatoria o manejar la creación de
            // una nueva imagen
            articuloInsumo.setImagen(null); // O manejar de otra forma
        }

        if (dto.getCategoria() != null && dto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Categoría no encontrada con id: " + dto.getCategoria().getId()));
            articuloInsumo.setCategoria(categoria);
        } else {
            articuloInsumo.setCategoria(null); // O manejar según la lógica de negocio
        }

        if (dto.getUnidadMedida() != null && dto.getUnidadMedida().getId() != null) {
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedida().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Unidad de Medida no encontrada con id: " + dto.getUnidadMedida().getId()));
            articuloInsumo.setUnidadMedida(unidadMedida);
        } else {
            articuloInsumo.setUnidadMedida(null); // O manejar según la lógica de negocio
        }

        ArticuloInsumo savedArticuloInsumo = articuloInsumoRepository.save(articuloInsumo);
        return articuloInsumoMapper.toDTO(savedArticuloInsumo);
    }

    public ArticuloInsumoDTO update(Long id, ArticuloInsumoDTO dto) {
        ArticuloInsumo existingArticuloInsumo = articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ArticuloInsumo no encontrado con id: " + id));

        // Actualizar campos simples
        existingArticuloInsumo.setDenominacion(dto.getDenominacion());
        existingArticuloInsumo.setPrecioVenta(dto.getPrecioVenta());
        existingArticuloInsumo.setPrecioCompra(dto.getPrecioCompra());
        existingArticuloInsumo.setStockActual(dto.getStockActual());
        existingArticuloInsumo.setStockMaximo(dto.getStockMaximo());
        existingArticuloInsumo.setEsParaElaborar(dto.getEsParaElaborar());

        // Manejar relaciones (similar a save, pero actualizando existingArticuloInsumo)
        if (dto.getImagen() != null && dto.getImagen().getId() != null) {
            Imagen imagen = imagenRepository.findById(dto.getImagen().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Imagen no encontrada con id: " + dto.getImagen().getId()));
            existingArticuloInsumo.setImagen(imagen);
        } else {
            existingArticuloInsumo.setImagen(null);
        }

        if (dto.getCategoria() != null && dto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Categoría no encontrada con id: " + dto.getCategoria().getId()));
            existingArticuloInsumo.setCategoria(categoria);
        } else {
            existingArticuloInsumo.setCategoria(null);
        }

        if (dto.getUnidadMedida() != null && dto.getUnidadMedida().getId() != null) {
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedida().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Unidad de Medida no encontrada con id: " + dto.getUnidadMedida().getId()));
            existingArticuloInsumo.setUnidadMedida(unidadMedida);
        } else {
            existingArticuloInsumo.setUnidadMedida(null);
        }

        ArticuloInsumo updatedArticuloInsumo = articuloInsumoRepository.save(existingArticuloInsumo);
        return articuloInsumoMapper.toDTO(updatedArticuloInsumo);
    }

    public void delete(Long id) {
        if (!articuloInsumoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ArticuloInsumo no encontrado con id: " + id);
        }
        articuloInsumoRepository.deleteById(id);
    }

    public List<ArticuloInsumoDTO> findAllByEsParaElaborarTrue() {
        return articuloInsumoRepository.findAllByEsParaElaborarTrue()
                .stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ArticuloInsumoDTO> findAllByEsParaElaborarFalse() {
        return articuloInsumoRepository.findAllByEsParaElaborarFalse()
                .stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ArticuloInsumoDTO> findAllByCategoriaId(Long categoriaId) {
        return articuloInsumoRepository.findAllByCategoria_Id(categoriaId)
                .stream()
                .map(articuloInsumoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
