package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.mappers.PedidoMapper;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 🚀 Servicio mejorado de Pedidos con funcionalidad completa
 */
@Service
@RequiredArgsConstructor
public class PedidoServiceMejorado {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final SucursalRepository sucursalRepository;
    private final EstadoRepository estadoRepository;
    private final TipoEnvioRepository tipoEnvioRepository;
    private final FormaPagoRepository formaPagoRepository;
    private final ArticuloRepository articuloRepository;
    private final LocalidadRepository localidadRepository;
    private final DomicilioRepository domicilioRepository;
    private final UsuarioRepository usuarioRepository;
    private final StockService stockService;
    private final FacturaService facturaService;
    private final CalculadoraPedidoService calculadoraService;
    private final ArticuloValidacionService articuloValidacionService;
    private final PedidoMapper pedidoMapper;

    /**
     * 🚀 Método principal para crear un pedido completo
     */
    @Transactional
    public PedidoDTO crearPedidoCompleto(PedidoCreacionDTO dto) {
        try {
            // 1. Validaciones básicas
            validarDatosCreacion(dto);

            // 2. Crear pedido
            Pedido pedido = new Pedido();
            pedido.setFechaPedido(LocalDate.now());

            // 3. Establecer referencias
            establecerReferencias(pedido, dto);

            // 4. Manejar domicilio si es necesario
            if (dto.getDomicilio() != null) {
                crearNuevoDomicilio(pedido.getCliente(), dto.getDomicilio());
            }

            // 5. Agregar detalles
            List<DetallePedido> detalles = crearDetallesPedido(pedido, dto.getDetalles());
            pedido.setDetalles(detalles);

            // 6. Validar y descontar stock
            stockService.descontarStockPorPedido(pedido);

            // 7. Calcular totales y tiempo
            calculadoraService.actualizarTotalesPedido(pedido);
            calcularTiempoEstimado(pedido);

            // 8. Guardar pedido
            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            // 9. Crear factura
            crearFacturaAutomatica(pedidoGuardado);

            return pedidoMapper.toDTO(pedidoGuardado);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error al crear pedido: " + e.getMessage(), e);
        }
    }

    private void validarDatosCreacion(PedidoCreacionDTO dto) {
        if (dto.getClienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID del cliente es obligatorio");
        }
        if (dto.getSucursalId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de la sucursal es obligatorio");
        }
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido debe tener al menos un detalle");
        }
    }

    private void establecerReferencias(Pedido pedido, PedidoCreacionDTO dto) {
        // Cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado"));
        pedido.setCliente(cliente);

        // Sucursal
        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sucursal no encontrada"));
        pedido.setSucursal(sucursal);

        // Estado
        Estado estado;
        if (dto.getEstadoId() != null) {
            estado = estadoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Estado no encontrado"));
        } else {
            estado = estadoRepository.findByNombre("PENDIENTE")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Estado PENDIENTE no encontrado"));
        }
        pedido.setEstado(estado);

        // Tipo de envío
        TipoEnvio tipoEnvio = tipoEnvioRepository.findById(dto.getTipoEnvioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tipo de envío no encontrado"));
        pedido.setTipoEnvio(tipoEnvio);

        // Forma de pago
        FormaPago formaPago = formaPagoRepository.findById(dto.getFormaPagoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Forma de pago no encontrada"));
        pedido.setFormaPago(formaPago);
    }

    private void crearNuevoDomicilio(Cliente cliente, PedidoCreacionDTO.DomicilioCreacionDTO domicilioDTO) {
        // Buscar la localidad
        Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Localidad no encontrada"));

        // Crear el domicilio
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(domicilioDTO.getCalle());
        domicilio.setNumero(domicilioDTO.getNumero());
        domicilio.setCp(domicilioDTO.getCp());
        domicilio.setLatitud(domicilioDTO.getLatitud());
        domicilio.setLongitud(domicilioDTO.getLongitud());
        domicilio.setLocalidad(localidad);
        // Asociar al usuario del cliente
        // Buscar el usuario asociado al cliente
        Usuario usuario = usuarioRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado para el cliente"));

        domicilio.setUsuario(usuario);

        // Inicializar lista de domicilios si es null
        if (usuario.getDomicilios() == null) {
            usuario.setDomicilios(new ArrayList<>());
        }

        // Guardar domicilio y agregarlo al usuario
        Domicilio domicilioGuardado = domicilioRepository.save(domicilio);
        usuario.getDomicilios().add(domicilioGuardado);
        usuarioRepository.save(usuario);
    }

    private List<DetallePedido> crearDetallesPedido(Pedido pedido,
            List<PedidoCreacionDTO.DetallePedidoCreacionDTO> detallesDTO) {

        return detallesDTO.stream().map(dto -> {
            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(dto.getCantidad());
            detalle.setPedido(pedido);

            // Usar el servicio de validación para buscar y validar el artículo
            Articulo articulo = articuloValidacionService.buscarYValidarArticulo(dto.getArticuloId());

            // Validar stock según el tipo de artículo
            if (articuloValidacionService.esArticuloInsumo(articulo)) {
                ArticuloInsumo insumo = (ArticuloInsumo) articulo;
                articuloValidacionService.validarStockInsumo(insumo, dto.getCantidad());
            } else if (articuloValidacionService.esArticuloManufacturado(articulo)) {
                ArticuloManufacturado manufacturado = (ArticuloManufacturado) articulo;
                articuloValidacionService.validarStockManufacturado(manufacturado, dto.getCantidad());
            }

            detalle.setArticulo(articulo);
            return detalle;
        }).collect(Collectors.toList());
    }

    private void calcularTiempoEstimado(Pedido pedido) {
        // Encontrar el tiempo máximo de los artículos manufacturados
        int maxMinutosCocina = pedido.getDetalles().stream()
                .filter(det -> det.getArticulo() instanceof ArticuloManufacturado)
                .mapToInt(det -> ((ArticuloManufacturado) det.getArticulo()).getTiempoEstimadoMinutos())
                .max()
                .orElse(15); // Tiempo base de 15 minutos si no hay manufacturados

        // Agregar tiempo de delivery si corresponde
        int minutosDelivery = "DELIVERY".equals(pedido.getTipoEnvio().getNombre()) ? 30 : 0;

        LocalTime horaEstimada = LocalTime.now().plusMinutes(maxMinutosCocina + minutosDelivery);
        pedido.setHorasEstimadaFinalizacion(horaEstimada);
    }

    private void crearFacturaAutomatica(Pedido pedido) {
        try {
            FacturaDTO facturaDTO = new FacturaDTO();
            facturaDTO.setTotalVenta(pedido.getTotal());
            facturaDTO.setFechaFacturacion(LocalDate.now());
            facturaDTO.setClienteEmail(pedido.getCliente().getEmail());

            // Referencias simples
            PedidoDTO pedidoRef = new PedidoDTO();
            pedidoRef.setId(pedido.getId());
            facturaDTO.setPedido(pedidoRef);

            FormaPagoDTO formaPagoRef = new FormaPagoDTO();
            formaPagoRef.setId(pedido.getFormaPago().getId());
            facturaDTO.setFormaPago(formaPagoRef);

            facturaService.save(facturaDTO);
        } catch (IOException e) {
            // Log del error pero no fallar la creación del pedido
            System.err.println("Error al generar factura: " + e.getMessage());
        }
    }
}
