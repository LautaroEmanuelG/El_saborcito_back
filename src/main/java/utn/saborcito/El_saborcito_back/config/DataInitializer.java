package utn.saborcito.El_saborcito_back.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
import utn.saborcito.El_saborcito_back.repositories.PaisRepository;
import utn.saborcito.El_saborcito_back.repositories.ProvinciaRepository;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;
import utn.saborcito.El_saborcito_back.repositories.EmpresaRepository;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;
import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;
import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;
import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoDetalleRepository;
import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;
import utn.saborcito.El_saborcito_back.repositories.PromocionDetalleRepository;
import utn.saborcito.El_saborcito_back.repositories.ClienteRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;
import utn.saborcito.El_saborcito_back.repositories.FacturaRepository;
import utn.saborcito.El_saborcito_back.repositories.DatosMercadoPagoRepository;
import utn.saborcito.El_saborcito_back.repositories.HorarioAtencionRepository;
import utn.saborcito.El_saborcito_back.repositories.EstadoRepository;
import utn.saborcito.El_saborcito_back.repositories.TipoEnvioRepository;
import utn.saborcito.El_saborcito_back.repositories.FormaPagoRepository;
import utn.saborcito.El_saborcito_back.repositories.EmpleadoRepository;
import utn.saborcito.El_saborcito_back.repositories.HistorialPedidoRepository;
import utn.saborcito.El_saborcito_back.models.Pais;
import utn.saborcito.El_saborcito_back.models.Provincia;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.models.Empresa;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.models.UnidadMedida;
import utn.saborcito.El_saborcito_back.models.Imagen;
import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
import utn.saborcito.El_saborcito_back.models.Promocion;
import utn.saborcito.El_saborcito_back.models.PromocionDetalle;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.DetallePedidoPromocion;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
import utn.saborcito.El_saborcito_back.models.HorarioAtencion;
import utn.saborcito.El_saborcito_back.models.Estado;
import utn.saborcito.El_saborcito_back.models.TipoEnvio;
import utn.saborcito.El_saborcito_back.models.FormaPago;
import utn.saborcito.El_saborcito_back.models.Empleado;
import utn.saborcito.El_saborcito_back.models.HistorialPedido;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.enums.DiaSemana;
import utn.saborcito.El_saborcito_back.enums.CategoriaTipo;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoPromocionRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
* 🚀 Inicializador de datos para la aplicación
* Carga datos de ejemplo cuando la aplicación arranca
*/
@Configuration
public class DataInitializer {

   /**
    * 📋 Bean que se ejecuta al iniciar la aplicación para cargar datos de prueba
    */
   @Bean
   public CommandLineRunner initData(
           PaisRepository paisRepo,
           ProvinciaRepository provinciaRepo,
           LocalidadRepository localidadRepo,
           UsuarioRepository usuarioRepo,
           EmpresaRepository empresaRepo,
           DomicilioRepository domicilioRepo,
           SucursalRepository sucursalRepo,
           UnidadMedidaRepository unidadMedidaRepo,
           CategoriaRepository categoriaRepo,
           ImagenRepository imagenRepo,
           ArticuloInsumoRepository articuloInsumoRepo,
           ArticuloManufacturadoRepository articuloManufacturadoRepo, ArticuloManufacturadoDetalleRepository amdRepo,
           PromocionRepository promocionRepo,
           PromocionDetalleRepository promocionDetalleRepo,
           ClienteRepository clienteRepo,
           PedidoRepository pedidoRepo,
           DetallePedidoRepository detallePedidoRepo,
           FacturaRepository facturaRepo,
           DatosMercadoPagoRepository datosMPRepo,
           HorarioAtencionRepository horarioRepo,
           EstadoRepository estadoRepo,
           TipoEnvioRepository tipoEnvioRepo,
           FormaPagoRepository formaPagoRepo,
           EmpleadoRepository empleadoRepo,
           HistorialPedidoRepository historialPedidoRepo,
           DetallePedidoPromocionRepository detallePedidoPromocionRepo) {
       return args -> {
           // 1. Geografía y usuarios
           Pais pais = paisRepo.save(Pais.builder().nombre("Argentina").build());
           Provincia provincia = provinciaRepo
                   .save(Provincia.builder().nombre("Mendoza").pais(pais).build());
           Localidad localidad = localidadRepo
                   .save(Localidad.builder().nombre("Ciudad de Mendoza").provincia(provincia)
                           .build());


           // 2. Empresa, sucursal y domicilio
           Empresa empresa = empresaRepo.save(Empresa.builder().nombre("El Saborcito SA")
                   .razonSocial("El Saborcito SRL").cuil("20-12345678-9").build());

           // Primero creamos el domicilio sin persistirlo
           Domicilio domicilio = Domicilio.builder()
                   .calle("San Martín")
                   .numero(123)
                   .cp("5500")
                   .localidad(localidad)
                   .build();

           // Luego creamos la sucursal con el domicilio (se persistirá en cascada)
           Sucursal sucursal = sucursalRepo
                   .save(Sucursal.builder().nombre("Sucursal Centro").empresa(empresa)
                           .domicilio(domicilio).build());

           // Agregamos horarios de atención para la sucursal
           for (DiaSemana dia : DiaSemana.values()) {
               // Horario de 10 a 22 de lunes a jueves, 10 a 0 viernes y sábado, cerrado
               // domingo
               LocalTime apertura = LocalTime.of(10, 0);
               LocalTime cierre;

               if (dia == DiaSemana.VIERNES || dia == DiaSemana.SABADO) {
                   cierre = LocalTime.of(0, 0); // Hasta medianoche
               } else if (dia == DiaSemana.DOMINGO) {
                   cierre = null; // Cerrado los domingos
                   apertura = null;
               } else {
                   cierre = LocalTime.of(22, 0); // De lunes a jueves
               }

               if (apertura != null && cierre != null) {
                   horarioRepo.save(HorarioAtencion.builder()
                           .diaSemana(dia)
                           .apertura(apertura)
                           .cierre(cierre)
                           .sucursal(sucursal).build());
               }
           }

           // 3. Inicializar Estados de Pedidos
           // Comprobar si ya existen los estados antes de crearlos
           Estado estadoPendiente = estadoRepo.findByNombre("PENDIENTE")
                   .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("PENDIENTE").build()));
           Estado estadoConfirmado = estadoRepo.findByNombre("CONFIRMADO")
                   .orElseGet(() -> estadoRepo
                           .save(Estado.builder().nombre("CONFIRMADO").build()));
           Estado estadoEnPreparacion = estadoRepo.findByNombre("EN_PREPARACION")
                   .orElseGet(() -> estadoRepo
                           .save(Estado.builder().nombre("EN_PREPARACION").build()));
           Estado estadoListo = estadoRepo.findByNombre("LISTO")
                   .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("LISTO").build()));
           Estado estadoEnDelivery = estadoRepo.findByNombre("EN_DELIVERY")
                   .orElseGet(() -> estadoRepo
                           .save(Estado.builder().nombre("EN_DELIVERY").build()));
           Estado estadoEntregado = estadoRepo.findByNombre("ENTREGADO")
                   .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("ENTREGADO").build()));
           estadoRepo.findByNombre("CANCELADO")
                   .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("CANCELADO").build()));

           // Inicializar Tipos de Envío
           TipoEnvio tipoDelivery = tipoEnvioRepo.findByNombre("DELIVERY")
                   .orElseGet(() -> tipoEnvioRepo
                           .save(TipoEnvio.builder().nombre("DELIVERY").build()));
           TipoEnvio tipoTakeAway = tipoEnvioRepo.findByNombre("TAKE_AWAY")
                   .orElseGet(() -> tipoEnvioRepo
                           .save(TipoEnvio.builder().nombre("TAKE_AWAY").build()));
           TipoEnvio tipoLocal = tipoEnvioRepo.findByNombre("EN_LOCAL")
                   .orElseGet(() -> tipoEnvioRepo
                           .save(TipoEnvio.builder().nombre("EN_LOCAL").build()));

           // Inicializar Formas de Pago
           FormaPago formaPagoEfectivo = formaPagoRepo.findByNombre("EFECTIVO")
                   .orElseGet(() -> formaPagoRepo
                           .save(FormaPago.builder().nombre("EFECTIVO").build()));
           FormaPago formaPagoMercadoPago = formaPagoRepo.findByNombre("MERCADO_PAGO")
                   .orElseGet(() -> formaPagoRepo
                           .save(FormaPago.builder().nombre("MERCADO_PAGO").build()));
           // 4. Catálogo de productos - Creamos las categorías principales y subcategorías

           // -- Categorías principales
           Categoria categoriaSandwiches = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Sandwiches")
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.MANUFACTURADOS)
                           .build()
           );
           Categoria categoriaPizzas = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Pizzas")
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.MANUFACTURADOS)
                           .build()
           );
           Categoria categoriaBebidas = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Bebidas")
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.INSUMOS)
                           .build()
           );
           Categoria categoriaInsumos = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Insumos")
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.INSUMOS)
                           .build()
           );

           // -- Subcategorías de Sandwiches
           Categoria categoriaHamburguesas = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Hamburguesas")
                           .tipoCategoria(categoriaSandwiches)
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.MANUFACTURADOS)
                           .build()
           );
           Categoria categoriaLomos = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Lomos")
                           .tipoCategoria(categoriaSandwiches)
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.MANUFACTURADOS)
                           .build()
           );

           // -- Subcategorías de Bebidas
           Categoria categoriaGaseosas = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Gaseosas")
                           .tipoCategoria(categoriaBebidas)
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.INSUMOS)
                           .build()
           );
           Categoria categoriaAguas = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Aguas")
                           .tipoCategoria(categoriaBebidas)
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.INSUMOS)
                           .build()
           );
           Categoria categoriaTragos = categoriaRepo.save(
                   Categoria.builder()
                           .denominacion("Tragos")
                           .tipoCategoria(categoriaBebidas)
                           .sucursal(sucursal)
                           .tipo(CategoriaTipo.INSUMOS)
                           .build()
           );



           // 5. Imágenes para cada tipo de producto
           Imagen imagenHamburguesa = imagenRepo
                   .save(Imagen.builder().url("/img/productos/hamburguesas/hamburguesa.png")
                           .build());
           Imagen imagenLomo = imagenRepo
                   .save(Imagen.builder().url("/img/productos/lomos/lomo.png")
                           .build());
           Imagen imagenPizza = imagenRepo
                   .save(Imagen.builder().url("/img/productos/pizza/pizza.png")
                           .build());
           Imagen imagenGaseosa = imagenRepo
                   .save(Imagen.builder().url("/img/productos/bebidas/gaseosa.png")
                           .build());
           Imagen imagenAgua = imagenRepo
                   .save(Imagen.builder().url("/img/productos/bebidas/agua.png")
                           .build());
           Imagen imagenTrago = imagenRepo
                   .save(Imagen.builder().url("/img/productos/bebidas/trago.png")
                           .build());
           Imagen imagenDefault = imagenRepo
                   .save(Imagen.builder().url("/img/default.png")
                           .build());

           // Nuevas imágenes para pizzas
           Imagen imagenPizzaMuzarella = imagenRepo
                   .save(Imagen.builder().url("/img/productos/pizza/pizzamuzarella.png")
                           .build());
           Imagen imagenPizzaCuatroQuesos = imagenRepo
                   .save(Imagen.builder().url("/img/productos/pizza/pizzacuatroquesos.png")
                           .build());
           Imagen imagenPizzaHawaiana = imagenRepo
                   .save(Imagen.builder().url("/img/productos/pizza/pizzahawaiana.png")
                           .build());
           Imagen imagenPizzaMargherita = imagenRepo
                   .save(Imagen.builder().url("/img/productos/pizza/pizzamargherita.png")
                           .build());
           Imagen imagenPizzaPepperoni = imagenRepo
                   .save(Imagen.builder().url("/img/productos/pizza/pizzapepperoni.png")
                           .build());

           // Nuevas imágenes para hamburguesas
           Imagen imagenHamburguesaBbq = imagenRepo
                   .save(Imagen.builder()
                           .url("/img/productos/hamburguesas/hamburguesabbq.png")
                           .build());
           Imagen imagenHamburguesaClasicaNueva = imagenRepo
                   .save(Imagen.builder().url(
                                   "/img/productos/hamburguesas/hamburguesaclásica.png")
                           .build());
           Imagen imagenHamburguesaConQueso = imagenRepo
                   .save(Imagen.builder().url(
                                   "/img/productos/hamburguesas/hamburguesaconqueso.png")
                           .build());
           Imagen imagenHamburguesaVegetariana = imagenRepo
                   .save(Imagen.builder().url(
                                   "/img/productos/hamburguesas/hamburguesavegetariana.png")
                           .build());

           // Nuevas imágenes para bebidas
           Imagen imagenAguaMineralNueva = imagenRepo
                   .save(Imagen.builder().url("/img/productos/bebidas/aguamineral.jpg")
                           .build());
           Imagen imagenCervezaArtesanal = imagenRepo
                   .save(Imagen.builder()
                           .url("/img/productos/bebidas/cervezaartesanal.png")
                           .build());
           Imagen imagenCocaColaNueva = imagenRepo
                   .save(Imagen.builder().url("/img/productos/bebidas/cocacola.jpg")
                           .build());
           Imagen imagenJugoDeNaranja = imagenRepo
                   .save(Imagen.builder().url("/img/productos/bebidas/jugodenaranja.png")
                           .build());

           // 6. Unidades de medida
           UnidadMedida unidadGramos = unidadMedidaRepo
                   .save(UnidadMedida.builder().denominacion("Gramos").build());
           UnidadMedida unidadLitros = unidadMedidaRepo
                   .save(UnidadMedida.builder().denominacion("Litros").build());
           UnidadMedida unidadUnidad = unidadMedidaRepo
                   .save(UnidadMedida.builder().denominacion("Unidad").build());


           // --------------------------------------------------------------------------------------------------------------------------------
           // CREACIÓN DE ARTÍCULOS INSUMO
           // --------------------------------------------------------------------------------------------------------------------------------

           // ARTICULO INSUMO 1
           ArticuloInsumo insumoHarina = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Harina 000")
                   .precioVenta(1000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(700.0)
                   .stockActual(10000)
                   .stockMinimo(2000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 2
           ArticuloInsumo insumoQueso = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Queso Mozzarella")
                   .precioVenta(2500.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(2000.0)
                   .stockActual(8000)
                   .stockMinimo(5000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 3
           ArticuloInsumo insumoCarne = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Carne Molida Premium")
                   .precioVenta(3500.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(3000.0)
                   .stockActual(7000)
                   .stockMinimo(4000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 4
           ArticuloInsumo insumoPan = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Pan de Hamburguesa")
                   .precioVenta(1500.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadUnidad)
                   .precioCompra(1200.0)
                   .stockActual(200)
                   .stockMinimo(100)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 5
           ArticuloInsumo insumoLechuga = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Lechuga Fresca")
                   .precioVenta(800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(600.0)
                   .stockActual(3000)
                   .stockMinimo(2000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 6
           ArticuloInsumo insumoTomate = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Tomate")
                   .precioVenta(900.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(700.0)
                   .stockActual(5000)
                   .stockMinimo(3000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 7
           ArticuloInsumo insumoCebolla = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Cebolla")
                   .precioVenta(600.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(400.0)
                   .stockActual(4000)
                   .stockMinimo(2500)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 8
           ArticuloInsumo insumoPanLomo = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Pan de Lomo")
                   .precioVenta(1700.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadUnidad)
                   .precioCompra(1400.0)
                   .stockActual(150)
                   .stockMinimo(80)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 9
           ArticuloInsumo insumoCarneLomo = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Bife de Lomo")
                   .precioVenta(5000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(4500.0)
                   .stockActual(5000)
                   .stockMinimo(3000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 10
           ArticuloInsumo insumoSalsaTomate = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Salsa de Tomate")
                   .precioVenta(1200.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(900.0)
                   .stockActual(6000)
                   .stockMinimo(4000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 11
           ArticuloInsumo insumoQuesoProvolone = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Queso Provolone")
                   .precioVenta(2800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(2200.0)
                   .stockActual(2000)
                   .stockMinimo(1000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 12
           ArticuloInsumo insumoQuesoRoquefort = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Queso Roquefort")
                   .precioVenta(3000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(2500.0)
                   .stockActual(1500)
                   .stockMinimo(800)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 13
           ArticuloInsumo insumoQuesoParmesano = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Queso Parmesano")
                   .precioVenta(3200.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(2600.0)
                   .stockActual(2200)
                   .stockMinimo(1200)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 14
           ArticuloInsumo insumoJamon = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Jamón Cocido")
                   .precioVenta(2000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1500.0)
                   .stockActual(5000)
                   .stockMinimo(3000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 15
           ArticuloInsumo insumoAnana = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Ananá en Rodajas")
                   .precioVenta(1500.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1000.0)
                   .stockActual(1000)
                   .stockMinimo(500)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 16
           ArticuloInsumo insumoAlbahaca = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Albahaca Fresca")
                   .precioVenta(500.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(300.0)
                   .stockActual(2000)
                   .stockMinimo(1000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 17
           ArticuloInsumo insumoPepperoni = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Pepperoni")
                   .precioVenta(2600.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(2000.0)
                   .stockActual(2500)
                   .stockMinimo(1500)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 18
           ArticuloInsumo insumoSalsaBBQ = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Salsa BBQ")
                   .precioVenta(1300.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(900.0)
                   .stockActual(1500)
                   .stockMinimo(1000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 19
           ArticuloInsumo insumoPanceta = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Panceta Ahumada")
                   .precioVenta(2200.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1700.0)
                   .stockActual(3000)
                   .stockMinimo(2000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 20
           ArticuloInsumo insumoQuesoCheddar = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Queso Cheddar")
                   .precioVenta(2700.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(2100.0)
                   .stockActual(2500)
                   .stockMinimo(1500)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 21
           ArticuloInsumo insumoMedallonVegetariano = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Medallón Vegetariano")
                   .precioVenta(1800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadUnidad)
                   .precioCompra(1300.0)
                   .stockActual(200)
                   .stockMinimo(100)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());


           // ARTICULO INSUMO 27 - Aceite de Oliva
           ArticuloInsumo insumoAceiteOliva = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Aceite de Oliva Extra Virgen")
                   .precioVenta(2800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadLitros)
                   .precioCompra(2200.0)
                   .stockActual(500)
                   .stockMinimo(300)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 28 - Oregano
           ArticuloInsumo insumoOregano = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Orégano Seco")
                   .precioVenta(800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(500.0)
                   .stockActual(1000)
                   .stockMinimo(500)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 29 - Sal
           ArticuloInsumo insumoSal = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Sal Fina")
                   .precioVenta(300.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(200.0)
                   .stockActual(5000)
                   .stockMinimo(3000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 30 - Pimienta
           ArticuloInsumo insumoPimienta = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Pimienta Negra Molida")
                   .precioVenta(1200.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(900.0)
                   .stockActual(800)
                   .stockMinimo(400)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 31 - Ajo
           ArticuloInsumo insumoAjo = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Ajo Fresco")
                   .precioVenta(1500.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1100.0)
                   .stockActual(2000)
                   .stockMinimo(1000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 32 - Levadura
           ArticuloInsumo insumoLevadura = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Levadura Fresca")
                   .precioVenta(600.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(400.0)
                   .stockActual(1500)
                   .stockMinimo(800)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 33 - Azúcar
           ArticuloInsumo insumoAzucar = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Azúcar Blanca")
                   .precioVenta(800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(600.0)
                   .stockActual(3000)
                   .stockMinimo(2000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 34 - Huevos
           ArticuloInsumo insumoHuevos = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Huevos Frescos")
                   .precioVenta(2000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadUnidad)
                   .precioCompra(1500.0)
                   .stockActual(500)
                   .stockMinimo(300)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 35 - Manteca
           ArticuloInsumo insumoManteca = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Manteca")
                   .precioVenta(1800.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1400.0)
                   .stockActual(2000)
                   .stockMinimo(1200)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 36 - Mayonesa
           ArticuloInsumo insumoMayonesa = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Mayonesa")
                   .precioVenta(1000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(700.0)
                   .stockActual(1500)
                   .stockMinimo(1000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 37 - Ketchup
           ArticuloInsumo insumoKetchup = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Ketchup")
                   .precioVenta(1100.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(800.0)
                   .stockActual(1200)
                   .stockMinimo(800)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 38 - Mostaza
           ArticuloInsumo insumoMostaza = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Mostaza")
                   .precioVenta(900.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(600.0)
                   .stockActual(1000)
                   .stockMinimo(600)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 39 - Papas
           ArticuloInsumo insumoPapas = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Papas para Fritar")
                   .precioVenta(1200.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(800.0)
                   .stockActual(8000)
                   .stockMinimo(5000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 40 - Morrones
           ArticuloInsumo insumoMorrones = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Morrones Rojos")
                   .precioVenta(1400.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1000.0)
                   .stockActual(3000)
                   .stockMinimo(2000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 41 - Vinagre
           ArticuloInsumo insumoVinagre = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Vinagre de Alcohol")
                   .precioVenta(700.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadLitros)
                   .precioCompra(500.0)
                   .stockActual(800)
                   .stockMinimo(400)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 42 - Perejil
           ArticuloInsumo insumoPerejil = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Perejil Fresco")
                   .precioVenta(600.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(400.0)
                   .stockActual(1200)
                   .stockMinimo(600)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 43 - Aceitunas
           ArticuloInsumo insumoAceitunas = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Aceitunas Verdes")
                   .precioVenta(1600.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1200.0)
                   .stockActual(2000)
                   .stockMinimo(1000)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 44 - Hongos
           ArticuloInsumo insumoHongos = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Hongos Frescos")
                   .precioVenta(2200.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadGramos)
                   .precioCompra(1800.0)
                   .stockActual(1500)
                   .stockMinimo(800)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 45 - Leche
           ArticuloInsumo insumoLeche = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Leche Entera")
                   .precioVenta(1000.0)
                   .categoria(categoriaInsumos)
                   .unidadMedida(unidadLitros)
                   .precioCompra(700.0)
                   .stockActual(1000)
                   .stockMinimo(500)
                   .esParaElaborar(true)
                   .imagen(imagenDefault)
                   .build());
           // ARTICULO INSUMO 22
           ArticuloInsumo insumoCocaCola = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Coca Cola 500ml")
                   .precioVenta(1800.0)
                   .categoria(categoriaGaseosas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(1300.0)
                   .stockActual(300)
                   .stockMinimo(200)
                   .esParaElaborar(false)
                   .imagen(imagenCocaColaNueva)
                   .build());

           // ARTICULO INSUMO 23
           ArticuloInsumo insumoAguaMineral = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Agua Mineral 500ml")
                   .precioVenta(1200.0)
                   .categoria(categoriaAguas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(800.0)
                   .stockActual(250)
                   .stockMinimo(150)
                   .esParaElaborar(false)
                   .imagen(imagenAguaMineralNueva)
                   .build());

           //INSUMOS NO PARA ELABORAR (BEBIDAS)

           // ARTICULO INSUMO 24
           ArticuloInsumo insumoFernet = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Fernet 750ml")
                   .precioVenta(6000.0)
                   .categoria(categoriaTragos)
                   .unidadMedida(unidadLitros)
                   .precioCompra(4500.0)
                   .stockActual(100)
                   .stockMinimo(50)
                   .esParaElaborar(false)
                   .imagen(imagenTrago)
                   .build());

           // ARTICULO INSUMO 25
           ArticuloInsumo insumoCervezaArtesanal = articuloInsumoRepo
                   .save(ArticuloInsumo.builder()
                           .denominacion("Cerveza Artesanal")
                           .precioVenta(2500.0)
                           .categoria(categoriaTragos) // O una nueva categoría Cervezas si se prefiere
                           .unidadMedida(unidadLitros)
                           .precioCompra(1800.0)
                           .stockActual(150)
                           .stockMinimo(100)
                           .esParaElaborar(false)
                           .imagen(imagenCervezaArtesanal)
                           .build());

           // ARTICULO INSUMO 26
           ArticuloInsumo insumoJugoDeNaranja = articuloInsumoRepo
                   .save(ArticuloInsumo.builder()
                           .denominacion("Jugo de Naranja")
                           .precioVenta(1500.0)
                           .categoria(categoriaBebidas)
                           .unidadMedida(unidadLitros)
                           .precioCompra(1000.0)
                           .stockActual(200)
                           .stockMinimo(120)
                           .esParaElaborar(false)
                           .imagen(imagenJugoDeNaranja)
                           .build());

           // ARTICULO INSUMO 46 - Sprite
           ArticuloInsumo insumoSprite = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Sprite 500ml")
                   .precioVenta(1700.0)
                   .categoria(categoriaGaseosas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(1200.0)
                   .stockActual(280)
                   .stockMinimo(180)
                   .esParaElaborar(false)
                   .imagen(imagenGaseosa)
                   .build());

           // ARTICULO INSUMO 47 - Fanta
           ArticuloInsumo insumoFanta = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Fanta 500ml")
                   .precioVenta(1750.0)
                   .categoria(categoriaGaseosas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(1250.0)
                   .stockActual(260)
                   .stockMinimo(160)
                   .esParaElaborar(false)
                   .imagen(imagenGaseosa)
                   .build());

           // ARTICULO INSUMO 48 - Agua con Gas
           ArticuloInsumo insumoAguaConGas = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Agua con Gas 500ml")
                   .precioVenta(1300.0)
                   .categoria(categoriaAguas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(900.0)
                   .stockActual(200)
                   .stockMinimo(120)
                   .esParaElaborar(false)
                   .imagen(imagenAgua)
                   .build());

           // ARTICULO INSUMO 49 - Agua Saborizada
           ArticuloInsumo insumoAguaSaborizada = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Agua Saborizada Limón 500ml")
                   .precioVenta(1400.0)
                   .categoria(categoriaAguas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(1000.0)
                   .stockActual(180)
                   .stockMinimo(100)
                   .esParaElaborar(false)
                   .imagen(imagenAgua)
                   .build());

           // ARTICULO INSUMO 50 - Cerveza Quilmes
           ArticuloInsumo insumoCervezaQuilmes = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Cerveza Quilmes 473ml")
                   .precioVenta(2000.0)
                   .categoria(categoriaTragos)
                   .unidadMedida(unidadLitros)
                   .precioCompra(1400.0)
                   .stockActual(120)
                   .stockMinimo(80)
                   .esParaElaborar(false)
                   .imagen(imagenCervezaArtesanal)
                   .build());

           // ARTICULO INSUMO 51 - Vino Tinto
           ArticuloInsumo insumoVinoTinto = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Vino Tinto 750ml")
                   .precioVenta(3500.0)
                   .categoria(categoriaTragos)
                   .unidadMedida(unidadLitros)
                   .precioCompra(2500.0)
                   .stockActual(80)
                   .stockMinimo(40)
                   .esParaElaborar(false)
                   .imagen(imagenTrago)
                   .build());

           // ARTICULO INSUMO 52 - Jugo de Manzana
           ArticuloInsumo insumoJugoManzana = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Jugo de Manzana 500ml")
                   .precioVenta(1450.0)
                   .categoria(categoriaBebidas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(950.0)
                   .stockActual(150)
                   .stockMinimo(100)
                   .esParaElaborar(false)
                   .imagen(imagenJugoDeNaranja)
                   .build());

           // ARTICULO INSUMO 53 - Red Bull
           ArticuloInsumo insumoRedBull = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Red Bull 250ml")
                   .precioVenta(3000.0)
                   .categoria(categoriaBebidas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(2200.0)
                   .stockActual(100)
                   .stockMinimo(60)
                   .esParaElaborar(false)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 54 - Gatorade
           ArticuloInsumo insumoGatorade = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Gatorade 500ml")
                   .precioVenta(2200.0)
                   .categoria(categoriaBebidas)
                   .unidadMedida(unidadLitros)
                   .precioCompra(1600.0)
                   .stockActual(120)
                   .stockMinimo(80)
                   .esParaElaborar(false)
                   .imagen(imagenDefault)
                   .build());

           // ARTICULO INSUMO 55 - Café Instantáneo
           ArticuloInsumo insumoCafeInstantaneo = articuloInsumoRepo.save(ArticuloInsumo.builder()
                   .denominacion("Café Instantáneo")
                   .precioVenta(1800.0)
                   .categoria(categoriaBebidas)
                   .unidadMedida(unidadUnidad)
                   .precioCompra(1200.0)
                   .stockActual(200)
                   .stockMinimo(150)
                   .esParaElaborar(false)
                   .imagen(imagenDefault)
                   .build());

           // --------------------------------------------------------------------------------------------------------------------------------
           // CREACIÓN DE ARTÍCULOS MANUFACTURADOS
           // --------------------------------------------------------------------------------------------------------------------------------

           // ARTICULO MANUFACTURADO 1
           ArticuloManufacturado pizzaMozzarella = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Pizza Mozzarella")
                           .descripcion("Pizza con queso mozzarella y salsa de tomate")
                           .tiempoEstimadoMinutos(20)
                           .precioVenta(4500.0)
                           .categoria(categoriaPizzas)
                           .imagen(imagenPizzaMuzarella)
                           .build());

           // Detalles de articulo manufacturado 1
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaMozzarella)
                   .articuloInsumo(insumoHarina)
                   .cantidad(200)
                   .build());

           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaMozzarella)
                   .articuloInsumo(insumoQueso)
                   .cantidad(150)
                   .build());

           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaMozzarella)
                   .articuloInsumo(insumoSalsaTomate)
                   .cantidad(100)
                   .build());


           // ARTICULO MANUFACTURADO 2
           ArticuloManufacturado pizzaCuatroQuesos = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder().denominacion("Pizza Cuatro Quesos")
                           .descripcion("Deliciosa pizza con mozzarella, provolone, roquefort y parmesano.")
                           .precioVenta(5200.0).tiempoEstimadoMinutos(22)
                           .categoria(categoriaPizzas).imagen(imagenPizzaCuatroQuesos)
                           .build());

           // Detalles de articulo manufacturado 2
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
                   .articuloInsumo(insumoHarina).cantidad(200).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
                   .articuloInsumo(insumoSalsaTomate).cantidad(80).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
                   .articuloInsumo(insumoQueso).cantidad(100).build()); // Mozzarella
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
                   .articuloInsumo(insumoQuesoProvolone).cantidad(50).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
                   .articuloInsumo(insumoQuesoRoquefort).cantidad(50).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
                   .articuloInsumo(insumoQuesoParmesano).cantidad(30).build());

           // ARTICULO MANUFACTURADO 3
           ArticuloManufacturado pizzaHawaiana = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder().denominacion("Pizza Hawaiana")
                           .descripcion("Exótica pizza con jamón, ananá y mozzarella.")
                           .precioVenta(5000.0).tiempoEstimadoMinutos(20)
                           .categoria(categoriaPizzas).imagen(imagenPizzaHawaiana)
                           .build());

           // Detalles de articulo manufacturado 3
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
                   .articuloInsumo(insumoHarina).cantidad(200).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
                   .articuloInsumo(insumoSalsaTomate).cantidad(100).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
                   .articuloInsumo(insumoQueso).cantidad(150).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
                   .articuloInsumo(insumoJamon).cantidad(100).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
                   .articuloInsumo(insumoAnana).cantidad(80).build());

           // ARTICULO MANUFACTURADO 4
           ArticuloManufacturado pizzaMargherita = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder().denominacion("Pizza Margherita")
                           .descripcion("Clásica pizza con tomate, mozzarella fresca y albahaca.")
                           .precioVenta(4800.0).tiempoEstimadoMinutos(18)
                           .categoria(categoriaPizzas).imagen(imagenPizzaMargherita)
                           .build());

           // Detalles de articulo manufacturado 4
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
                   .articuloInsumo(insumoHarina).cantidad(200).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
                   .articuloInsumo(insumoSalsaTomate).cantidad(120).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
                   .articuloInsumo(insumoQueso).cantidad(180).build()); // Mozzarella fresca
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
                   .articuloInsumo(insumoAlbahaca).cantidad(20).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
                   .articuloInsumo(insumoTomate).cantidad(100).build()); // Rodajas de tomate
           // fresco

           // ARTICULO MANUFACTURADO 5
           ArticuloManufacturado pizzaPepperoni = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder().denominacion("Pizza Pepperoni")
                           .descripcion("Sabrosa pizza con abundante pepperoni y mozzarella.")
                           .precioVenta(5300.0).tiempoEstimadoMinutos(22)
                           .categoria(categoriaPizzas).imagen(imagenPizzaPepperoni)
                           .build());

           // Detalles de articulo manufacturado 5
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
                   .articuloInsumo(insumoHarina).cantidad(200).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
                   .articuloInsumo(insumoSalsaTomate).cantidad(100).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
                   .articuloInsumo(insumoQueso).cantidad(150).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
                   .articuloInsumo(insumoPepperoni).cantidad(120).build());

           // ARTICULO MANUFACTURADO 6
           ArticuloManufacturado hamburguesaClasica = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Hamburguesa Clásica")
                           .descripcion("Hamburguesa con lechuga, tomate y cebolla")
                           .tiempoEstimadoMinutos(15)
                           .precioVenta(3800.0)
                           .categoria(categoriaHamburguesas)
                           .imagen(imagenHamburguesaClasicaNueva) // Nueva imagen
                           .build());

           // Detalles de articulo manufacturado 6
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaClasica)
                   .articuloInsumo(insumoCarne)
                   .cantidad(150)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaClasica)
                   .articuloInsumo(insumoPan)
                   .cantidad(1)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaClasica)
                   .articuloInsumo(insumoLechuga)
                   .cantidad(30)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaClasica)
                   .articuloInsumo(insumoTomate)
                   .cantidad(50)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaClasica)
                   .articuloInsumo(insumoCebolla)
                   .cantidad(30)
                   .build());

           // ARTICULO MANUFACTURADO 7
           ArticuloManufacturado hamburguesaBbq = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Hamburguesa BBQ")
                           .descripcion("Hamburguesa con salsa BBQ, panceta y cebolla caramelizada.")
                           .precioVenta(4200.0).tiempoEstimadoMinutos(18)
                           .categoria(categoriaHamburguesas).imagen(imagenHamburguesaBbq)
                           .build());

           // Detalles de articulo manufacturado 7
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
                   .articuloInsumo(insumoCarne).cantidad(180).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
                   .articuloInsumo(insumoPan).cantidad(1).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
                   .articuloInsumo(insumoQuesoCheddar).cantidad(40).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
                   .articuloInsumo(insumoPanceta).cantidad(50).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
                   .articuloInsumo(insumoSalsaBBQ).cantidad(30).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
                   .articuloInsumo(insumoCebolla).cantidad(30).build());

           // ARTICULO MANUFACTURADO 8
           ArticuloManufacturado hamburguesaConQueso = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder().denominacion("Hamburguesa con Queso")
                           .descripcion("Jugosa hamburguesa con doble queso cheddar.")
                           .precioVenta(4000.0).tiempoEstimadoMinutos(16)
                           .categoria(categoriaHamburguesas)
                           .imagen(imagenHamburguesaConQueso).build());

           // Detalles de articulo manufacturado 8
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
                   .articuloInsumo(insumoCarne).cantidad(150).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
                   .articuloInsumo(insumoPan).cantidad(1).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
                   .articuloInsumo(insumoQuesoCheddar).cantidad(80).build()); // Doble cheddar
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
                   .articuloInsumo(insumoLechuga).cantidad(20).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
                   .articuloInsumo(insumoTomate).cantidad(30).build());

           // ARTICULO MANUFACTURADO 9
           ArticuloManufacturado hamburguesaVegetariana = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder().denominacion("Hamburguesa Vegetariana")
                           .descripcion("Deliciosa hamburguesa a base de plantas con vegetales frescos.")
                           .precioVenta(3900.0).tiempoEstimadoMinutos(15)
                           .categoria(categoriaHamburguesas)
                           .imagen(imagenHamburguesaVegetariana).build());

           // Detalles de articulo manufacturado 9
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaVegetariana)
                   .articuloInsumo(insumoMedallonVegetariano).cantidad(1).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoPan)
                   .cantidad(1).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoLechuga)
                   .cantidad(40).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoTomate)
                   .cantidad(60).build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoCebolla)
                   .cantidad(30).build());

           // ARTICULO MANUFACTURADO 10
           ArticuloManufacturado lomoCompleto = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Lomo Completo")
                           .descripcion("Lomo con jamón, queso, lechuga y tomate")
                           .tiempoEstimadoMinutos(20)
                           .precioVenta(5500.0)
                           .categoria(categoriaLomos)
                           .imagen(imagenLomo)
                           .build());

           // Detalles de articulo manufacturado 10
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoCompleto)
                   .articuloInsumo(insumoCarneLomo)
                   .cantidad(200)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoCompleto)
                   .articuloInsumo(insumoPanLomo)
                   .cantidad(1)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoCompleto)
                   .articuloInsumo(insumoLechuga)
                   .cantidad(30)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoCompleto)
                   .articuloInsumo(insumoTomate)
                   .cantidad(50)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoCompleto)
                   .articuloInsumo(insumoQueso)
                   .cantidad(50)
                   .build());

           // ARTICULO MANUFACTURADO 11 - Lomo Especial
           ArticuloManufacturado lomoEspecial = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Lomo Especial")
                           .descripcion("Lomo con jamón, queso, huevo frito, lechuga y tomate")
                           .tiempoEstimadoMinutos(25)
                           .precioVenta(6200.0)
                           .categoria(categoriaLomos)
                           .imagen(imagenLomo)
                           .build());

           // Detalles de articulo manufacturado 11
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoCarneLomo)
                   .cantidad(220)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoPanLomo)
                   .cantidad(1)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoJamon)
                   .cantidad(80)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoQueso)
                   .cantidad(60)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoHuevos)
                   .cantidad(1)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoLechuga)
                   .cantidad(40)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoEspecial)
                   .articuloInsumo(insumoTomate)
                   .cantidad(60)
                   .build());

           // ARTICULO MANUFACTURADO 12 - Hamburguesa Doble
           ArticuloManufacturado hamburguesaDoble = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Hamburguesa Doble")
                           .descripcion("Hamburguesa con doble carne, queso cheddar, panceta y vegetales")
                           .tiempoEstimadoMinutos(20)
                           .precioVenta(5200.0)
                           .categoria(categoriaHamburguesas)
                           .imagen(imagenHamburguesa)
                           .build());

           // Detalles de articulo manufacturado 12
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaDoble)
                   .articuloInsumo(insumoCarne)
                   .cantidad(300) // Doble carne
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaDoble)
                   .articuloInsumo(insumoPan)
                   .cantidad(1)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaDoble)
                   .articuloInsumo(insumoQuesoCheddar)
                   .cantidad(100)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaDoble)
                   .articuloInsumo(insumoPanceta)
                   .cantidad(60)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaDoble)
                   .articuloInsumo(insumoLechuga)
                   .cantidad(40)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(hamburguesaDoble)
                   .articuloInsumo(insumoTomate)
                   .cantidad(50)
                   .build());

           // ARTICULO MANUFACTURADO 13 - Pizza Napolitana
           ArticuloManufacturado pizzaNapolitana = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Pizza Napolitana")
                           .descripcion("Pizza con mozzarella, tomate, ajo y aceitunas")
                           .tiempoEstimadoMinutos(22)
                           .precioVenta(5100.0)
                           .categoria(categoriaPizzas)
                           .imagen(imagenPizza)
                           .build());

           // Detalles de articulo manufacturado 13
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaNapolitana)
                   .articuloInsumo(insumoHarina)
                   .cantidad(200)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaNapolitana)
                   .articuloInsumo(insumoSalsaTomate)
                   .cantidad(120)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaNapolitana)
                   .articuloInsumo(insumoQueso)
                   .cantidad(180)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaNapolitana)
                   .articuloInsumo(insumoTomate)
                   .cantidad(100)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaNapolitana)
                   .articuloInsumo(insumoAjo)
                   .cantidad(15)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaNapolitana)
                   .articuloInsumo(insumoAceitunas)
                   .cantidad(50)
                   .build());

           // ARTICULO MANUFACTURADO 14 - Lomo Vegetariano
           ArticuloManufacturado lomoVegetariano = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Lomo Vegetariano")
                           .descripcion("Lomo vegetariano con medallón de plantas, queso y vegetales frescos")
                           .tiempoEstimadoMinutos(18)
                           .precioVenta(4800.0)
                           .categoria(categoriaLomos)
                           .imagen(imagenLomo)
                           .build());

           // Detalles de articulo manufacturado 14
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoVegetariano)
                   .articuloInsumo(insumoMedallonVegetariano)
                   .cantidad(2) // Doble medallón vegetariano
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoVegetariano)
                   .articuloInsumo(insumoPanLomo)
                   .cantidad(1)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoVegetariano)
                   .articuloInsumo(insumoQueso)
                   .cantidad(60)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoVegetariano)
                   .articuloInsumo(insumoLechuga)
                   .cantidad(50)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoVegetariano)
                   .articuloInsumo(insumoTomate)
                   .cantidad(70)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(lomoVegetariano)
                   .articuloInsumo(insumoMorrones)
                   .cantidad(40)
                   .build());

           // ARTICULO MANUFACTURADO 15 - Pizza Fugazzeta
           ArticuloManufacturado pizzaFugazzeta = articuloManufacturadoRepo
                   .save(ArticuloManufacturado.builder()
                           .denominacion("Pizza Fugazzeta")
                           .descripcion("Pizza con abundante cebolla, mozzarella y aceite de oliva")
                           .tiempoEstimadoMinutos(20)
                           .precioVenta(4700.0)
                           .categoria(categoriaPizzas)
                           .imagen(imagenPizza)
                           .build());

           // Detalles de articulo manufacturado 15
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaFugazzeta)
                   .articuloInsumo(insumoHarina)
                   .cantidad(200)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaFugazzeta)
                   .articuloInsumo(insumoQueso)
                   .cantidad(200) // Extra queso
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaFugazzeta)
                   .articuloInsumo(insumoCebolla)
                   .cantidad(150) // Abundante cebolla
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaFugazzeta)
                   .articuloInsumo(insumoAceiteOliva)
                   .cantidad(30)
                   .build());
           amdRepo.save(ArticuloManufacturadoDetalle.builder()
                   .articuloManufacturado(pizzaFugazzeta)
                   .articuloInsumo(insumoOregano)
                   .cantidad(10)
                   .build());

           // --------------------------------------------------------------------------------------------------------------------------------
           // 10. PROMOCIONES
           // --------------------------------------------------------------------------------------------------------------------------------

           LocalDate fechaInicio = LocalDate.now();
           LocalDate fechaFin = LocalDate.now().plusDays(60);

           // Imágenes para promociones
           Imagen img1 = imagenRepo.save(Imagen.builder()
                   .url("/img/promociones/combo-cerveza-hamburguesa.png")
                   .build());
           Imagen img2 = imagenRepo.save(Imagen.builder()
                   .url("/img/promociones/combo-pizza-coca.png")
                   .build());
           Imagen img3 = imagenRepo.save(Imagen.builder()
                   .url("/img/promociones/combo-4q-cerveza.png")
                   .build());
           Imagen img4 = imagenRepo.save(Imagen.builder()
                   .url("/img/promociones/combo-fernet-coca.png")
                   .build());
           Imagen img5 = imagenRepo.save(Imagen.builder()
                   .url("/img/promociones/combo-agua-jugo.png")
                   .build());

           // PROMO 1 - Cerveza + Hamburguesa
           Promocion promo1 = Promocion.builder()
                   .denominacion("🍺 Cerveza + Hamburguesa")
                   .fechaDesde(fechaInicio)
                   .fechaHasta(fechaFin)
                   .horaDesde(LocalTime.of(18, 0))
                   .horaHasta(LocalTime.of(23, 59))
                   .precioPromocional(5500.0)
                   .sucursal(sucursal)
                   .imagen(img1)
                   .build();

           // Crear detalles para PROMO 1
           PromocionDetalle detalle1_1 = PromocionDetalle.builder()
                   .promocion(promo1)
                   .articulo(insumoCervezaArtesanal)
                   .cantidadRequerida(1)
                   .build();

           PromocionDetalle detalle1_2 = PromocionDetalle.builder()
                   .promocion(promo1)
                   .articulo(hamburguesaClasica)
                   .cantidadRequerida(1)
                   .build();

           // Asignar detalles a la promoción
           promo1.setPromocionDetalles(List.of(detalle1_1, detalle1_2));

           // Calcular descuento
           double precioTotalPromo1 = insumoCervezaArtesanal.getPrecioVenta() + hamburguesaClasica.getPrecioVenta();
           double descuentoPromo1 = 100 - (promo1.getPrecioPromocional() / precioTotalPromo1) * 100;
           promo1.setDescuento(Math.round(descuentoPromo1 * 100.0) / 100.0);

           // Guardar promoción con detalles
           promo1 = promocionRepo.save(promo1);

           // PROMO 2 - Pizza Margherita + Coca Cola
           Promocion promo2 = Promocion.builder()
                   .denominacion("🍕 Margherita + Coca Cola")
                   .fechaDesde(fechaInicio)
                   .fechaHasta(fechaFin)
                   .horaDesde(LocalTime.of(11, 0))
                   .horaHasta(LocalTime.of(22, 0))
                   .precioPromocional(5800.0)
                   .sucursal(sucursal)
                   .imagen(img2)
                   .build();

           // Crear detalles para PROMO 2
           PromocionDetalle detalle2_1 = PromocionDetalle.builder()
                   .promocion(promo2)
                   .articulo(pizzaMargherita)
                   .cantidadRequerida(1)
                   .build();

           PromocionDetalle detalle2_2 = PromocionDetalle.builder()
                   .promocion(promo2)
                   .articulo(insumoCocaCola)
                   .cantidadRequerida(1)
                   .build();

           // Asignar detalles a la promoción
           promo2.setPromocionDetalles(List.of(detalle2_1, detalle2_2));

           // Calcular descuento
           double precioTotalPromo2 = pizzaMargherita.getPrecioVenta() + insumoCocaCola.getPrecioVenta();
           double descuentoPromo2 = 100 - (promo2.getPrecioPromocional() / precioTotalPromo2) * 100;
           promo2.setDescuento(Math.round(descuentoPromo2 * 100.0) / 100.0);

           // Guardar promoción con detalles
           promo2 = promocionRepo.save(promo2);

           // PROMO 3 - Cuatro Quesos + Cerveza Artesanal
           Promocion promo3 = Promocion.builder()
                   .denominacion("🍕 Cuatro Quesos + Cerveza Artesanal")
                   .fechaDesde(fechaInicio)
                   .fechaHasta(fechaFin)
                   .horaDesde(LocalTime.of(12, 0))
                   .horaHasta(LocalTime.of(23, 0))
                   .precioPromocional(7200.0)
                   .sucursal(sucursal)
                   .imagen(img3)
                   .build();

           // Crear detalles para PROMO 3
           PromocionDetalle detalle3_1 = PromocionDetalle.builder()
                   .promocion(promo3)
                   .articulo(pizzaCuatroQuesos)
                   .cantidadRequerida(1)
                   .build();

           PromocionDetalle detalle3_2 = PromocionDetalle.builder()
                   .promocion(promo3)
                   .articulo(insumoCervezaArtesanal)
                   .cantidadRequerida(1)
                   .build();

           // Asignar detalles a la promoción
           promo3.setPromocionDetalles(List.of(detalle3_1, detalle3_2));

           // Calcular descuento
           double precioTotalPromo3 = pizzaCuatroQuesos.getPrecioVenta() + insumoCervezaArtesanal.getPrecioVenta();
           double descuentoPromo3 = 100 - (promo3.getPrecioPromocional() / precioTotalPromo3) * 100;
           promo3.setDescuento(Math.round(descuentoPromo3 * 100.0) / 100.0);

           // Guardar promoción con detalles
           promo3 = promocionRepo.save(promo3);

           // PROMO 4 - Fernet + Coca Cola
           Promocion promo4 = Promocion.builder()
                   .denominacion("🥃 Fernet + Coca Cola")
                   .fechaDesde(fechaInicio)
                   .fechaHasta(fechaFin)
                   .horaDesde(LocalTime.of(18, 0))
                   .horaHasta(LocalTime.of(2, 0))
                   .precioPromocional(7200.0)
                   .sucursal(sucursal)
                   .imagen(img4)
                   .build();

           // Crear detalles para PROMO 4
           PromocionDetalle detalle4_1 = PromocionDetalle.builder()
                   .promocion(promo4)
                   .articulo(insumoFernet)
                   .cantidadRequerida(1)
                   .build();

           PromocionDetalle detalle4_2 = PromocionDetalle.builder()
                   .promocion(promo4)
                   .articulo(insumoCocaCola)
                   .cantidadRequerida(1)
                   .build();

           // Asignar detalles a la promoción
           promo4.setPromocionDetalles(List.of(detalle4_1, detalle4_2));

           // Calcular descuento
           double precioTotalPromo4 = insumoFernet.getPrecioVenta() + insumoCocaCola.getPrecioVenta();
           double descuentoPromo4 = 100 - (promo4.getPrecioPromocional() / precioTotalPromo4) * 100;
           promo4.setDescuento(Math.round(descuentoPromo4 * 100.0) / 100.0);

           // Guardar promoción con detalles
           promo4 = promocionRepo.save(promo4);

           // PROMO 5 - Agua + Jugo de Naranja
           Promocion promo5 = Promocion.builder()
                   .denominacion("🥤 Agua + Jugo de Naranja")
                   .fechaDesde(fechaInicio)
                   .fechaHasta(fechaFin)
                   .horaDesde(LocalTime.of(10, 0))
                   .horaHasta(LocalTime.of(20, 0))
                   .precioPromocional(2500.0)
                   .sucursal(sucursal)
                   .imagen(img5)
                   .build();

           // Crear detalles para PROMO 5
           PromocionDetalle detalle5_1 = PromocionDetalle.builder()
                   .promocion(promo5)
                   .articulo(insumoAguaMineral)
                   .cantidadRequerida(1)
                   .build();

           PromocionDetalle detalle5_2 = PromocionDetalle.builder()
                   .promocion(promo5)
                   .articulo(insumoJugoDeNaranja)
                   .cantidadRequerida(1)
                   .build();

           // Asignar detalles a la promoción
           promo5.setPromocionDetalles(List.of(detalle5_1, detalle5_2));

           // Calcular descuento
           double precioTotalPromo5 = insumoAguaMineral.getPrecioVenta() + insumoJugoDeNaranja.getPrecioVenta();
           double descuentoPromo5 = 100 - (promo5.getPrecioPromocional() / precioTotalPromo5) * 100;
           promo5.setDescuento(Math.round(descuentoPromo5 * 100.0) / 100.0);

           // Guardar promoción con detalles
           promo5 = promocionRepo.save(promo5);
       };
   }
}