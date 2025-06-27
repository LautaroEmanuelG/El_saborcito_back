package utn.saborcito.El_saborcito_back.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;

/**
 * 🚀 Inicializador básico de datos para UnidadMedida
 * Carga unidades de medida básicas al iniciar la aplicación
 */
@Configuration
public class UnidadMedidaInitializer {

    /**
     * 📋 Bean que carga las unidades de medida básicas
     */
    @Bean
    public CommandLineRunner initUnidadesMedida(UnidadMedidaRepository unidadMedidaRepo) {
        return args -> {
            // Solo inicializar si no hay datos
            if (unidadMedidaRepo.count() == 0) {
                System.out.println("🔄 Inicializando unidades de medida básicas...");

                // UNIDADES DE MEDIDA BÁSICAS
                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Gramos")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Kilogramos")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Litros")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Mililitros")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Unidad")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Porción")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Cucharada")
                        .eliminado(false)
                        .build()
                );

                unidadMedidaRepo.save(
                    UnidadMedida.builder()
                        .denominacion("Cucharadita")
                        .eliminado(false)
                        .build()
                );

                System.out.println("✅ Unidades de medida básicas creadas exitosamente");
                System.out.println("📊 Total de unidades de medida: " + unidadMedidaRepo.count());
            } else {
                System.out.println("ℹ️ Las unidades de medida ya existen en la base de datos");
            }
        };
    }
}
