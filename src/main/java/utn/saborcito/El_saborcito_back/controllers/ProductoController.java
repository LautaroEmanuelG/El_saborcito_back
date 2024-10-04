package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ProductoDto;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.models.Producto;
import utn.saborcito.El_saborcito_back.models.Valor;
import utn.saborcito.El_saborcito_back.services.CategoriaService;
import utn.saborcito.El_saborcito_back.services.ProductoService;
import utn.saborcito.El_saborcito_back.services.ValorService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ValorService valorService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/all")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/id")
    public ResponseEntity<Producto> buscarProductoPorId(Long id) {
        Producto producto = productoService.buscarProductoPorId(id);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el producto con ID: " + id);
        }
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<Producto>> listarProductosPorCategoria(Long id) {
        return ResponseEntity.ok(productoService.listarProductosPorCategoria(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarProducto(@RequestBody ProductoDto productoDto, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        // Fetch and set the Categoria
        Categoria categoria = categoriaService.buscarCategoriaPorId(productoDto.getCategoria().getId());
        if (categoria == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la categoria con ID: " + productoDto.getCategoria().getId());
        }

        Producto productoCompleto;
        if (productoDto.getId() != null) {
            // Update existing product
            productoCompleto = productoService.buscarProductoPorId(productoDto.getId());
            if (productoCompleto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el producto con ID: " + productoDto.getId());
            }
            productoCompleto.setNombre(productoDto.getNombre());
            productoCompleto.setDescripcion(productoDto.getDescripcion());
            productoCompleto.setStock(productoDto.getStock() == null ? productoCompleto.getStock() : productoDto.getStock());
            productoCompleto.setCategoria(categoria);
        } else {
            // Create new product
            productoCompleto = new Producto();
            productoCompleto.setNombre(productoDto.getNombre());
            productoCompleto.setDescripcion(productoDto.getDescripcion());
            productoCompleto.setStock(productoDto.getStock());
            productoCompleto.setFechaCreacion(new Date());
            productoCompleto.setCategoria(categoria);
        }

        Valor valor = new Valor();
        valor.setId(productoDto.getValor().getId());
        valor.setPrecio(productoDto.getValor().getPrecio());
        valor.setCosto(productoDto.getValor().getCosto());
        valor.setFechaModificacion(new Date());

        // Save the Valor entity first
        Valor savedValor = valorService.guardarValor(valor);
        productoCompleto.setValor(savedValor);

        // Save the Producto entity
        Producto savedProducto = productoService.guardarProducto(productoCompleto);

        return ResponseEntity.ok(savedProducto);
    }

    @PostMapping("/eliminar")
    public ResponseEntity<String> eliminarProductoPorId(Long id) {
        if (productoService.buscarProductoPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el producto con ID: " + id);
        }
        productoService.eliminarProductoPorId(id);
        return ResponseEntity.ok("Producto eliminado");
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(e -> {
            errors.put(e.getField(), e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
