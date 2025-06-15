package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.OrigenDetalle;
import utn.saborcito.El_saborcito_back.mappers.SucursalMapper;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SucursalService {
    private final SucursalRepository repo;
    private final DomicilioRepository domicilioRepository;
    private final SucursalMapper sucursalMapper;
    private final DetallePedidoRepository detallePedidoRepo;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoPromocionRepository detallePromocionRepo;
    


    //RANKING DE PRODUCTOS
    public ResponseEntity<byte[]> exportarRankingProductosExcel(LocalDate desde, LocalDate hasta) {
        List<ProductoRankingDTO> ranking = getRankingProductos(desde, hasta).getProductos();

        long totalManu = ranking.stream()
                .filter(p -> "MANUFACTURADO".equals(p.getTipoProducto()))
                .mapToLong(ProductoRankingDTO::getCantidadVendida)
                .sum();

        long totalInsumo = ranking.stream()
                .filter(p -> "INSUMO".equals(p.getTipoProducto()))
                .mapToLong(ProductoRankingDTO::getCantidadVendida)
                .sum();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ranking Productos");

            // Cabecera de tabla
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Denominación");
            headerRow.createCell(2).setCellValue("Cantidad Vendida");
            headerRow.createCell(3).setCellValue("Tipo");

            // Cuerpo
            int rowNum = 1;
            for (ProductoRankingDTO dto : ranking) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getId());
                row.createCell(1).setCellValue(dto.getDenominacion());
                row.createCell(2).setCellValue(dto.getCantidadVendida());
                row.createCell(3).setCellValue(dto.getTipoProducto());
            }

            // Espacio
            rowNum += 2;

            // Título resumen
            Row resumenTitulo = sheet.createRow(rowNum++);
            resumenTitulo.createCell(0).setCellValue("Resumen de Ventas por Tipo");

            // Encabezado resumen
            Row resumenHeader = sheet.createRow(rowNum++);
            resumenHeader.createCell(0).setCellValue("Tipo");
            resumenHeader.createCell(1).setCellValue("Cantidad Vendida");

            // Valores resumen
            Row manuRow = sheet.createRow(rowNum++);
            manuRow.createCell(0).setCellValue("MANUFACTURADO");
            manuRow.createCell(1).setCellValue(totalManu);

            Row insumoRow = sheet.createRow(rowNum++);
            insumoRow.createCell(0).setCellValue("INSUMO");
            insumoRow.createCell(1).setCellValue(totalInsumo);

            // Exportar
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ranking-productos.xlsx");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel", e);
        }
    }


    public ProductoRankingConResumenDTO getRankingProductos(LocalDate desde, LocalDate hasta) {
        Map<Articulo, Long> conteo = new HashMap<>();

        // 1) artículos individuales
        detallePedidoRepo.findAllByPedido_FechaPedidoBetween(desde, hasta)
            .forEach(detalle -> 
                conteo.merge(detalle.getArticulo(),
                            detalle.getCantidad().longValue(),
                            Long::sum)
            );

        // 2) añadir artículos de las promociones
        detallePromocionRepo.findAllByPedidoFechaPedidoBetween(desde, hasta)
            .forEach(dpp -> {
                Promocion promo = dpp.getPromocion();
                int veces = dpp.getCantidadPromocion();
                // cada Articulo dentro de la promo
                promo.getPromocionDetalles().forEach(pd -> {
                    long unidades = (long) pd.getCantidadRequerida() * veces;
                    conteo.merge(pd.getArticulo(), unidades, Long::sum);
                });
            });

        // 3) armar DTO y resumen
        List<ProductoRankingDTO> ranking = new ArrayList<>();
        long totalManu = 0, totalInsumo = 0;

        for (Map.Entry<Articulo, Long> entry : conteo.entrySet()) {
            Articulo art = entry.getKey();
            long cant = entry.getValue();
            String tipo = art instanceof ArticuloManufacturado ? "MANUFACTURADO" : "INSUMO";

            ranking.add(new ProductoRankingDTO(
                art.getId(),
                art.getDenominacion(),
                cant,
                tipo
            ));
            if (tipo.equals("MANUFACTURADO")) totalManu += cant;
            else                               totalInsumo += cant;
        }

        ranking.sort(Comparator.comparingLong(ProductoRankingDTO::getCantidadVendida).reversed());
        return new ProductoRankingConResumenDTO(ranking, totalManu, totalInsumo);
    }

    // RANKING DE CLIENTES

    // DTO de promoción para front
    public static class PromocionDetalleDTO {
        private Long idPromocion;
        private String denominacion;
        private Integer cantidadPromocion;
        private Double precioTotalPromocion;
    }

    /**
     * Devuelve los pedidos de un cliente incluyendo detalles de productos y promociones
     */
    public List<PedidoResumenPorClienteDTO> getPedidosPorCliente(Long clienteId, LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos = pedidoRepository.findAllByCliente_IdAndFechaPedidoBetween(clienteId, desde, hasta);

        return pedidos.stream()
            .map(pedido -> {
                List<DetallePedidoDTO> detallesDTO = new ArrayList<>();

                // 1) Detalles individuales
                for (DetallePedido dp : pedido.getDetalles()) {
                    if (dp.getOrigen() == OrigenDetalle.INDIVIDUAL) {
                        detallesDTO.add(new DetallePedidoDTO(
                            dp.getId(),
                            dp.getCantidad(),
                            dp.getCantidadConPromocion(),
                            dp.getCantidadSinPromocion(),
                            dp.getSubtotal(),           // subtotal histórico
                            dp.getOrigen(),
                            dp.getPromocionOrigenId(),
                            new ArticuloDTO(
                                dp.getArticulo().getId(),
                                dp.getArticulo().getDenominacion(),
                                dp.getArticulo().getPrecioVenta(),
                                null, null, false, null
                            )
                        ));
                    }
                }

                // 2) Detalles de promoción: un DTO por cada promo aplicada
                List<DetallePedidoPromocion> promos = detallePromocionRepo.findByPedidoId(pedido.getId());
                for (DetallePedidoPromocion dpp : promos) {
                    Promocion promo = dpp.getPromocion();
                    detallesDTO.add(new DetallePedidoDTO(
                        // usamos el propio ID de la tabla detalle_pedido_promocion
                        dpp.getId(),
                        dpp.getCantidadPromocion(),     // cuántas veces aplicó la promo
                        null,                           // cantidadConPromocion no relevante aquí
                        null,                           // cantidadSinPromocion
                        dpp.getPrecioTotalPromocion(), // subtotal de la promo
                        OrigenDetalle.PROMOCION,
                        promo.getId(),
                        // en el artículo devolvemos la promoción en lugar de un artículo simple
                        new ArticuloDTO(
                            promo.getId(),
                            "🎁 " + promo.getDenominacion(),
                            promo.getPrecioPromocional(),
                            null, null, false, null
                        )
                    ));
                }

                // 3) Construcción del resumen
                PedidoResumenPorClienteDTO dto = new PedidoResumenPorClienteDTO();
                dto.setIdPedido(pedido.getId());
                dto.setFechaPedido(pedido.getFechaPedido());
                // total ya incluye promos y artículos normales
                dto.setTotal(pedido.getTotal());
                dto.setDetalles(detallesDTO);
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * Exporta a Excel los pedidos con sus detalles y promociones
     */
    public void exportarPedidosClienteExcel(
            Long clienteId,
            LocalDate desde,
            LocalDate hasta,
            HttpServletResponse response) throws IOException {
        List<PedidoResumenPorClienteDTO> pedidos =
            getPedidosPorCliente(clienteId, desde, hasta);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pedidos Cliente");
            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID Pedido");
            header.createCell(1).setCellValue("Fecha Pedido");
            header.createCell(2).setCellValue("Artículo");
            header.createCell(3).setCellValue("Cantidad");
            header.createCell(4).setCellValue("Precio Unitario");
            header.createCell(5).setCellValue("Subtotal");

            int rowNum = 1;
            for (var pedido : pedidos) {
                for (var det : pedido.getDetalles()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(pedido.getIdPedido());
                    row.createCell(1).setCellValue(pedido.getFechaPedido().toString());
                    row.createCell(2).setCellValue(det.getArticulo().getDenominacion());
                    row.createCell(3).setCellValue(det.getCantidad());
                    double precioUnit = det.getOrigen() == OrigenDetalle.PROMOCION
                        ? det.getSubtotal()
                        : det.getArticulo().getPrecioVenta();
                    row.createCell(4).setCellValue(precioUnit);
                    row.createCell(5).setCellValue(det.getSubtotal());
                }
                // Fila de total
                Row totalRow = sheet.createRow(rowNum++);
                totalRow.createCell(4).setCellValue("Total Pedido");
                totalRow.createCell(5).setCellValue(pedidos.stream()
                    .filter(p -> p.getIdPedido().equals(pedido.getIdPedido()))
                    .mapToDouble(PedidoResumenPorClienteDTO::getTotal).sum()
                );
            }

            for (int i = 0; i <= 5; i++) sheet.autoSizeColumn(i);

            response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=pedidos-cliente-" + clienteId + ".xlsx"
            );
            workbook.write(response.getOutputStream());
        }
    }

    public void exportarRankingClientesExcel(LocalDate desde, LocalDate hasta, String ordenarPor,
            HttpServletResponse response) {
        List<ClienteRankingDTO> ranking = getRankingClientes(desde, hasta, ordenarPor);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ranking Clientes");

            // Cabecera
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID Cliente");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Cantidad Pedidos");
            headerRow.createCell(3).setCellValue("Total Gastado");

            // Datos
            int rowNum = 1;
            for (ClienteRankingDTO cliente : ranking) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cliente.getIdCliente());
                row.createCell(1).setCellValue(cliente.getNombreCompleto());
                row.createCell(2).setCellValue(cliente.getCantidadPedidos());
                row.createCell(3).setCellValue(cliente.getTotalImporte());
            }

            // Configurar respuesta
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=ranking-clientes.xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel de clientes", e);
        }
    }


    public List<ClienteRankingDTO> getRankingClientes(LocalDate desde, LocalDate hasta, String ordenarPor) {
        try {
            System.out.println("=== DEBUG RANKING CLIENTES ===");
            System.out.println("Fecha desde: " + desde);
            System.out.println("Fecha hasta: " + hasta);
            System.out.println("Ordenar por: " + ordenarPor);

            // Buscar pedidos
            List<Pedido> pedidos = pedidoRepository.findAllByFechaPedidoBetween(desde, hasta);
            System.out.println("Cantidad de pedidos encontrados: " + pedidos.size());

            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos en el período especificado");
                return new ArrayList<>();
            }

            // Filtrar pedidos con datos válidos
            List<Pedido> pedidosValidos = pedidos.stream()
                    .filter(pedido -> pedido != null && pedido.getCliente() != null)
                    .collect(Collectors.toList());

            System.out.println("Pedidos válidos: " + pedidosValidos.size());

            if (pedidosValidos.isEmpty()) {
                System.out.println("No hay pedidos válidos en el período");
                return new ArrayList<>();
            }

            // Agrupar por ID de cliente (más confiable que por objeto Cliente)
            Map<Long, List<Pedido>> pedidosPorClienteId = pedidosValidos.stream()
                    .collect(Collectors.groupingBy(pedido -> pedido.getCliente().getId()));

            System.out.println("Clientes únicos: " + pedidosPorClienteId.size());

            // Crear DTOs agrupados correctamente
            List<ClienteRankingDTO> ranking = pedidosPorClienteId.entrySet().stream()
                    .map(entry -> {
                        try {
                            Long clienteId = entry.getKey();
                            List<Pedido> pedidosCliente = entry.getValue();

                            // Tomar el primer pedido para obtener datos del cliente
                            Cliente cliente = pedidosCliente.get(0).getCliente();

                            // Calcular totales
                            long cantidadPedidos = pedidosCliente.size();

                            // CAMBIO: Usar la misma lógica que getMovimientos()
                            // Solo sumar pedidos que tienen total válido (distinto de null y mayor a 0)
                            double totalImporte = pedidosCliente.stream()
                                    .mapToDouble(pedido -> pedido.getTotal() != null && pedido.getTotal() > 0
                                            ? pedido.getTotal()
                                            : 0.0)
                                    .sum();

                            String nombreCompleto = construirNombreCompleto(cliente);

                            System.out.println("Cliente: " + nombreCompleto + " - Pedidos: " + cantidadPedidos
                                    + " - Total: " + totalImporte);

                            return new ClienteRankingDTO(
                                    clienteId, // Usar Long directamente
                                    nombreCompleto,
                                    cantidadPedidos,
                                    totalImporte);
                        } catch (Exception e) {
                            System.err.println("Error procesando cliente: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .sorted((a, b) -> {
                        try {
                            if ("importe".equalsIgnoreCase(ordenarPor)) {
                                return Double.compare(b.getTotalImporte(), a.getTotalImporte());
                            } else {
                                return Long.compare(b.getCantidadPedidos(), a.getCantidadPedidos());
                            }
                        } catch (Exception e) {
                            System.err.println("Error ordenando: " + e.getMessage());
                            return 0;
                        }
                    })
                    .collect(Collectors.toList());

            System.out.println("Ranking final generado con " + ranking.size() + " clientes únicos");
            return ranking;

        } catch (Exception e) {
            System.err.println("ERROR GENERAL en getRankingClientes: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener ranking de clientes: " + e.getMessage(), e);
        }
    }

    // Método helper para construir nombre completo de forma segura
    private String construirNombreCompleto(Cliente cliente) {
        try {
            if (cliente == null) {
                return "Cliente desconocido";
            }

            String nombre = cliente.getNombre();
            String apellido = cliente.getApellido();

            // Manejar casos de nombres/apellidos nulos o vacíos
            if (nombre == null)
                nombre = "";
            if (apellido == null)
                apellido = "";

            String nombreCompleto = (nombre.trim() + " " + apellido.trim()).trim();

            if (nombreCompleto.isEmpty()) {
                return "Cliente #" + cliente.getId();
            }

            return nombreCompleto;

        } catch (Exception e) {
            System.err.println("Error construyendo nombre para cliente " +
                    (cliente != null ? cliente.getId() : "null") + ": " + e.getMessage());
            return "Cliente #" + (cliente != null ? cliente.getId() : "unknown");
        }
    }


    // MOVIMIENTOS MONETARIOS

    public MovimientoMonetarioDTO getMovimientos(LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos = pedidoRepository.findAllByFechaPedidoBetween(desde, hasta);

        double ingresos = pedidos.stream()
                .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                .sum();

        // Cambiar esta línea para usar directamente el campo total_costo de la BD
        double costos = pedidos.stream()
                .mapToDouble(p -> p.getTotalCosto() != null && p.getTotalCosto() > 0 ? p.getTotalCosto() : 0.0)
                .sum();

        double ganancias = ingresos - costos;

        return new MovimientoMonetarioDTO(ingresos, costos, ganancias);
    }

    public List<PedidoGananciaDetalleDTO> getDetalleGanancias(LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos = pedidoRepository.findAllByFechaPedidoBetween(desde, hasta);

        return pedidos.stream()
                .filter(pedido -> pedido.getTotal() != null && pedido.getTotal() > 0)
                .map(pedido -> new PedidoGananciaDetalleDTO(
                        pedido.getId(),
                        pedido.getFechaPedido(),
                        pedido.getTotal()))
                .collect(Collectors.toList());
    }

    public List<PedidoCostoDetalleDTO> getDetalleCostos(LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos = pedidoRepository.findAllByFechaPedidoBetween(desde, hasta);

        return pedidos.stream()
                .filter(pedido -> pedido.getTotalCosto() != null && pedido.getTotalCosto() > 0) // Filtrar primero
                .map(pedido -> new PedidoCostoDetalleDTO(
                        pedido.getId(),
                        pedido.getFechaPedido(),
                        pedido.getTotalCosto() // ← CAMBIAR AQUÍ: usar getTotalCosto() en lugar de calcularCostoTotal()
                ))
                .collect(Collectors.toList());
    }

    public void exportarDetalleGananciasExcel(LocalDate desde, LocalDate hasta, HttpServletResponse response)
            throws IOException {
        List<PedidoGananciaDetalleDTO> ganancias = getDetalleGanancias(desde, hasta);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Detalle Ganancias");

            // Cabecera
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID Pedido");
            headerRow.createCell(1).setCellValue("Fecha Pedido");
            headerRow.createCell(2).setCellValue("Total");

            // Datos
            int rowNum = 1;
            double totalGanancias = 0;
            for (PedidoGananciaDetalleDTO ganancia : ganancias) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ganancia.getIdPedido());
                row.createCell(1).setCellValue(ganancia.getFechaPedido().toString());
                row.createCell(2).setCellValue(ganancia.getTotal());
                totalGanancias += ganancia.getTotal();
            }

            // Fila de total
            Row totalRow = sheet.createRow(rowNum + 1);
            totalRow.createCell(1).setCellValue("TOTAL:");
            totalRow.createCell(2).setCellValue(totalGanancias);

            // Configurar respuesta
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=detalle-ganancias.xlsx");
            workbook.write(response.getOutputStream());
        }
    }

    public void exportarDetalleCostosExcel(LocalDate desde, LocalDate hasta, HttpServletResponse response)
            throws IOException {
        List<PedidoCostoDetalleDTO> costos = getDetalleCostos(desde, hasta);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Detalle Costos");

            // Cabecera
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID Pedido");
            headerRow.createCell(1).setCellValue("Fecha Pedido");
            headerRow.createCell(2).setCellValue("Total Costo");

            // Datos
            int rowNum = 1;
            double totalCostos = 0;
            for (PedidoCostoDetalleDTO costo : costos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(costo.getIdPedido());
                row.createCell(1).setCellValue(costo.getFechaPedido().toString());
                row.createCell(2).setCellValue(costo.getTotalCosto());
                totalCostos += costo.getTotalCosto();
            }

            // Fila de total
            Row totalRow = sheet.createRow(rowNum + 1);
            totalRow.createCell(1).setCellValue("TOTAL:");
            totalRow.createCell(2).setCellValue(totalCostos);

            // Configurar respuesta
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=detalle-costos.xlsx");
            workbook.write(response.getOutputStream());
        }
    }

    public List<SucursalDTO> findAll() {
        return repo.findAll().stream()
                .map(sucursalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SucursalDTO findById(Long id) {
        Sucursal sucursal = repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id));
        return sucursalMapper.toDTO(sucursal);
    }

    public SucursalDTO create(SucursalDTO dto) {
        Sucursal suc = sucursalMapper.toEntity(dto);

        // Validaciones existentes (adaptadas si es necesario para trabajar con la
        // entidad 'suc' antes de guardar)
        if (suc.getDomicilio() != null && suc.getDomicilio().getId() != null) {
            Domicilio domicilioExistente = domicilioRepository.findById(suc.getDomicilio().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Domicilio proporcionado para la nueva sucursal no encontrado con ID: "
                                    + suc.getDomicilio().getId()));
            suc.setDomicilio(domicilioExistente);
        } else if (suc.getDomicilio() != null) {
            // Si el domicilio es nuevo (sin ID), se guardará por cascada.
            // Asegurarse que el usuario dentro de domicilio no intente crearse si ya existe
            // o si no se desea.
            // Por simplicidad, asumimos que si viene un domicilio nuevo, se crea.
            // Considerar la lógica para el usuario dentro del domicilio si es necesario.
            if (suc.getDomicilio().getUsuario() != null && suc.getDomicilio().getUsuario().getId() != null) {
                // Lógica para asociar usuario existente si es necesario, o validar.
            }
        }

        if (suc.getEmpresa() != null && suc.getEmpresa().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La empresa asociada a la sucursal debe tener un ID válido o ser nula.");
        }
        // Aquí se podrían añadir más validaciones, como verificar que la empresa exista
        // en la BD si se proporciona un ID.

        Sucursal savedSucursal = repo.save(suc);
        return sucursalMapper.toDTO(savedSucursal);
    }

    public SucursalDTO update(Long id, SucursalDTO dto) {
        Sucursal existingSucursal = repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id));

        // Mapear los campos actualizables del DTO a la entidad existente
        // Esto podría ser más granular si no todos los campos del DTO son actualizables
        // o si se necesita lógica especial.

        existingSucursal.setNombre(dto.getNombre());

        // Manejo del domicilio
        if (dto.getDomicilio() != null) {
            if (dto.getDomicilio().getId() != null) {
                Domicilio domicilioAActualizar = domicilioRepository.findById(dto.getDomicilio().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Domicilio no encontrado con ID: " + dto.getDomicilio().getId()));
                // Actualizar campos del domicilio existente desde el DTO
                domicilioAActualizar.setCalle(dto.getDomicilio().getCalle());
                domicilioAActualizar.setNumero(dto.getDomicilio().getNumero());
                domicilioAActualizar.setCp(dto.getDomicilio().getCp());
                // Manejar localidad si es necesario (asumimos que la localidad se asigna por ID
                // y ya existe)
                // Si la localidad también puede cambiar, se necesitaría lógica similar.
                existingSucursal.setDomicilio(domicilioAActualizar);
            } else {
                // Si se envía un domicilio sin ID en una actualización, podría significar crear
                // uno nuevo
                // o podría ser un error. Aquí se asume que se reemplaza/crea.
                // Esta lógica puede necesitar ser más robusta dependiendo de los requisitos.
                Domicilio nuevoDomicilio = new Domicilio();
                nuevoDomicilio.setCalle(dto.getDomicilio().getCalle());
                nuevoDomicilio.setNumero(dto.getDomicilio().getNumero());
                nuevoDomicilio.setCp(dto.getDomicilio().getCp());
                // Asignar localidad (requiere que LocalidadDTO tenga ID y que exista)
                // Esta parte necesitaría el LocalidadMapper y LocalidadRepository si se
                // crea/actualiza profundamente
                existingSucursal.setDomicilio(nuevoDomicilio); // Esto podría requerir guardar el domicilio primero si
                                                               // no hay cascada completa
            }
        } else {
            existingSucursal.setDomicilio(null); // Si el DTO no trae domicilio, se elimina la asociación
        }

        // Manejo de la empresa (asumimos que se asigna por ID y ya existe)
        if (dto.getEmpresa() != null && dto.getEmpresa().getId() != null) {
            // Aquí se buscaría la empresa por ID y se asignaría.
            // Por simplicidad, y dado que el mapper lo haría si la entidad Empresa está en
            // el DTO:
            // Sucursal entidadActualizadaDesdeDto = sucursalMapper.toEntity(dto);
            // existingSucursal.setEmpresa(entidadActualizadaDesdeDto.getEmpresa());
            // O más directamente si solo se actualiza la referencia por ID:
            // Empresa empresa =
            // empresaRepository.findById(dto.getEmpresa().getId()).orElseThrow(...);
            // existingSucursal.setEmpresa(empresa);
            // Por ahora, si el mapper está configurado, esto podría funcionar, pero es
            // delicado.
            // Para ser más explícito, se debería cargar la entidad Empresa.
        } else {
            existingSucursal.setEmpresa(null);
        }

        // Horarios: Esto es más complejo. Si la lista de horarios puede cambiar
        // (añadir, quitar, modificar),
        // se necesita una lógica para sincronizar la colección.
        // MapStruct puede mapear colecciones, pero la actualización de colecciones JPA
        // a menudo requiere un manejo cuidadoso (borrar y recrear, o comparar y
        // actualizar elementos).
        // Por simplicidad, si el DTO trae horarios, se reemplazan.
        // Esto podría no ser lo ideal si los horarios tienen su propio ciclo de vida
        // complejo.
        // existingSucursal.setHorarios(sucursalMapper.toEntity(dto).getHorarios()); //
        // Esto requiere que HorarioMapper esté bien configurado

        Sucursal updatedSucursal = repo.save(existingSucursal);
        return sucursalMapper.toDTO(updatedSucursal);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id);
        repo.deleteById(id);
    }
}