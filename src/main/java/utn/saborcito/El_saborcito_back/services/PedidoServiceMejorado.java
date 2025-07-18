package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;
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
    private final ProduccionAnalisisService produccionAnalisisService;
    private final PedidoMapper pedidoMapper;

    // Constantes para lógicas de negocio
    private static final String ESTADO_LISTO = "LISTO";

    // NUEVO: DTO para respuesta que incluye pedido y factura
    public static class PedidoConFacturaDTO {
        private PedidoDTO pedido;
        private Long facturaId;

        public PedidoConFacturaDTO() {
        }

        public PedidoConFacturaDTO(PedidoDTO pedido, Long facturaId) {
            this.pedido = pedido;
            this.facturaId = facturaId;
        }

        public PedidoDTO getPedido() {
            return pedido;
        }

        public void setPedido(PedidoDTO pedido) {
            this.pedido = pedido;
        }

        public Long getFacturaId() {
            return facturaId;
        }

        public void setFacturaId(Long facturaId) {
            this.facturaId = facturaId;
        }
    }

    @Transactional
    public PedidoConFacturaDTO crearPedidoCompleto(PedidoCreacionDTO dto) {
        try {
            validarDatosCreacion(dto);

            // 🎯 Validar que todos los artículos se puedan producir ANTES de crear el
            // pedido
            // Usa la misma lógica que el endpoint /analizar-produccion
            validarProducibilidadPedido(dto);

            Pedido pedido = crearPedidoBase(dto);
            List<DetallePedido> detalles = crearDetallesPedido(pedido, dto.getDetalles());
            aplicarPromociones(pedido, detalles, dto.getPromocionesSeleccionadas());

            // 🆕 Establecer estado automático si todos los artículos son insumos
            establecerEstadoAutomatico(pedido);
            stockService.descontarStockPorPedido(pedido);
            calculadoraService.actualizarTotalesPedido(pedido);
            // Tiempo estimado y descuentos ya se calculan en actualizarTotalesPedido

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

    /**
     * 🆕 Método público para verificar si un pedido contiene solo artículos insumo
     */
    public Boolean esPedidoSoloInsumos(Pedido pedido) {
        return calculadoraService.esPedidoSoloInsumos(pedido);
    }

    /**
     * 🆕 Establece automáticamente el estado del pedido basado en sus artículos
     */
    private void establecerEstadoAutomatico(Pedido pedido) {
        // Solo aplicar si no tiene estado definido
        if (pedido.getEstado() == null) {
            // Si todos los artículos son insumos, establecer estado LISTO
            if (calculadoraService.esPedidoSoloInsumos(pedido)) {
                try {
                    Optional<Estado> estadoListoOpt = estadoRepository.findByNombre(ESTADO_LISTO);
                    if (estadoListoOpt.isPresent()) {
                        pedido.setEstado(estadoListoOpt.get());
                    }
                } catch (Exception e) {
                    // Si no existe el estado LISTO, continuar sin establecer estado
                    System.err.println("Estado LISTO no encontrado: " + e.getMessage());
                }
            }
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
            if (dto.getPromocionesSeleccionadas() == null || dto.getPromocionesSeleccionadas().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El pedido debe tener al menos un detalle o una promoción");
            }
        }

        // NUEVO: Validar producibilidad antes de crear el pedido
        validarProducibilidadPedido(dto);
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

            // 🎯 Solo buscar y validar que el artículo existe - NO validar stock aquí
            // La validación de stock se hace posteriormente de manera unificada en
            // StockService
            Articulo articulo = articuloValidacionService.buscarYValidarArticulo(dto.getArticuloId());

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
        // Solución: limpiar y agregar detalles en vez de reemplazar la lista
        if (pedido.getDetalles() == null) {
            pedido.setDetalles(new ArrayList<>());
        } else {
            pedido.getDetalles().clear();
        }
        pedido.getDetalles().addAll(detalles);
        // Asegurar la relación bidireccional
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setPedido(pedido);
        }
    }

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
                return facturaGuardada.getId();
            }
        } catch (IOException e) {
            System.err.println("Error al generar factura: " + e.getMessage());
        }
        return null;
    }

    public List<PedidoResumenPorClienteDTO> getPedidosPorCliente(Long clienteId, LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos = pedidoRepository.findAllByCliente_IdAndFechaPedidoBetween(clienteId, desde, hasta);

        return pedidos.stream().map(pedido -> {

            List<DetallePedidoDTO> detallesDTO = new ArrayList<>();

            // Detalles individuales
            for (DetallePedido dp : pedido.getDetalles()) {
                if (dp.getOrigen() == OrigenDetalle.INDIVIDUAL) {
                    detallesDTO.add(new DetallePedidoDTO(
                            dp.getId(),
                            dp.getCantidad(),
                            dp.getCantidadConPromocion(),
                            dp.getCantidadSinPromocion(),
                            dp.getSubtotal(),
                            dp.getOrigen(),
                            dp.getPromocionOrigenId(),
                            new ArticuloDTO(
                                    dp.getArticulo().getId(),
                                    dp.getArticulo().getDenominacion(),
                                    dp.getArticulo().getPrecioVenta(),
                                    null, null, false, null)));
                }
            }

            // Promociones aplicadas
            List<DetallePedidoPromocion> promociones = pedido.getPromociones();
            if (promociones != null) {
                for (DetallePedidoPromocion dpp : promociones) {
                    Promocion promo = dpp.getPromocion();
                    detallesDTO.add(new DetallePedidoDTO(
                            dpp.getId(),
                            dpp.getCantidadPromocion(),
                            null,
                            null,
                            dpp.getPrecioTotalPromocion(),
                            OrigenDetalle.PROMOCION,
                            promo.getId(),
                            new ArticuloDTO(
                                    promo.getId(),
                                    "🎁 " + promo.getDenominacion(),
                                    promo.getPrecioPromocional(),
                                    null, null, false, null)));
                }
            }

            PedidoResumenPorClienteDTO dto = new PedidoResumenPorClienteDTO();
            dto.setIdPedido(pedido.getId());
            dto.setFechaPedido(pedido.getFechaPedido());
            dto.setTotal(pedido.getTotal());
            dto.setDetalles(detallesDTO);
            return dto;
        }).collect(Collectors.toList());
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

    /**
     * 🎯 Valida que todos los artículos del pedido puedan ser producidos/obtenidos
     * Usa la misma lógica que el endpoint /analizar-produccion para consistencia
     *
     * @param dto Los datos del pedido a validar
     */
    private void validarProducibilidadPedido(PedidoCreacionDTO dto) {
        // Convertir detalles del pedido al mismo formato que usa el endpoint de
        // análisis
        Map<Long, Double> articulosMap = new HashMap<>();

        // Agregar artículos de detalles normales
        if (dto.getDetalles() != null) {
            for (PedidoCreacionDTO.DetallePedidoCreacionDTO detalle : dto.getDetalles()) {
                articulosMap.merge(detalle.getArticuloId(), detalle.getCantidad().doubleValue(), Double::sum);
            }
        }

        // Agregar artículos de promociones (si existen)
        if (dto.getPromocionesSeleccionadas() != null) {
            for (PromocionSeleccionadaDTO promocionSeleccionada : dto.getPromocionesSeleccionadas()) {
                Promocion promocion = promocionRepository.findById(promocionSeleccionada.getPromocionId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Promoción no encontrada: " + promocionSeleccionada.getPromocionId()));

                if (promocion.getPromocionDetalles() != null) {
                    for (PromocionDetalle detalle : promocion.getPromocionDetalles()) {
                        Double cantidadTotal = detalle.getCantidadRequerida() *
                                promocionSeleccionada.getCantidad().doubleValue();
                        articulosMap.merge(detalle.getArticulo().getId(), cantidadTotal, Double::sum);
                    }
                }
            }
        }

        // Usar exactamente la misma validación que el endpoint de análisis
        var analisis = produccionAnalisisService.analizarProduccionCompleta(articulosMap);

        if (!analisis.isSePuedeProducirCompleto()) {
            StringBuilder mensaje = new StringBuilder("No se puede procesar el pedido: ");

            if (!analisis.getProductosConProblemas().isEmpty()) {
                mensaje.append("Productos con problemas: ");
                for (var problema : analisis.getProductosConProblemas()) {
                    mensaje.append(problema.getArticuloId()).append(" (").append(problema.getMotivoProblema())
                            .append("), ");
                }
            }

            if (!analisis.getInsumosInsuficientes().isEmpty()) {
                mensaje.append("Insumos insuficientes: ");
                for (var insumo : analisis.getInsumosInsuficientes()) {
                    mensaje.append(insumo.getDenominacion())
                            .append(" (disponible: ").append(insumo.getStockDisponible())
                            .append(", necesario: ").append(insumo.getCantidadNecesaria()).append("), ");
                }
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje.toString());
        }
    }
}
