package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import utn.saborcito.El_saborcito_back.dto.ClienteRankingDTO;
import utn.saborcito.El_saborcito_back.dto.MovimientoMonetarioDTO;
import utn.saborcito.El_saborcito_back.dto.ProductoRankingDTO;
import utn.saborcito.El_saborcito_back.dto.SucursalDTO;
import utn.saborcito.El_saborcito_back.services.SucursalService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {
    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping("/movimientos")
    public MovimientoMonetarioDTO getMovimientos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return service.getMovimientos(desde, hasta);
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
