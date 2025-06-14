package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.ProduccionAnalisisDTO;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.util.*;

/**
 * 🏭 Servicio unificado para análisis de producción y validación de stock
 * Centraliza toda la lógica de validación para evitar duplicaciones
 */
@Service
@RequiredArgsConstructor
public class ProduccionAnalisisService {

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;

    /**
     * 🎯 Verifica si un artículo manufacturado específico puede fabricarse
     * 
     * @param manufacturadoId ID del artículo manufacturado
     * @return true si puede fabricarse, false si no
     */
    @Transactional(readOnly = true)
    public boolean puedeProducirseArticuloManufacturado(Long manufacturadoId) {
        ArticuloManufacturado manufacturado = articuloManufacturadoRepository.findByIdNotDeleted(manufacturadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artículo manufacturado no encontrado"));

        return puedeProducirseManufacturado(manufacturado, 1);
    }

    /**
     * 🎯 Verifica si un artículo manufacturado puede fabricarse con una cantidad
     * específica
     * 
     * @param manufacturado El artículo manufacturado
     * @param cantidad      Cantidad a producir
     * @return true si puede fabricarse, false si no
     */
    public boolean puedeProducirseManufacturado(ArticuloManufacturado manufacturado, Integer cantidad) {
        if (manufacturado.getArticuloManufacturadoDetalles() == null ||
                manufacturado.getArticuloManufacturadoDetalles().isEmpty()) {
            return false;
        }

        return manufacturado.getArticuloManufacturadoDetalles().stream().allMatch(detalle -> {
            if (detalle.getArticuloInsumo() == null || detalle.getCantidad() == null) {
                return false;
            }
            Integer stockActual = detalle.getArticuloInsumo().getStockActual();
            Integer cantidadNecesaria = detalle.getCantidad() * cantidad;
            return stockActual != null && stockActual >= cantidadNecesaria;
        });
    }

    /**
     * 🎯 Verifica si un artículo insumo tiene stock suficiente
     * 
     * @param insumo   El artículo insumo
     * @param cantidad Cantidad requerida
     * @return true si hay stock suficiente, false si no
     */
    public boolean tieneStockSuficienteInsumo(ArticuloInsumo insumo, Integer cantidad) {
        if (insumo.getStockActual() == null) {
            return false;
        }
        // Verificar si es para elaborar (no se puede vender directamente)
        if (insumo.getEsParaElaborar() != null && insumo.getEsParaElaborar()) {
            return false;
        }
        return insumo.getStockActual() >= cantidad;
    }

    /**
     * 🎯 Análisis completo de producción para múltiples artículos
     * 
     * @param articulosConCantidad Mapa de ID de artículo -> cantidad
     * @return DTO con análisis completo
     */
    @Transactional(readOnly = true)
    public ProduccionAnalisisDTO analizarProduccionCompleta(Map<Long, Integer> articulosConCantidad) {
        ProduccionAnalisisDTO resultado = new ProduccionAnalisisDTO();

        // Mapa para almacenar el consumo total de cada insumo
        Map<Long, Integer> consumoTotalPorInsumo = new HashMap<>();

        // Mapa para almacenar la cantidad máxima producible de cada artículo
        Map<Long, Integer> maximoProducible = new HashMap<>();

        // Lista de productos con problemas
        List<ProduccionAnalisisDTO.ProductoProblemaDTO> productosConProblemas = new ArrayList<>();

        // Lista de insumos insuficientes
        List<ProduccionAnalisisDTO.InsumoInsuficienteDTO> insumosInsuficientes = new ArrayList<>();

        boolean sePuedeProducirCompleto = true;

        for (Map.Entry<Long, Integer> entrada : articulosConCantidad.entrySet()) {
            Long articuloId = entrada.getKey();
            Integer cantidad = entrada.getValue();

            // Intentar encontrar como ArticuloInsumo
            Optional<ArticuloInsumo> articuloInsumo = articuloInsumoRepository.findByIdNotDeleted(articuloId);

            if (articuloInsumo.isPresent()) {
                ArticuloInsumo insumo = articuloInsumo.get();

                // Verificar si es para elaborar (no se puede vender)
                if (insumo.getEsParaElaborar() != null && insumo.getEsParaElaborar()) {
                    // Buscar si existe como manufacturado también
                    Optional<ArticuloManufacturado> manufacturado = articuloManufacturadoRepository
                            .findByIdNotDeleted(articuloId);
                    if (manufacturado.isEmpty()) {
                        // Es un insumo solo para elaborar, no se puede vender
                        agregarProductoConProblema(productosConProblemas, articuloId,
                                "Artículo es solo para elaboración, no se puede vender directamente", 0);
                        sePuedeProducirCompleto = false;
                        continue;
                    }
                }

                // Validar stock del insumo
                if (!tieneStockSuficienteInsumo(insumo, cantidad)) {
                    agregarInsumoInsuficiente(insumosInsuficientes, insumo.getId(),
                            insumo.getDenominacion(), insumo.getStockActual(), cantidad);
                    agregarProductoConProblema(productosConProblemas, articuloId,
                            "Stock insuficiente", insumo.getStockActual());
                    sePuedeProducirCompleto = false;
                    maximoProducible.put(articuloId, insumo.getStockActual() != null ? insumo.getStockActual() : 0);
                } else {
                    maximoProducible.put(articuloId, insumo.getStockActual());
                }
                continue;
            }

            // Intentar encontrar como ArticuloManufacturado
            Optional<ArticuloManufacturado> articuloManufacturado = articuloManufacturadoRepository
                    .findByIdNotDeleted(articuloId);

            if (articuloManufacturado.isPresent()) {
                ArticuloManufacturado manufacturado = articuloManufacturado.get();

                if (manufacturado.getArticuloManufacturadoDetalles() == null ||
                        manufacturado.getArticuloManufacturadoDetalles().isEmpty()) {
                    agregarProductoConProblema(productosConProblemas, articuloId,
                            "No tiene receta definida", 0);
                    sePuedeProducirCompleto = false;
                    maximoProducible.put(articuloId, 0);
                    continue;
                }

                // Calcular cantidad máxima que se puede producir
                int cantidadMaxima = calcularCantidadMaximaManufacturado(manufacturado);
                maximoProducible.put(articuloId, cantidadMaxima);

                if (cantidadMaxima < cantidad) {
                    // Agregar problemas específicos de insumos
                    for (ArticuloManufacturadoDetalle detalle : manufacturado.getArticuloManufacturadoDetalles()) {
                        ArticuloInsumo insumo = detalle.getArticuloInsumo();
                        Integer cantidadNecesaria = detalle.getCantidad() * cantidad;

                        if (insumo.getStockActual() == null || insumo.getStockActual() < cantidadNecesaria) {
                            agregarInsumoInsuficiente(insumosInsuficientes, insumo.getId(),
                                    insumo.getDenominacion(), insumo.getStockActual(), cantidadNecesaria);
                        }
                    }

                    agregarProductoConProblema(productosConProblemas, articuloId,
                            "Insumos insuficientes para la cantidad solicitada", cantidadMaxima);
                    sePuedeProducirCompleto = false;
                }
                continue;
            }

            // Si no se encuentra ni como insumo ni como manufacturado
            agregarProductoConProblema(productosConProblemas, articuloId,
                    "Artículo no encontrado", 0);
            sePuedeProducirCompleto = false;
            maximoProducible.put(articuloId, 0);
        }

        // Completar resultado
        resultado.setSePuedeProducirCompleto(sePuedeProducirCompleto);
        resultado.setProductosConProblemas(productosConProblemas);
        resultado.setMaximoProducible(maximoProducible);
        resultado.setInsumosInsuficientes(insumosInsuficientes);

        return resultado;
    }

    /**
     * 🔢 Calcula la cantidad máxima que se puede producir de un manufacturado
     */
    private int calcularCantidadMaximaManufacturado(ArticuloManufacturado manufacturado) {
        return manufacturado.getArticuloManufacturadoDetalles().stream()
                .mapToInt(detalle -> {
                    ArticuloInsumo insumo = detalle.getArticuloInsumo();
                    if (insumo.getStockActual() == null || detalle.getCantidad() == null
                            || detalle.getCantidad() == 0) {
                        return 0;
                    }
                    return insumo.getStockActual() / detalle.getCantidad();
                })
                .min()
                .orElse(0);
    }

    /**
     * 📝 Agrega un producto con problema a la lista
     */
    private void agregarProductoConProblema(List<ProduccionAnalisisDTO.ProductoProblemaDTO> lista,
            Long articuloId, String problema, Integer cantidadMaxima) {
        ProduccionAnalisisDTO.ProductoProblemaDTO producto = new ProduccionAnalisisDTO.ProductoProblemaDTO();
        producto.setId(articuloId);
        producto.setDenominacion(problema); // Usar denominación para mostrar el problema
        producto.setCantidadProducible(cantidadMaxima);
        lista.add(producto);
    }

    /**
     * 📝 Agrega un insumo insuficiente a la lista
     */
    private void agregarInsumoInsuficiente(List<ProduccionAnalisisDTO.InsumoInsuficienteDTO> lista,
            Long insumoId, String nombre, Integer stockActual, Integer cantidadNecesaria) {
        // Verificar si ya existe en la lista y sumar
        Optional<ProduccionAnalisisDTO.InsumoInsuficienteDTO> existente = lista.stream()
                .filter(i -> i.getId().equals(insumoId))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setStockRequerido(
                    existente.get().getStockRequerido() + cantidadNecesaria);
            existente.get().setStockFaltante(
                    Math.max(0, existente.get().getStockRequerido() - stockActual));
        } else {
            ProduccionAnalisisDTO.InsumoInsuficienteDTO insumo = new ProduccionAnalisisDTO.InsumoInsuficienteDTO();
            insumo.setId(insumoId);
            insumo.setDenominacion(nombre);
            insumo.setStockActual(stockActual != null ? stockActual : 0);
            insumo.setStockRequerido(cantidadNecesaria);
            insumo.setStockFaltante(Math.max(0, cantidadNecesaria - (stockActual != null ? stockActual : 0)));
            lista.add(insumo);
        }
    }
}
