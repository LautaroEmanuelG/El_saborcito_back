package utn.saborcito.El_saborcito_back.controllers;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import utn.saborcito.El_saborcito_back.dto.ClienteRankingDTO;
import utn.saborcito.El_saborcito_back.dto.MovimientoMonetarioDTO;
import utn.saborcito.El_saborcito_back.dto.ProductoRankingDTO;
import utn.saborcito.El_saborcito_back.dto.SucursalDTO;
import utn.saborcito.El_saborcito_back.services.SucursalService;

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

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarRankingProductosExcel(
        @RequestParam LocalDate desde,
        @RequestParam LocalDate hasta
    ) {
        return service.exportarRankingProductosExcel(desde, hasta);
    }

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


    @GetMapping("/ranking-clientes")
    public List<ClienteRankingDTO> getRankingClientes(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
        @RequestParam(defaultValue = "cantidad") String ordenarPor
    ) {
        return service.getRankingClientes(desde, hasta, ordenarPor);
    }

    @GetMapping("/ranking-productos")
    public List<ProductoRankingDTO> getRankingProductos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getRankingProductos(desde, hasta);
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
