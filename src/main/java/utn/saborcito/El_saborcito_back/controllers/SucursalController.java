package utn.saborcito.El_saborcito_back.controllers;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utn.saborcito.El_saborcito_back.dto.PedidoResumenPorClienteDTO;
import jakarta.servlet.http.HttpServletResponse;
import utn.saborcito.El_saborcito_back.dto.ClienteRankingDTO;
import utn.saborcito.El_saborcito_back.dto.DetallePedidoDTO;
import utn.saborcito.El_saborcito_back.dto.MovimientoMonetarioDTO;
import utn.saborcito.El_saborcito_back.dto.ProductoRankingConResumenDTO;
import utn.saborcito.El_saborcito_back.dto.ProductoRankingDTO;
import utn.saborcito.El_saborcito_back.dto.SucursalDTO;
import utn.saborcito.El_saborcito_back.services.SucursalService;
import utn.saborcito.El_saborcito_back.dto.PedidoGananciaDetalleDTO;
import utn.saborcito.El_saborcito_back.dto.PedidoCostoDetalleDTO;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {
    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    //CLIENTES Y PEDIDOS

    @GetMapping("/pedidos-cliente")
    public List<PedidoResumenPorClienteDTO> getPedidosPorCliente(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getPedidosPorCliente(clienteId, desde, hasta);
    }

    @GetMapping("/ranking-clientes")
    public List<ClienteRankingDTO> getRankingClientes(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
        @RequestParam(defaultValue = "cantidad") String ordenarPor
    ) {
        return service.getRankingClientes(desde, hasta, ordenarPor);
    }


    @GetMapping("/exportar-pedidos-cliente-excel")
    public void exportarPedidosClienteExcel(
        @RequestParam Long clienteId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
        HttpServletResponse response
    ) throws IOException {
        service.exportarPedidosClienteExcel(clienteId, desde, hasta, response);
    }



    @GetMapping("/exportar-ranking-clientes-excel")
    public void exportarRankingClientesExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "cantidad") String ordenarPor,
            HttpServletResponse response
    ) throws IOException {
        service.exportarRankingClientesExcel(desde, hasta, ordenarPor, response);
    }



    //MOVIMIENTOS MONETARIOS
    @GetMapping("/movimientos")
    public MovimientoMonetarioDTO getMovimientos(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getMovimientos(desde, hasta);
    }

    @GetMapping("/exportar-movimientos-excel")
    public void exportarMovimientosExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response
    ) throws IOException {
        MovimientoMonetarioDTO datos = service.getMovimientos(desde, hasta);

        // Crear archivo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Movimientos");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Fecha Desde");
        header.createCell(1).setCellValue("Fecha Hasta");
        header.createCell(2).setCellValue("Ingresos");
        header.createCell(3).setCellValue("Costos");
        header.createCell(4).setCellValue("Ganancias");

        Row fila = sheet.createRow(1);
        fila.createCell(0).setCellValue(desde.toString());
        fila.createCell(1).setCellValue(hasta.toString());
        fila.createCell(2).setCellValue(datos.getIngresos());
        fila.createCell(3).setCellValue(datos.getCostos());
        fila.createCell(4).setCellValue(datos.getGanancias());

        // Configurar response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=movimientos-monetarios.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @GetMapping("/detalle-ganancias")
    public List<PedidoGananciaDetalleDTO> getDetalleGanancias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getDetalleGanancias(desde, hasta);
    }

    @GetMapping("/detalle-costos")
    public List<PedidoCostoDetalleDTO> getDetalleCostos(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getDetalleCostos(desde, hasta);
    }

    @GetMapping("/exportar-detalle-ganancias-excel")
    public void exportarDetalleGananciasExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response
    ) throws IOException {
        service.exportarDetalleGananciasExcel(desde, hasta, response);
    }

    @GetMapping("/exportar-detalle-costos-excel")
    public void exportarDetalleCostosExcel(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
      HttpServletResponse response
    ) throws IOException {
        service.exportarDetalleCostosExcel(desde, hasta, response);
    }

    // DETALLES DE PEDIDOS Y RANKING DE PRODUCTOS

    @GetMapping("/ranking-productos")
    public ProductoRankingConResumenDTO getRankingProductos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getRankingProductos(desde, hasta);
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarRankingProductosExcel(
        @RequestParam LocalDate desde,
        @RequestParam LocalDate hasta
    ) {
        return service.exportarRankingProductosExcel(desde, hasta);
    }

    @GetMapping
    public List<SucursalDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SucursalDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public SucursalDTO create(@RequestBody SucursalDTO sucursalDTO) {
        return service.create(sucursalDTO);
    }

    @PutMapping("/{id}")
    public SucursalDTO update(@PathVariable Long id, @RequestBody SucursalDTO sucursalDTO) {
        return service.update(id, sucursalDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
