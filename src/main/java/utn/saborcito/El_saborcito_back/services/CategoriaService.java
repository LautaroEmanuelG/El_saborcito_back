package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findByIsDeletedFalse();
    }

    public Categoria buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Categoria guardarCategoria(Categoria categoria) {
        Categoria categoriaCompleta;

        if (categoria.getId() != null) {
            // Update existing category
            categoriaCompleta = categoriaRepository.findById(categoria.getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la categoría con ID: " + categoria.getId())
            );
            categoriaCompleta.setNombre(categoria.getNombre());
            categoriaCompleta.setDescripcion(categoria.getDescripcion());
        } else {
            // Create new category
            categoriaCompleta = new Categoria();
            categoriaCompleta.setNombre(categoria.getNombre());
            categoriaCompleta.setDescripcion(categoria.getDescripcion());
        }

        // Save or update the category
        return categoriaRepository.save(categoriaCompleta);
    }

    @Transactional
    public void eliminarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la categoría con ID: " + id)
        );
        categoria.setIsDeleted(true);
        categoriaRepository.save(categoria);
    }
}