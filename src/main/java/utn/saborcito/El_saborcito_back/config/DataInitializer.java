package utn.saborcito.El_saborcito_back.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;

import java.util.Date;

/**
 * 🚀 Inicializador de datos para la aplicación
 * Carga datos de ejemplo cuando la aplicación arranca
 */
@Configuration
public class DataInitializer {

    private static final double PRECIO_BASE_HAMBURGUESA = 5000.0;
    private static final double PRECIO_BASE_PIZZA = 8000.0;
    private static final double PRECIO_BASE_BEBIDA = 2000.0;

    /**
     * 📋 Bean que se ejecuta al iniciar la aplicación para cargar datos de prueba
     */
    @Bean
    public CommandLineRunner initData(
            CategoriaRepository categoriaRepo) {
        return args -> {
            // El código está comentado para evitar duplicados en cada reinicio
            // Descomenta para cargar los datos de prueba
            /*
             * // Crear Categorías
             * Categoria hamburguesas = new Categoria(null, "Hamburguesas",
             * "Diferentes estilos, tipos y tamaños, pensadas para sacarte el hambre.",
             * false);
             * Categoria pizza = new Categoria(null, "Pizza", "Al estilo Italianas.",
             * false);
             * Categoria bebidas = new Categoria(null, "Bebidas", "Las más refrescantes.",
             * false);
             * 
             * // Guardar categorías en la base de datos
             * categoriaRepo.save(hamburguesas);
             * categoriaRepo.save(pizza);
             * categoriaRepo.save(bebidas);
             * 
             * // Crear y guardar Valores
             * Valor valorHamburguesasClasica = new Valor(null, 11000.0,
             * PRECIO_BASE_HAMBURGUESA, new Date());
             * Valor valorHamburguesasConQueso = new Valor(null, 12000.0,
             * PRECIO_BASE_HAMBURGUESA, new Date());
             * Valor valorHamburguesasBBQ = new Valor(null, 12500.0,
             * PRECIO_BASE_HAMBURGUESA, new Date());
             * Valor valorHamburguesasVegetariana = new Valor(null, 10000.0,
             * PRECIO_BASE_HAMBURGUESA, new Date());
             * Valor valorPizzaMargherita = new Valor(null, 12000.0, PRECIO_BASE_PIZZA, new
             * Date());
             * Valor valorPizzaPepperoni = new Valor(null, 14000.0, PRECIO_BASE_PIZZA, new
             * Date());
             * Valor valorPizzaCuatroQuesos = new Valor(null, 15000.0, PRECIO_BASE_PIZZA,
             * new Date());
             * Valor valorPizzaHawaiana = new Valor(null, 13500.0, PRECIO_BASE_PIZZA, new
             * Date());
             * Valor valorBebidasCoca = new Valor(null, 4500.0, PRECIO_BASE_BEBIDA, new
             * Date());
             * Valor valorBebidasAgua = new Valor(null, 4000.0, PRECIO_BASE_BEBIDA, new
             * Date());
             * Valor valorBebidasJugo = new Valor(null, 3500.0, PRECIO_BASE_BEBIDA, new
             * Date());
             * Valor valorBebidasCerveza = new Valor(null, 5000.0, PRECIO_BASE_BEBIDA, new
             * Date());
             * 
             * valorRepo.save(valorHamburguesasClasica);
             * valorRepo.save(valorHamburguesasConQueso);
             * valorRepo.save(valorHamburguesasBBQ);
             * valorRepo.save(valorHamburguesasVegetariana);
             * valorRepo.save(valorPizzaMargherita);
             * valorRepo.save(valorPizzaPepperoni);
             * valorRepo.save(valorPizzaCuatroQuesos);
             * valorRepo.save(valorPizzaHawaiana);
             * valorRepo.save(valorBebidasCoca);
             * valorRepo.save(valorBebidasAgua);
             * valorRepo.save(valorBebidasJugo);
             * valorRepo.save(valorBebidasCerveza);
             * 
             * // Crear y guardar Productos con relación a Valores
             * productoRepo.save(new Producto(null, "Pizza Margherita",
             * "Pizza clásica con tomate, mozzarella y albahaca.", 20, new Date(), false,
             * valorPizzaMargherita, pizza));
             * productoRepo.save(new Producto(null, "Pizza Pepperoni",
             * "Pizza con pepperoni, queso y salsa de tomate.", 15, new Date(), false,
             * valorPizzaPepperoni, pizza));
             * productoRepo.save(new Producto(null, "Pizza Cuatro Quesos",
             * "Pizza con mezcla de cuatro quesos: mozzarella, gorgonzola, parmesano y ricotta."
             * , 10, new Date(), false, valorPizzaCuatroQuesos, pizza));
             * productoRepo.save(new Producto(null, "Pizza Hawaiana",
             * "Pizza con jamón, piña y queso.", 12, new Date(), false, valorPizzaHawaiana,
             * pizza));
             * productoRepo.save(new Producto(null, "Coca Cola",
             * "Bebida gaseosa refrescante.", 20, new Date(), false, valorBebidasCoca,
             * bebidas));
             * productoRepo.save(new Producto(null, "Agua Mineral", "Agua mineral sin gas.",
             * 30, new Date(), false, valorBebidasAgua, bebidas));
             * productoRepo.save(new Producto(null, "Jugo de Naranja",
             * "Jugo de naranja natural.", 25, new Date(), false, valorBebidasJugo,
             * bebidas));
             * productoRepo.save(new Producto(null, "Cerveza Artesanal",
             * "Cerveza artesanal estilo IPA.", 15, new Date(), false, valorBebidasCerveza,
             * bebidas));
             * productoRepo.save(new Producto(null, "Hamburguesa Clásica",
             * "Hamburguesa con carne de res, lechuga, tomate y queso.", 25, new Date(),
             * false, valorHamburguesasClasica, hamburguesas));
             * productoRepo.save(new Producto(null, "Hamburguesa con Queso",
             * "Hamburguesa con carne de res, queso cheddar y cebolla.", 20, new Date(),
             * false, valorHamburguesasConQueso, hamburguesas));
             * productoRepo.save(new Producto(null, "Hamburguesa BBQ",
             * "Hamburguesa con carne de res, queso, cebolla caramelizada y salsa BBQ.", 18,
             * new Date(), false, valorHamburguesasBBQ, hamburguesas));
             * productoRepo.save(new Producto(null, "Hamburguesa Vegetariana",
             * "Hamburguesa con base de garbanzos, lechuga, tomate y guacamole.", 22, new
             * Date(), false, valorHamburguesasVegetariana, hamburguesas));
             */
        };
    }
}