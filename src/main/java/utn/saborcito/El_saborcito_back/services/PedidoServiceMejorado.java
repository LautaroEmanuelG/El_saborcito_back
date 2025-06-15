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
import java.util.*;
import java.util.stream.Collectors;

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

    // NUEVO: DTO para respuesta que incluye pedido y factura
    public static class PedidoConFacturaDTO {
        private PedidoDTO pedido;
        private Long facturaId;

        public PedidoConFacturaDTO() {}

        public PedidoConFacturaDTO(PedidoDTO pedido, Long facturaId) {
            this.pedido = pedido;
            this.facturaId = facturaId;
        }

        public PedidoDTO getPedido() { return pedido; }
        public void setPedido(PedidoDTO pedido) { this.pedido = pedido; }
        public Long getFacturaId() { return facturaId; }
        public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }
    }

    @Transactional
    public PedidoConFacturaDTO crearPedidoCompleto(PedidoCreacionDTO dto) {
        try {
            validarDatosCreacion(dto);

            Pedido pedido = crearPedidoBase(dto);
            List<DetallePedido> detalles = crearDetallesPedido(pedido, dto.getDetalles());

            aplicarPromociones(pedido, detalles, dto.getPromocionesSeleccionadas());

            stockService.descontarStockPorPedido(pedido);
            calculadoraService.actualizarTotalesPedido(pedido);
            calcularTiempoEstimado(pedido);

            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            Long facturaId = crearFacturaAutomatica(pedidoGuardado);

            return new PedidoConFacturaDTO(pedidoMapper.toDTO(pedidoGuardado), facturaId);
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
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            if (dto.getPromocionesSeleccionadas() == null || dto.getPromocionesSeleccionadas().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El pedido debe tener al menos un detalle o una promoción");
            }
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

        TipoEnvio tipoEnvio = tipoEnvioRepository.findById(dto.getTipoEnvioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de envío no encontrado"));

        FormaPago formaPago = formaPagoRepository.findById(dto.getFormaPagoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de pago no encontrada"));

        Estado estado;
        if (dto.getEstadoId() != null) {
            estado = estadoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado"));
        } else {
            estado = estadoRepository.findByNombre("PENDIENTE")
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado PENDIENTE no encontrado"));
        }

        pedido.setCliente(cliente);
        pedido.setSucursal(sucursal);
        pedido.setTipoEnvio(tipoEnvio);
        pedido.setFormaPago(formaPago);
        pedido.setEstado(estado);
    }

    private void crearNuevoDomicilio(Cliente cliente, PedidoCreacionDTO.DomicilioCreacionDTO domicilioDTO) {
        Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(domicilioDTO.getCalle());
        domicilio.setNumero(domicilioDTO.getNumero());
        domicilio.setCp(domicilioDTO.getCp());
        domicilio.setLatitud(domicilioDTO.getLatitud());
        domicilio.setLongitud(domicilioDTO.getLongitud());
        domicilio.setLocalidad(localidad);

        Usuario usuario = usuarioRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado para el cliente"));

        domicilio.setUsuario(usuario);

        if (usuario.getDomicilios() == null) {
            usuario.setDomicilios(new ArrayList<>());
        }

        Domicilio domicilioGuardado = domicilioRepository.save(domicilio);
        usuario.getDomicilios().add(domicilioGuardado);
        usuarioRepository.save(usuario);
    }

    private List<DetallePedido> crearDetallesPedido(Pedido pedido,
                                                    List<PedidoCreacionDTO.DetallePedidoCreacionDTO> detallesDTO) {
        if (detallesDTO == null || detallesDTO.isEmpty()) {
            return new ArrayList<>();
        }

        return detallesDTO.stream().map(dto -> {
            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(dto.getCantidad());
            detalle.setCantidadSinPromocion(dto.getCantidad());
            detalle.setCantidadConPromocion(0);
            detalle.setPedido(pedido);

            Articulo articulo = articuloValidacionService.buscarYValidarArticulo(dto.getArticuloId());

            if (articuloValidacionService.esArticuloInsumo(articulo)) {
                ArticuloInsumo insumo = (ArticuloInsumo) articulo;
                articuloValidacionService.validarStockInsumo(insumo, dto.getCantidad());
            } else if (articuloValidacionService.esArticuloManufacturado(articulo)) {
                ArticuloManufacturado manufacturado = (ArticuloManufacturado) articulo;
                articuloValidacionService.validarStockManufacturado(manufacturado, dto.getCantidad());
            }

            detalle.setArticulo(articulo);
            detalle.calcularYEstablecerSubtotal();

            return detalle;
        }).collect(Collectors.toList());
    }

    private void aplicarPromociones(Pedido pedido, List<DetallePedido> detalles,
                                    List<PromocionSeleccionadaDTO> promocionesSeleccionadas) {
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

    // MODIFICADO: Ahora retorna el ID de la factura generada
    private Long crearFacturaAutomatica(Pedido pedido) {
        try {
            if (pedido.getFormaPago() != null && pedido.getFormaPago().getId() != null) {
                FacturaDTO facturaDTO = FacturaDTO.builder()
                        .pedido(pedidoMapper.toDTO(pedido))
                        .formaPago(FormaPagoDTO.builder().id(pedido.getFormaPago().getId()).build())
                        .totalVenta(pedido.getTotal())
                        .fechaFacturacion(LocalDate.now())
                        .clienteEmail(pedido.getCliente().getEmail())
                        .build();

                FacturaDTO facturaGuardada = facturaService.save(facturaDTO);
                return facturaGuardada.getId(); // Retornar el ID de la factura
            }
        } catch (IOException e) {
            System.err.println("Error al generar factura: " + e.getMessage());
        }
        return null; // Si no se pudo generar la factura
    }

    private Map<String, Object> crearInfoValidacion(Promocion promocion) {
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        boolean fechaValida = !promocion.getFechaDesde().isAfter(fechaActual)
                && !promocion.getFechaHasta().isBefore(fechaActual);
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