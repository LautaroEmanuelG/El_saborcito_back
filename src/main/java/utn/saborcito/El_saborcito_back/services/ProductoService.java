package utn.saborcito.El_saborcito_back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ProductoDto;
import utn.saborcito.El_saborcito_back.enums.TransaccionTipo;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.ProductoRepository;
import utn.saborcito.El_saborcito_back.repositories.TicketRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ValorService valorService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private TicketRepository ticketRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findByIsDeletedFalse();
    }

    public Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Producto guardarProducto(ProductoDto productoDto) {
        Producto producto;
        if (productoDto.getId() != null) {
            // Actualizar producto existente
            producto = actualizarProducto(productoDto);
        } else {
            // Crear nuevo producto
            producto = crearProducto(productoDto);
        }
        return producto;
    }

    private Producto crearProducto(ProductoDto productoDto) {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(productoDto.getNombre());
        nuevoProducto.setDescripcion(productoDto.getDescripcion());
        nuevoProducto.setStock(productoDto.getStock());
        nuevoProducto.setFechaCreacion(new Date());

        // Establecer categoría
        Categoria categoria = categoriaService.buscarCategoriaPorId(productoDto.getCategoria().getId());
        if (categoria == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la categoría con ID: " + productoDto.getCategoria().getId());
        }
        nuevoProducto.setCategoria(categoria);

        // Crear y guardar Valor
        Valor valor = new Valor();
        valor.setPrecio(productoDto.getValor().getPrecio());
        valor.setCosto(productoDto.getValor().getCosto());
        valor.setFechaModificacion(new Date());

        Valor savedValor = valorService.guardarValor(valor);
        nuevoProducto.setValor(savedValor);

        return productoRepository.save(nuevoProducto);
    }

    private Producto actualizarProducto(ProductoDto productoDto) {
        Producto productoExistente = productoRepository.findById(productoDto.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el producto con ID: " + productoDto.getId())
        );

        int stockDifference = productoDto.getStock() - productoExistente.getStock();
        productoExistente.setNombre(productoDto.getNombre());
        productoExistente.setDescripcion(productoDto.getDescripcion());
        productoExistente.setStock(productoDto.getStock() == null ? productoExistente.getStock() : productoDto.getStock());

        // Establecer categoría
        Categoria categoria = categoriaService.buscarCategoriaPorId(productoDto.getCategoria().getId());
        if (categoria == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la categoría con ID: " + productoDto.getCategoria().getId());
        }
        productoExistente.setCategoria(categoria);

        // Actualizar y guardar Valor
        Valor valorExistente = productoExistente.getValor();
        valorExistente.setPrecio(productoDto.getValor().getPrecio());
        valorExistente.setCosto(productoDto.getValor().getCosto());
        valorExistente.setFechaModificacion(new Date());

        Valor savedValor = valorService.guardarValor(valorExistente);
        productoExistente.setValor(savedValor);

        Producto productoActualizado = productoRepository.save(productoExistente);

        // Crear y guardar TicketProducto
        TicketProducto ticketProducto = new TicketProducto();
        ticketProducto.setProducto(productoExistente);
        ticketProducto.setCantidad(stockDifference);

        // Crear y guardar Ticket
        double total = stockDifference * savedValor.getPrecio();
        Ticket ticket = new Ticket();
        ticket.setFecha(new Date());
        ticket.setTotal(-total); // Set total as negative
        ticket.setTicketProductos(new ArrayList<>()); // Initialize the list
        ticket = ticketRepository.save(ticket);

        ticketProducto.setTicket(ticket);
        ticket.getTicketProductos().add(ticketProducto);

        // Crear y guardar Transaccion
        Transaccion transaccion = new Transaccion();
        transaccion.setTicket(ticket);
        transaccion.setTipo(TransaccionTipo.MERCADERIA);
        transaccionService.guardarTransaccion(transaccion);

        return productoActualizado;
    }

    @Transactional
    public void eliminarProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el producto con ID: " + id)
        );
        producto.setIsDeleted(true);
        productoRepository.save(producto);
    }

    public List<Producto> listarProductosPorCategoria(Long id) {
        return productoRepository.findByCategoriaIdAndIsDeletedFalse(id);
    }
}