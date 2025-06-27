// src/main/java/utn/saborcito/El_saborcito_back/services/CompraInsumoService.java
package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;
import utn.saborcito.El_saborcito_back.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraInsumoService {

    private final CompraInsumoRepository compraRepo;
    private final CompraDetalleRepository detalleRepo;
    private final ArticuloInsumoRepository insumoRepo;

    @Transactional
    public CompraInsumoDTO registrarCompra(NuevaCompraDTO dto) {
        List<CompraDetalle> lineas = dto.getDetalles().stream()
                .map(det -> {
                    ArticuloInsumo ins = insumoRepo.findById(det.getInsumoId())
                            .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));
                    double sub = det.getCantidad() * det.getPrecioUnitario();

                    // ✅ Actualizar stock con Double
                    ins.setPrecioCompra(det.getPrecioUnitario());
                    ins.setStockActual(ins.getStockActual() + det.getCantidad());
                    insumoRepo.save(ins);

                    return CompraDetalle.builder()
                            .insumo(ins)
                            .cantidad(det.getCantidad())    // Ya es Double
                            .precioUnitario(det.getPrecioUnitario())
                            .subtotal(sub)
                            .build();
                }).collect(Collectors.toList());

        double total = lineas.stream()
            .mapToDouble(CompraDetalle::getSubtotal)
            .sum();

        // 2) Crear cabecera
        CompraInsumo compra = CompraInsumo.builder()
            .denominacion(dto.getDenominacion())
            .fechaCompra(LocalDate.now())
            .totalCompra(total)
            .detalles(lineas)
            .build();

        // 3) Asociar detalle → cabecera y guardar
        compra.getDetalles().forEach(d -> d.setCompra(compra));
        CompraInsumo saved = compraRepo.save(compra);

        // 4) Mapear a DTO
        List<CompraDetalleDTO> detallesDTO = saved.getDetalles().stream()
            .map(d -> new CompraDetalleDTO(
                d.getInsumo().getId(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal()
            )).collect(Collectors.toList());

        return new CompraInsumoDTO(
            saved.getId(),
            saved.getDenominacion(),
            saved.getFechaCompra(),
            saved.getTotalCompra(),
            detallesDTO
        );
    }

    @Transactional(readOnly = true)
    public List<CompraInsumoDTO> listarCompras() {
        return compraRepo.findAll().stream().map(compra -> {
            List<CompraDetalleDTO> dets = compra.getDetalles().stream().map(d ->
                new CompraDetalleDTO(
                    d.getInsumo().getId(),
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.getSubtotal()
                )
            ).collect(Collectors.toList());
            return new CompraInsumoDTO(
                compra.getId(),
                compra.getDenominacion(),
                compra.getFechaCompra(),
                compra.getTotalCompra(),
                dets
            );
        }).collect(Collectors.toList());
    }
}
