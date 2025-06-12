package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.mappers.CategoriaMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repo;
    private final CategoriaMapper categoriaMapper;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;

    // Para GET /api/categorias
    public List<CategoriaDTO> findAll() {
        return categoriaMapper.toDTOList(repo.findAllByEliminadoFalse());
    }

    // Nuevo método para GET /api/categorias/deleted
    public List<CategoriaDTO> findAllDeleted() {
        return categoriaMapper.toDTOList(repo.findAllByEliminadoTrue());
    }

    public CategoriaDTO findById(Long id) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Categoría no encontrada con ID: " + id));
        return categoriaMapper.toDTO(categoria);
    }

    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        Optional<Categoria> existente = repo.findByDenominacionIgnoreCase(categoriaDTO.getDenominacion());
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe una categoría con la denominación: " + categoriaDTO.getDenominacion());
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        return categoriaMapper.toDTO(repo.save(categoria));
    }

    public CategoriaDTO update(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Categoría no encontrada con ID: " + id));
        if (categoriaDTO.getDenominacion() != null &&
                !categoriaDTO.getDenominacion().equalsIgnoreCase(categoriaExistente.getDenominacion())) {
            Optional<Categoria> otraCategoriaConMismaDenominacion = repo
                    .findByDenominacionIgnoreCase(categoriaDTO.getDenominacion());
            if (otraCategoriaConMismaDenominacion.isPresent()
                    && !otraCategoriaConMismaDenominacion.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe otra categoría con la denominación: " + categoriaDTO.getDenominacion());
            }
            categoriaExistente.setDenominacion(categoriaDTO.getDenominacion());
        }
        // Actualizar otros campos si los hubiera y fueran modificables
        // Ejemplo:
        // categoriaExistente.setDescripcion(categoriaDTO.getDescripcion());
        return categoriaMapper.toDTO(repo.save(categoriaExistente));
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se puede eliminar: Categoría no encontrada con ID: " + id));

        // Eliminar artículos manufacturados de esta categoría y subcategorías recursivamente (soft delete)
        eliminarArticulosDeCategoriaYSubcategorias(categoria);

        // Baja lógica de la categoría
        categoria.setEliminado(true);
        repo.save(categoria);
    }

    @Transactional
    public void restore(Long id) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se puede restaurar: Categoría no encontrada con ID: " + id));
        categoria.setEliminado(false);
        repo.save(categoria);
    }

    private void eliminarArticulosDeCategoriaYSubcategorias(Categoria categoria) {
        // 1. Eliminar artículos manufacturados de esta categoría
        List<ArticuloManufacturado> articulos = articuloManufacturadoRepository.findAllByCategoria_Id(categoria.getId());
        for (ArticuloManufacturado articulo : articulos) {
            articulo.setEliminado(true);
            articulo.setFechaEliminacion(LocalDateTime.now());
            articuloManufacturadoRepository.save(articulo);
        }
        // 2. Buscar subcategorías directas usando el método optimizado
        List<Categoria> subcategorias = repo.findAllByTipoCategoria_Id(categoria.getId());
        for (Categoria sub : subcategorias) {
            eliminarArticulosDeCategoriaYSubcategorias(sub);
        }
    }
}