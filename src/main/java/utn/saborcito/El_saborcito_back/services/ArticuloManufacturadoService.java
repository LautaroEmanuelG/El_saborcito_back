package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ArticuloManufacturadoDTO;
import utn.saborcito.El_saborcito_back.mappers.ArticuloManufacturadoMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoService {

    private final ArticuloManufacturadoRepository repo;
    private final ArticuloManufacturadoMapper mapper;

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
        // Aquí podrías necesitar lógica adicional para manejar las relaciones, como
        // ArticuloManufacturadoDetalle
        return mapper.toDTO(repo.save(articuloManufacturado));
    }

    public ArticuloManufacturadoDTO update(Long id, ArticuloManufacturadoDTO dto) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Artículo manufacturado no encontrado para actualizar");
        }
        ArticuloManufacturado articuloManufacturado = mapper.toEntity(dto);
        articuloManufacturado.setId(id);
        // Aquí podrías necesitar lógica adicional para manejar las relaciones
        return mapper.toDTO(repo.save(articuloManufacturado));
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