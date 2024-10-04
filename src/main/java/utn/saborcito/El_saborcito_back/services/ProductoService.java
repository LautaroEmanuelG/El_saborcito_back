package utn.saborcito.El_saborcito_back.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.saborcito.El_saborcito_back.models.Producto;
import utn.saborcito.El_saborcito_back.repositories.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
    @Transactional
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminarProductoPorId(Long id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> listarProductosPorCategoria(Long id) {
        return productoRepository.findByCategoriaId(id);
    }
}
