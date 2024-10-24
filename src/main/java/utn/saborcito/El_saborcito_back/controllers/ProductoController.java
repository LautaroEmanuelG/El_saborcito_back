package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ProductoDto;
import utn.saborcito.El_saborcito_back.models.Producto;
import utn.saborcito.El_saborcito_back.services.ProductoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/all")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/id")
    public ResponseEntity<Producto> buscarProductoPorId(@RequestParam Long id) {
        Producto producto = productoService.buscarProductoPorId(id);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el producto con ID: " + id);
        }
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<Producto>> listarProductosPorCategoria(@RequestParam Long id) {
        return ResponseEntity.ok(productoService.listarProductosPorCategoria(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarProducto(@RequestBody ProductoDto productoDto, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Producto productoGuardado = productoService.guardarProducto(productoDto);
        return ResponseEntity.ok(productoGuardado);
    }

    @PostMapping("/eliminar")
    public ResponseEntity<String> eliminarProductoPorId(@RequestParam Long id) {
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