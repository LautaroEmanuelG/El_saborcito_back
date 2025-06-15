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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoServiceMejorado {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final SucursalRepository sucursalRepository;
    private final EstadoRepository estadoRepository;
    private final TipoEnvioRepository tipoEnvioRepository;
    private final FormaPagoRepository formaPagoRepository;
    private final LocalidadRepository localidadRepository;
    private final DomicilioRepository domicilioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PromocionRepository promocionRepository;

    private final StockService stockService;
    private final FacturaService facturaService;
    private final CalculadoraPedidoService calculadoraService;
    private final ArticuloValidacionService articuloValidacionService;
    private final PromocionComboService promocionComboService;
    private final PedidoMapper pedidoMapper;

    @Transactional
    public PedidoDTO crearPedidoCompleto(PedidoCreacionDTO dto) {
        try {
            validarDatosCreacion(dto);
            
            Pedido pedido = crearPedidoBase(dto);
            List<DetallePedido> detalles = crearDetallesPedido(pedido, dto.getDetalles());
            
            aplicarPromociones(pedido, detalles, dto.getPromocionesSeleccionadas());
            
            stockService.descontarStockPorPedido(pedido);
            calculadoraService.actualizarTotalesPedido(pedido);
            calcularTiempoEstimado(pedido);

            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            crearFacturaAutomatica(pedidoGuardado);

            return pedidoMapper.toDTO(pedidoGuardado);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear pedido: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> validarPromociones(List<Long> promocionIds) {
        Map<String, Object> resultado = new HashMap<>();
        
        for (Long promocionId : promocionIds) {
            try {
                Promocion promocion = promocionRepository.findById(promocionId)
                        .orElseThrow(() -> new RuntimeException("Promoción no encontrada: " + promocionId));
                
                resultado.put("promocion_" + promocionId, crearInfoValidacion(promocion));
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", e.getMessage());
                resultado.put("promocion_" + promocionId, error);
            }
        }
        
        return resultado;
    }

    private void validarDatosCreacion(PedidoCreacionDTO dto) {
        if (dto.getClienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID del cliente es obligatorio");
        }
        if (dto.getSucursalId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de la sucursal es obligatorio");
        }
        if (dto.getFormaPagoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de la forma de pago es obligatorio");
        }
    }

    private Pedido crearPedidoBase(PedidoCreacionDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.now());
        establecerReferencias(pedido, dto);
        
        if (dto.getDomicilio() != null) {
            crearNuevoDomicilio(pedido.getCliente(), dto.getDomicilio());
        }
        
        return pedido;
    }

    private void establecerReferencias(Pedido pedido, PedidoCreacionDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        
        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
        
        FormaPago formaPago = formaPagoRepository.findById(dto.getFormaPagoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de pago no encontrada"));

        Estado estadoPendiente = estadoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado PENDIENTE no encontrado"));

        TipoEnvio tipoEnvio = tipoEnvioRepository.findByNombre("TAKE_AWAY")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de envío TAKE_AWAY no encontrado"));

        pedido.setCliente(cliente);
        pedido.setSucursal(sucursal);
        pedido.setFormaPago(formaPago);
        pedido.setEstado(estadoPendiente);
        pedido.setTipoEnvio(tipoEnvio);
    }

    private void crearNuevoDomicilio(Cliente cliente, PedidoCreacionDTO.DomicilioCreacionDTO domicilioDTO) {
        Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

        Domicilio domicilio = Domicilio.builder()
                .calle(domicilioDTO.getCalle())
                .numero(domicilioDTO.getNumero())
                .cp(domicilioDTO.getCp())
                .latitud(domicilioDTO.getLatitud())
                .longitud(domicilioDTO.getLongitud())
                .localidad(localidad)
                .build();

        Usuario usuario = usuarioRepository.findByCliente_Id(cliente.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (usuario.getDomicilios() == null) {
            usuario.setDomicilios(new ArrayList<>());
        }

        Domicilio domicilioGuardado = domicilioRepository.save(domicilio);
        usuario.getDomicilios().add(domicilioGuardado);
        usuarioRepository.save(usuario);
    }

    private List<DetallePedido> crearDetallesPedido(Pedido pedido, List<PedidoCreacionDTO.DetallePedidoCreacionDTO> detallesDTO) {
        if (detallesDTO == null || detallesDTO.isEmpty()) {
            return new ArrayList<>();
        }

        return detallesDTO.stream().map(dto -> {
            Articulo articulo = articuloValidacionService.buscarYValidarArticulo(dto.getArticuloId());
            
            validarStockArticulo(articulo, dto.getCantidad());

            DetallePedido detalle = DetallePedido.builder()
                    .cantidad(dto.getCantidad())
                    .cantidadSinPromocion(dto.getCantidad())
                    .cantidadConPromocion(0)
                    .pedido(pedido)
                    .articulo(articulo)
                    .build();

            detalle.calcularYEstablecerSubtotal();
            return detalle;
        }).toList();
    }

    private void validarStockArticulo(Articulo articulo, int cantidad) {
        if (articuloValidacionService.esArticuloInsumo(articulo)) {
            articuloValidacionService.validarStockInsumo((ArticuloInsumo) articulo, cantidad);
        } else if (articuloValidacionService.esArticuloManufacturado(articulo)) {
            articuloValidacionService.validarStockManufacturado((ArticuloManufacturado) articulo, cantidad);
        }
    }

    private void aplicarPromociones(Pedido pedido, List<DetallePedido> detalles, List<PromocionSeleccionadaDTO> promocionesSeleccionadas) {
        if (promocionesSeleccionadas != null && !promocionesSeleccionadas.isEmpty()) {
            List<DetallePedidoPromocion> promociones = promocionComboService.aplicarPromocionesSeleccionadas(
                    pedido, detalles, promocionesSeleccionadas, pedido.getSucursal());
            pedido.setPromociones(promociones);
        }
        pedido.setDetalles(detalles);
    }

    private void calcularTiempoEstimado(Pedido pedido) {
        int maxMinutosCocina = pedido.getDetalles().stream()
                .filter(det -> det.getArticulo() instanceof ArticuloManufacturado)
                .mapToInt(det -> ((ArticuloManufacturado) det.getArticulo()).getTiempoEstimadoMinutos())
                .max()
                .orElse(15);

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

            PedidoDTO pedidoRef = new PedidoDTO();
            pedidoRef.setId(pedido.getId());
            facturaDTO.setPedido(pedidoRef);

            FormaPagoDTO formaPagoRef = new FormaPagoDTO();
            formaPagoRef.setId(pedido.getFormaPago().getId());
            facturaDTO.setFormaPago(formaPagoRef);

            facturaService.save(facturaDTO);
        } catch (Exception e) {
            System.err.println("Error al generar factura: " + e.getMessage());
        }
    }

    private Map<String, Object> crearInfoValidacion(Promocion promocion) {
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        
        boolean fechaValida = !promocion.getFechaDesde().isAfter(fechaActual) && !promocion.getFechaHasta().isBefore(fechaActual);
        boolean horarioValido = promocion.getHoraDesde() == null || promocion.getHoraHasta() == null ||
                (!horaActual.isBefore(promocion.getHoraDesde()) && !horaActual.isAfter(promocion.getHoraHasta()));
        
        Map<String, Object> info = new HashMap<>();
        info.put("denominacion", promocion.getDenominacion());
        info.put("fechaDesde", promocion.getFechaDesde());
        info.put("fechaHasta", promocion.getFechaHasta());
        info.put("horaDesde", promocion.getHoraDesde());
        info.put("horaHasta", promocion.getHoraHasta());
        info.put("fechaActual", fechaActual);
        info.put("horaActual", horaActual);
        info.put("fechaValida", fechaValida);
        info.put("horarioValido", horarioValido);
        info.put("vigente", fechaValida && horarioValido);
        
        return info;
    }
}
