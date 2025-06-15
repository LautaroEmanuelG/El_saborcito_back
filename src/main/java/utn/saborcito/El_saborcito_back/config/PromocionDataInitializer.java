package utn.saborcito.El_saborcito_back.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utn.saborcito.El_saborcito_back.models.*;
import utn.saborcito.El_saborcito_back.repositories.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 🚀 Inicializador de datos para promociones de ejemplo
 */
@Configuration
public class PromocionDataInitializer {

    /**
     * 🎁 Inicializa promociones de ejemplo en la base de datos
     */
    @Bean
    public CommandLineRunner initPromociones(
            PromocionRepository promocionRepo,
            PromocionDetalleRepository promocionDetalleRepo,
            ArticuloInsumoRepository articuloInsumoRepo,
            ArticuloManufacturadoRepository articuloManufacturadoRepo,
            SucursalRepository sucursalRepo) {

        return args -> {
            // Solo inicializar si no hay promociones
            if (promocionRepo.count() > 0) {
                return;
            }

            // Obtener sucursal principal (asumiendo que existe ID 1)
            Sucursal sucursal = sucursalRepo.findById(1L).orElse(null);
            if (sucursal == null) {
                System.out.println("⚠️ No se encontró sucursal con ID 1, saltando inicialización de promociones");
                return;
            }

            // Fechas de promoción
            LocalDate fechaInicio = LocalDate.now();
            LocalDate fechaFin = LocalDate.now().plusDays(60);

            // 🍺🍔 Promoción 1: Cerveza + Hamburguesa = $1500
            Promocion comboCervezaHamburguesa = promocionRepo.save(Promocion.builder()
                    .denominacion("🍺 Cerveza + Hamburguesa")
                    .fechaDesde(fechaInicio)
                    .fechaHasta(fechaFin)
                    .horaDesde(LocalTime.of(18, 0)) // Solo de noche
                    .horaHasta(LocalTime.of(23, 30))
                    .precioPromocional(1500.0)
                    .sucursal(sucursal)
                    .build());

            // Buscar artículos (ajustar IDs según tu BD)
            var cerveza = articuloInsumoRepo.findById(1L).orElse(null);
            var hamburguesa = articuloManufacturadoRepo.findById(1L).orElse(null);

            if (cerveza != null && hamburguesa != null) {
                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(comboCervezaHamburguesa)
                        .articulo(cerveza)
                        .cantidadRequerida(1)
                        .build());

                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(comboCervezaHamburguesa)
                        .articulo(hamburguesa)
                        .cantidadRequerida(1)
                        .build());
            } // 🥃🥩 Promoción 2: Fernet + 2 Lomos = $2500
            Promocion comboFernetLomos = promocionRepo.save(Promocion.builder()
                    .denominacion("🥃 Fernet + 2 Lomos")
                    .fechaDesde(fechaInicio)
                    .fechaHasta(fechaFin)
                    .horaDesde(LocalTime.of(19, 0))
                    .horaHasta(LocalTime.of(23, 59))
                    .precioPromocional(2500.0)
                    .sucursal(sucursal)
                    .build());

            var fernet = articuloInsumoRepo.findById(2L).orElse(null);
            var lomo = articuloManufacturadoRepo.findById(2L).orElse(null);

            if (fernet != null && lomo != null) {
                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(comboFernetLomos)
                        .articulo(fernet)
                        .cantidadRequerida(1)
                        .build());

                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(comboFernetLomos)
                        .articulo(lomo)
                        .cantidadRequerida(2) // 2 lomos
                        .build());
            }

            // 🍕🥤 Promoción 3: Pizza + Gaseosa = $1800
            Promocion comboPizzaGaseosa = promocionRepo.save(Promocion.builder()
                    .denominacion("🍕 Pizza + Gaseosa")
                    .fechaDesde(fechaInicio)
                    .fechaHasta(fechaFin)
                    .precioPromocional(1800.0)
                    .sucursal(sucursal)
                    .build());

            var pizza = articuloManufacturadoRepo.findById(3L).orElse(null);
            var gaseosa = articuloInsumoRepo.findById(3L).orElse(null);

            if (pizza != null && gaseosa != null) {
                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(comboPizzaGaseosa)
                        .articulo(pizza)
                        .cantidadRequerida(1)
                        .build());

                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(comboPizzaGaseosa)
                        .articulo(gaseosa)
                        .cantidadRequerida(1)
                        .build());
            }

            // 🌮 Promoción 4: Descuento 25% en Empanadas (promoción individual)
            var empanada = articuloManufacturadoRepo.findById(4L).orElse(null);
            if (empanada != null) {
                Promocion descuentoEmpanadas = promocionRepo.save(Promocion.builder()
                        .denominacion("🌮 25% OFF Empanadas")
                        .fechaDesde(fechaInicio)
                        .fechaHasta(fechaFin)
                        .horaDesde(LocalTime.of(14, 0)) // Solo en la tarde
                        .horaHasta(LocalTime.of(18, 0))
                        .descuento(25.0) // 25% de descuento
                        .sucursal(sucursal)
                        .build());

                promocionDetalleRepo.save(PromocionDetalle.builder()
                        .promocion(descuentoEmpanadas)
                        .articulo(empanada)
                        .cantidadRequerida(1)
                        .build());
            }

            System.out.println("✅ Promociones de ejemplo inicializadas correctamente");
        };
    }
}
