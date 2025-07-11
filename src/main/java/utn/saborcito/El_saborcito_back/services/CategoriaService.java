package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.CategoriaDTO;
import utn.saborcito.El_saborcito_back.mappers.CategoriaMapper;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repo;
    private final CategoriaMapper categoriaMapper;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;

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

    // NUEVO: Verificar si una categoría puede ser restaurada
    public Map<String, Object> canRestoreCategoria(Long id) {
        Map<String, Object> result = new HashMap<>();

        // Buscar la categoría por ID (eliminada o no)
        Categoria categoria = repo.findById(id)
                .orElse(null);

        if (categoria == null || !categoria.isEliminado()) {
            result.put("canRestore", false);
            result.put("message", "Categoría no encontrada o no está eliminada");
            return result;
        }

        // Si es una categoría padre (tipoCategoria == null), siempre se puede restaurar
        if (categoria.getTipoCategoria() == null) {
            result.put("canRestore", true);
            return result;
        }

        // Si es una subcategoría, verificar que su padre no esté eliminado
        Categoria categoriaPadre = categoria.getTipoCategoria();
        if (categoriaPadre.isEliminado()) {
            result.put("canRestore", false);
            result.put("message", "No se puede restaurar esta subcategoría porque su categoría padre está eliminada");
            return result;
        }

        result.put("canRestore", true);
        return result;
    }

    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        Optional<Categoria> existente = repo.findByDenominacionIgnoreCase(categoriaDTO.getDenominacion());
        if (existente.isPresent()) {
            if (existente.get().isEliminado()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe una categoría con ese nombre, pero está eliminada. Debe restaurarla o elegir otro nombre.");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe una categoría activa con la denominación: " + categoriaDTO.getDenominacion());
            }
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);

        // Validar que la sucursal no sea null
        if (categoria.getSucursal() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sucursal es obligatoria.");
        }

        // Copiar tipo del padre si es subcategoría
        if (categoria.getTipoCategoria() != null) {
            Categoria padre = repo.findById(categoria.getTipoCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría padre no encontrada"));
            categoria.setTipo(padre.getTipo());
        }

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
                if (otraCategoriaConMismaDenominacion.get().isEliminado()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Ya existe una categoría con ese nombre, pero está eliminada. Debe restaurarla o elegir otro nombre.");
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Ya existe otra categoría activa con la denominación: " + categoriaDTO.getDenominacion());
                }
            }
            categoriaExistente.setDenominacion(categoriaDTO.getDenominacion());
        }

        // Si se cambia el padre, actualizar el tipo
        if (categoriaDTO.getTipoCategoria() != null) {
            Categoria padre = repo.findById(categoriaDTO.getTipoCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría padre no encontrada"));
            categoriaExistente.setTipoCategoria(padre);
            categoriaExistente.setTipo(padre.getTipo());
        }

        // Actualizar sucursal si viene en el DTO
        if (categoriaDTO.getSucursal() != null && categoriaDTO.getSucursal().getId() != null) {
            // Si tienes un repositorio de sucursal, puedes validar que exista:
            // Sucursal sucursal = sucursalRepository.findById(categoriaDTO.getSucursal().getId())
            //     .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrada"));
            // categoriaExistente.setSucursal(sucursal);
            // Si no, simplemente asigna el id recibido:
            categoriaExistente.getSucursal().setId(categoriaDTO.getSucursal().getId());
        }

        return categoriaMapper.toDTO(repo.save(categoriaExistente));
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se puede eliminar: Categoría no encontrada con ID: " + id));

        // Eliminar artículos manufacturados e insumos de esta categoría y subcategorías recursivamente (soft delete)
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
        // 1b. Eliminar insumos de esta categoría
        List<ArticuloInsumo> insumos = articuloInsumoRepository.findAllByCategoria_IdAll(categoria.getId());
        for (ArticuloInsumo insumo : insumos) {
            insumo.setEliminado(true);
            // Si tienes campo fechaEliminacion en insumo, setéalo aquí
            articuloInsumoRepository.save(insumo);
        }
        // 2. Buscar subcategorías directas usando el método optimizado
        List<Categoria> subcategorias = repo.findAllByTipoCategoria_Id(categoria.getId());
        for (Categoria sub : subcategorias) {
            eliminarArticulosDeCategoriaYSubcategorias(sub);
        }
    }
}