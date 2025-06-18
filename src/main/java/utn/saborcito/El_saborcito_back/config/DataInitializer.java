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
            Usuario usuario = usuarioRepo.save(Usuario.builder().auth0Id("auth0|1").username("admin")
                    .email("admin@ejemplo.com").password("pass").nombre("Admin").apellido("User")
                    .telefono("+541111111")
                    .fechaNacimiento(LocalDate.of(1990, 1, 1)).rol(Rol.ADMIN).estado(true)
                    .fechaRegistro(LocalDateTime.now()).fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            // 2. Empresa, sucursal y domicilio
            Empresa empresa = empresaRepo.save(Empresa.builder().nombre("El Saborcito SA")
                    .razonSocial("El Saborcito SRL").cuil("20-12345678-9").build());

            // Primero creamos el domicilio sin persistirlo
            Domicilio domicilio = Domicilio.builder()
                    .calle("San Martín")
                    .numero(123)
                    .cp("5500")
                    .usuario(usuario)
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
            } // 3. Inicializar Estados de Pedidos
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
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("CANCELADO").build()));// Inicializar
                                                                                                    // Tipos
                                                                                                    // de
                                                                                                    // Envío
            TipoEnvio tipoDelivery = tipoEnvioRepo.findByNombre("DELIVERY")
                    .orElseGet(() -> tipoEnvioRepo
                            .save(TipoEnvio.builder().nombre("DELIVERY").build()));
            TipoEnvio tipoTakeAway = tipoEnvioRepo.findByNombre("TAKE_AWAY")
                    .orElseGet(() -> tipoEnvioRepo
                            .save(TipoEnvio.builder().nombre("TAKE_AWAY").build()));
            TipoEnvio tipoLocal = tipoEnvioRepo.findByNombre("EN_LOCAL")
                    .orElseGet(() -> tipoEnvioRepo
                            .save(TipoEnvio.builder().nombre("EN_LOCAL").build())); // Inicializar
                                                                                    // Formas
                                                                                    // de
                                                                                    // Pago
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
            

            // --------------------------------------------------------------------------------------------------------------------------------
            // 10. PROMOCIONES 
            // --------------------------------------------------------------------------------------------------------------------------------

            LocalDate fechaInicio = LocalDate.now();
            LocalDate fechaFin = LocalDate.now().plusDays(60);

            // 1) 🍺 Cerveza + Hamburguesa
            Imagen img1 = imagenRepo.save(Imagen.builder()
                .url("/img/promociones/combo-cerveza-hamburguesa.png")
                .build());
            // 2) 🍕 Pizza Margherita + Coca Cola 500ml
            Imagen img2 = imagenRepo.save(Imagen.builder()
                .url("/img/promociones/combo-pizza-coca.png")
                .build());
            // 3) 🍕 Cuatro Quesos + Cerveza Artesanal
            Imagen img3 = imagenRepo.save(Imagen.builder()
                .url("/img/promociones/combo-4q-cerveza.png")
                .build());
            // 4) 🥃 Fernet 750ml + Coca Cola 500ml
            Imagen img4 = imagenRepo.save(Imagen.builder()
                .url("/img/promociones/combo-fernet-coca.png")
                .build());
        
            // 5) 🥤 Agua Mineral + Jugo de Naranja
            Imagen img5 = imagenRepo.save(Imagen.builder()
                .url("/img/promociones/combo-agua-jugo.png")
                .build());



            // PROMO 1
            Promocion promo1 = promocionRepo.save(Promocion.builder()
                .denominacion("🍺 Cerveza + Hamburguesa")
                .fechaDesde(fechaInicio)
                .fechaHasta(fechaFin)
                .horaDesde(LocalTime.of(18, 0))
                .horaHasta(LocalTime.of(23, 59))
                .precioPromocional(1500.0)
                .sucursal(sucursal)
                .imagen(img1)
                .build());

            // PROMO 2
            Promocion promo2 = promocionRepo.save(Promocion.builder()
                .denominacion("🍕 Margherita + Coca Cola")
                .fechaDesde(fechaInicio)
                .fechaHasta(fechaFin)
                .horaDesde(LocalTime.of(11, 0))
                .horaHasta(LocalTime.of(22, 0))
                .precioPromocional(5500.0)
                .sucursal(sucursal)
                .imagen(img2)
                .build());

            // PROMO 3
            Promocion promo3 = promocionRepo.save(Promocion.builder()
                .denominacion("🍕 Cuatro Quesos + Cerveza Artesanal")
                .fechaDesde(fechaInicio)
                .fechaHasta(fechaFin)
                .horaDesde(LocalTime.of(12, 0))
                .horaHasta(LocalTime.of(23, 0))
                .precioPromocional(7000.0)
                .sucursal(sucursal)
                .imagen(img3)
                .build());

            // PROMO 4   
            Promocion promo4 = promocionRepo.save(Promocion.builder()
                .denominacion("🥃 Fernet + Coca Cola")
                .fechaDesde(fechaInicio)
                .fechaHasta(fechaFin)
                .horaDesde(LocalTime.of(18, 0))
                .horaHasta(LocalTime.of(2, 0))
                .precioPromocional(6800.0)
                .sucursal(sucursal)
                .imagen(img4)
                .build());

            // PROMO 5
            Promocion promo5 = promocionRepo.save(Promocion.builder()
                .denominacion("🥤 Agua + Jugo de Naranja")
                .fechaDesde(fechaInicio)
                .fechaHasta(fechaFin)
                .horaDesde(LocalTime.of(10, 0))
                .horaHasta(LocalTime.of(20, 0))
                .precioPromocional(2400.0)
                .sucursal(sucursal)
                .imagen(img5)
                .build());

            // --------------------------------------------------------------------------------------------------------------------------------
            // DETALLE DE PROMOCIONES
            // --------------------------------------------------------------------------------------------------------------------------------

            // DETALLE PROMO 1
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo1)
                .articulo(insumoCervezaArtesanal)
                .cantidadRequerida(1)
                .build());
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo1)
                .articulo(hamburguesaClasica)
                .cantidadRequerida(1)
                .build());


            // DETALLE PROMO 2
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo2)
                .articulo(pizzaMargherita)
                .cantidadRequerida(1)
                .build());
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo2)
                .articulo(insumoCocaCola)
                .cantidadRequerida(1)
                .build());



            // DETALLE PROMO 3
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo3)
                .articulo(pizzaCuatroQuesos)
                .cantidadRequerida(1)
                .build());
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo3)
                .articulo(insumoCervezaArtesanal)
                .cantidadRequerida(1)
                .build());



            // DETALLE PROMO 4
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo4)
                .articulo(insumoFernet)
                .cantidadRequerida(1)
                .build());
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo4)
                .articulo(insumoCocaCola)
                .cantidadRequerida(1)
                .build());



            // DETALLE PROMO 5
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo5)
                .articulo(insumoAguaMineral)
                .cantidadRequerida(1)
                .build());
            promocionDetalleRepo.save(PromocionDetalle.builder()
                .promocion(promo5)
                .articulo(insumoJugoDeNaranja)
                .cantidadRequerida(1)
                .build());

                
             
                    
            // --------------------------------------------------------------------------------------------------------------------------------
            // USUARIOS CLIENTE
            // --------------------------------------------------------------------------------------------------------------------------------


            // CLIENTE 1
            Usuario usuarioCliente1 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|3")
                    .username("cliente1")
                    .email("cliente1@ejemplo.com")
                    .password("pass")
                    .nombre("María")
                    .apellido("González")
                    .telefono("+543333333")
                    .fechaNacimiento(LocalDate.of(1992, 8, 20))
                    .rol(Rol.CLIENTE)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            
            // Domicilios para cliente 1
            Domicilio domicilioCliente1 = domicilioRepo.save(Domicilio.builder()
                    .calle("Belgrano")
                    .numero(456)
                    .cp("5500")
                    .usuario(usuarioCliente1)
                    .localidad(localidad)
                    .build());


            // Se guarda cliente 1 
            Cliente cliente1 = clienteRepo.save(Cliente.builder()
                    .id(usuarioCliente1.getId())
                    .auth0Id(usuarioCliente1.getAuth0Id())
                    .username(usuarioCliente1.getUsername())
                    .email(usuarioCliente1.getEmail())
                    .password(usuarioCliente1.getPassword())
                    .nombre(usuarioCliente1.getNombre())
                    .apellido(usuarioCliente1.getApellido())
                    .telefono(usuarioCliente1.getTelefono())
                    .fechaNacimiento(usuarioCliente1.getFechaNacimiento())
                    .rol(usuarioCliente1.getRol())
                    .estado(usuarioCliente1.getEstado())
                    .fechaRegistro(usuarioCliente1.getFechaRegistro())
                    .fechaUltimaModificacion(usuarioCliente1.getFechaUltimaModificacion())
                    .domicilios(usuarioCliente1.getDomicilios())
                    .imagen(usuarioCliente1.getImagen())
                    .build());

            // CLIENTE 3
            Usuario usuario3 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|5")
                    .username("cliente3")
                    .email("cliente3@ejemplo.com")
                    .password("pass")
                    .nombre("Lucía")
                    .apellido("Martínez")
                    .telefono("+54555555123")
                    .fechaNacimiento(LocalDate.of(1990, 5, 15))
                    .rol(Rol.CLIENTE)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            // Domicilios para cliente 3
            Domicilio dom3 = domicilioRepo.save(Domicilio.builder()
                    .calle("San Martín")
                    .numero(123)
                    .cp("5500")
                    .usuario(usuario3)
                    .localidad(localidad)
                    .build());

            // Se guarda cliente 3
            Cliente cliente3 = clienteRepo.save(Cliente.builder()
                    .id(usuario3.getId())
                    .auth0Id(usuario3.getAuth0Id())
                    .username(usuario3.getUsername())
                    .email(usuario3.getEmail())
                    .password(usuario3.getPassword())
                    .nombre(usuario3.getNombre())
                    .apellido(usuario3.getApellido())
                    .telefono(usuario3.getTelefono())
                    .fechaNacimiento(usuario3.getFechaNacimiento())
                    .rol(usuario3.getRol())
                    .estado(usuario3.getEstado())
                    .fechaRegistro(usuario3.getFechaRegistro())
                    .fechaUltimaModificacion(usuario3.getFechaUltimaModificacion())
                    .domicilios(List.of(dom3))
                    .build());

            
            // CLIENTE 4
            Usuario usuario4 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|6")
                    .username("cliente4")
                    .email("cliente4@ejemplo.com")
                    .password("pass")
                    .nombre("Diego")
                    .apellido("Peralta")
                    .telefono("+54555555234")
                    .fechaNacimiento(LocalDate.of(1985, 11, 2))
                    .rol(Rol.CLIENTE)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            // Domicilios para cliente 4
            Domicilio dom4 = domicilioRepo.save(Domicilio.builder()
                    .calle("Belgrano")
                    .numero(456)
                    .cp("5501")
                    .usuario(usuario4)
                    .localidad(localidad)
                    .build());

            // Se guarda cliente 4
            Cliente cliente4 = clienteRepo.save(Cliente.builder()
                    .id(usuario4.getId())
                    .auth0Id(usuario4.getAuth0Id())
                    .username(usuario4.getUsername())
                    .email(usuario4.getEmail())
                    .password(usuario4.getPassword())
                    .nombre(usuario4.getNombre())
                    .apellido(usuario4.getApellido())
                    .telefono(usuario4.getTelefono())
                    .fechaNacimiento(usuario4.getFechaNacimiento())
                    .rol(usuario4.getRol())
                    .estado(usuario4.getEstado())
                    .fechaRegistro(usuario4.getFechaRegistro())
                    .fechaUltimaModificacion(usuario4.getFechaUltimaModificacion())
                    .domicilios(List.of(dom4))
                    .build());

            
            // CLIENTE 5
            Usuario usuario5 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|7")
                    .username("cliente5")
                    .email("cliente5@ejemplo.com")
                    .password("pass")
                    .nombre("María")
                    .apellido("Gómez")
                    .telefono("+54555555345")
                    .fechaNacimiento(LocalDate.of(1992, 3, 20))
                    .rol(Rol.CLIENTE)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            // Domicilios para cliente 5
            Domicilio dom5 = domicilioRepo.save(Domicilio.builder()
                    .calle("Rodríguez")
                    .numero(789)
                    .cp("5502")
                    .usuario(usuario5)
                    .localidad(localidad)
                    .build());

            // Se guarda cliente 5
            Cliente cliente5 = clienteRepo.save(Cliente.builder()
                    .id(usuario5.getId())
                    .auth0Id(usuario5.getAuth0Id())
                    .username(usuario5.getUsername())
                    .email(usuario5.getEmail())
                    .password(usuario5.getPassword())
                    .nombre(usuario5.getNombre())
                    .apellido(usuario5.getApellido())
                    .telefono(usuario5.getTelefono())
                    .fechaNacimiento(usuario5.getFechaNacimiento())
                    .rol(usuario5.getRol())
                    .estado(usuario5.getEstado())
                    .fechaRegistro(usuario5.getFechaRegistro())
                    .fechaUltimaModificacion(usuario5.getFechaUltimaModificacion())
                    .domicilios(List.of(dom5))
                    .build());


            // CLIENTE 6
            Usuario usuario6 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|8")
                    .username("cliente6")
                    .email("cliente6@ejemplo.com")
                    .password("pass")
                    .nombre("Juan")
                    .apellido("Pérez")
                    .telefono("+54555555456")
                    .fechaNacimiento(LocalDate.of(1988, 7, 8))
                    .rol(Rol.CLIENTE)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            // Domicilios para cliente 6
            Domicilio dom6 = domicilioRepo.save(Domicilio.builder()
                    .calle("San Juan")
                    .numero(321)
                    .cp("5503")
                    .usuario(usuario6)
                    .localidad(localidad)
                    .build());

            // Se guarda cliente 6
            Cliente cliente6 = clienteRepo.save(Cliente.builder()
                    .id(usuario6.getId())
                    .auth0Id(usuario6.getAuth0Id())
                    .username(usuario6.getUsername())
                    .email(usuario6.getEmail())
                    .password(usuario6.getPassword())
                    .nombre(usuario6.getNombre())
                    .apellido(usuario6.getApellido())
                    .telefono(usuario6.getTelefono())
                    .fechaNacimiento(usuario6.getFechaNacimiento())
                    .rol(usuario6.getRol())
                    .estado(usuario6.getEstado())
                    .fechaRegistro(usuario6.getFechaRegistro())
                    .fechaUltimaModificacion(usuario6.getFechaUltimaModificacion())
                    .domicilios(List.of(dom6))
                    .build());
            

            // CLIENTE 8
            Usuario usuario8 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|8")
                    .username("cliente8")
                    .email("cliente8@ejemplo.com")
                    .password("pass")
                    .nombre("Mariana")
                    .apellido("Pérez")
                    .telefono("+5491166677888")
                    .fechaNacimiento(LocalDate.of(1992,  3, 10))
                    .rol(Rol.CLIENTE)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());
            
            // Domicilios para cliente 8
            Domicilio dom8 = domicilioRepo.save(Domicilio.builder()
                    .calle("Belgrano")
                    .numero(789)
                    .cp("5500")
                    .usuario(usuario8)
                    .localidad(localidad)
                    .build());

            // Se guarda cliente 8
            Cliente cliente8 = clienteRepo.save(Cliente.builder()
                    .id(usuario8.getId())
                    .auth0Id(usuario8.getAuth0Id())
                    .username(usuario8.getUsername())
                    .email(usuario8.getEmail())
                    .password(usuario8.getPassword())
                    .nombre(usuario8.getNombre())
                    .apellido(usuario8.getApellido())
                    .telefono(usuario8.getTelefono())
                    .fechaNacimiento(usuario8.getFechaNacimiento())
                    .rol(usuario8.getRol())
                    .estado(usuario8.getEstado())
                    .fechaRegistro(usuario8.getFechaRegistro())
                    .fechaUltimaModificacion(usuario8.getFechaUltimaModificacion())
                    .domicilios(List.of(dom8))
                    .build());          



            // --------------------------------------------------------------------------------------------------------------------------------
            // USUARIOS EMPLEADO
            // --------------------------------------------------------------------------------------------------------------------------------

            // Usuario empleado 1
            Usuario usuarioEmpleado = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|2")
                    .username("empleado")
                    .email("empleado@ejemplo.com")
                    .password("pass")
                    .nombre("Juan")
                    .apellido("Pérez")
                    .telefono("+542222222")
                    .fechaNacimiento(LocalDate.of(1995, 5, 15))
                    .rol(Rol.CAJERO)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());


            // --------------------------------------------------------------------------------------------------------------------------------
            // USUARIOS COCINERO
            // --------------------------------------------------------------------------------------------------------------------------------
    

            // Empleado 1 cocinero 1
            Empleado empleadoCocinero = empleadoRepo.save(Empleado.builder()
                    .id(usuarioEmpleado.getId())
                    .auth0Id(usuarioEmpleado.getAuth0Id())
                    .username(usuarioEmpleado.getUsername())
                    .email(usuarioEmpleado.getEmail())
                    .password(usuarioEmpleado.getPassword())
                    .nombre(usuarioEmpleado.getNombre())
                    .apellido(usuarioEmpleado.getApellido())
                    .telefono(usuarioEmpleado.getTelefono())
                    .fechaNacimiento(usuarioEmpleado.getFechaNacimiento())
                    .rol(usuarioEmpleado.getRol())
                    .estado(usuarioEmpleado.getEstado())
                    .fechaRegistro(usuarioEmpleado.getFechaRegistro())
                    .fechaUltimaModificacion(usuarioEmpleado.getFechaUltimaModificacion())
                    .domicilios(usuarioEmpleado.getDomicilios())
                    .imagen(usuarioEmpleado.getImagen())
                    .legajo("C001")
                    .fechaIngreso(LocalDate.now().minusMonths(6))
                    .sucursal(sucursal)
                    .primerLogin(true) // Agregando primerLogin como true para nuevos empleados
                    .build());


            
            // --------------------------------------------------------------------------------------------------------------------------------
            // USUARIOS CAJERO
            // --------------------------------------------------------------------------------------------------------------------------------


            // Empleado cajero 1
            Usuario usuarioCajero = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|5")
                    .username("cajero")
                    .email("cajero@ejemplo.com")
                    .password("pass")
                    .nombre("Ana")
                    .apellido("Martínez")
                    .telefono("+545555555")
                    .fechaNacimiento(LocalDate.of(1993, 4, 12))
                    .rol(Rol.CAJERO)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());
            Empleado empleadoCajero = empleadoRepo.save(Empleado.builder()
                    .id(usuarioCajero.getId())
                    .auth0Id(usuarioCajero.getAuth0Id())
                    .username(usuarioCajero.getUsername())
                    .email(usuarioCajero.getEmail())
                    .password(usuarioCajero.getPassword())
                    .nombre(usuarioCajero.getNombre())
                    .apellido(usuarioCajero.getApellido())
                    .telefono(usuarioCajero.getTelefono())
                    .fechaNacimiento(usuarioCajero.getFechaNacimiento())
                    .rol(usuarioCajero.getRol())
                    .estado(usuarioCajero.getEstado())
                    .fechaRegistro(usuarioCajero.getFechaRegistro())
                    .fechaUltimaModificacion(usuarioCajero.getFechaUltimaModificacion())
                    .domicilios(usuarioCajero.getDomicilios())
                    .imagen(usuarioCajero.getImagen())
                    .legajo("CA001")
                    .fechaIngreso(LocalDate.now().minusMonths(3))
                    .sucursal(sucursal)
                    .primerLogin(true) // Agregando primerLogin como true para nuevos empleados
                    .build());




            // --------------------------------------------------------------------------------------------------------------------------------
            // USUARIOS DELIVERY
            // --------------------------------------------------------------------------------------------------------------------------------


            // Usuario delivery 1
            Usuario usuarioDelivery = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|6")
                    .username("delivery")
                    .email("delivery@ejemplo.com")
                    .password("pass")
                    .nombre("Pedro")
                    .apellido("Gómez")
                    .telefono("+546666666")
                    .fechaNacimiento(LocalDate.of(1997, 8, 25))
                    .rol(Rol.DELIVERY)
                    .estado(true)
                    .fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());
            Empleado empleadoDelivery = empleadoRepo.save(Empleado.builder()
                    .id(usuarioDelivery.getId())
                    .auth0Id(usuarioDelivery.getAuth0Id())
                    .username(usuarioDelivery.getUsername())
                    .email(usuarioDelivery.getEmail())
                    .password(usuarioDelivery.getPassword())
                    .nombre(usuarioDelivery.getNombre())
                    .apellido(usuarioDelivery.getApellido())
                    .telefono(usuarioDelivery.getTelefono())
                    .fechaNacimiento(usuarioDelivery.getFechaNacimiento())
                    .rol(usuarioDelivery.getRol())
                    .estado(usuarioDelivery.getEstado())
                    .fechaRegistro(usuarioDelivery.getFechaRegistro())
                    .fechaUltimaModificacion(usuarioDelivery.getFechaUltimaModificacion())
                    .domicilios(usuarioDelivery.getDomicilios())
                    .imagen(usuarioDelivery.getImagen())
                    .legajo("D001")
                    .fechaIngreso(LocalDate.now().minusMonths(1))
                    .sucursal(sucursal)
                    .primerLogin(true) // Agregando primerLogin como true para el nuevo empleado
                    .build());



            // --------------------------------------------------------------------------------------------------------------------------------
            // PEDIDOS, DETALLES Y FACTURAS
            // --------------------------------------------------------------------------------------------------------------------------------
    
            // Pedido 1 del Cliente 1
            Pedido pedidoCliente1 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(2))
                    .horasEstimadaFinalizacion(LocalTime.of(20, 30))
                    .total(5600.0) // Hamburguesa + Coca Cola
                    .totalCosto(4300.0)
                    .cliente(cliente1)
                    .sucursal(sucursal) // Vinculamos a la sucursal
                    .estado(estadoEntregado) // Estado del pedido
                    .tipoEnvio(tipoDelivery) // Tipo de envío
                    .formaPago(formaPagoEfectivo) // Forma de pago
                    .build());

            // Detalles del pedido 1 del Cliente 1 
            DetallePedido detallePedido1_1 = detallePedidoRepo.save(DetallePedido.builder()
                    .cantidad(1)
                    .pedido(pedidoCliente1)
                    .articulo(hamburguesaClasica)
                    .build());

            // Detalles del pedido 1 del Cliente 1
            DetallePedido detallePedido1_2 = detallePedidoRepo.save(DetallePedido.builder()
                    .cantidad(1)
                    .pedido(pedidoCliente1)
                    .articulo(insumoCocaCola)
                    .build()); 
            
            // Factura del pedido 1 del Cliente 1
            Factura facturaCliente1 = facturaRepo.save(Factura.builder()
                    .fechaFacturacion(LocalDate.now().minusDays(2))
                    .formaPago(formaPagoEfectivo)
                    .totalVenta(5600.0)
                    .pedido(pedidoCliente1)
                    .build());



            
            // Pedidos 1 del cliente 3
            Pedido p3_1 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(5))
                    .horasEstimadaFinalizacion(LocalTime.of(19, 0))
                    .total(7500.0)
                    .totalCosto(5000.0)
                    .cliente(cliente3)
                    .sucursal(sucursal)
                    .estado(estadoEntregado)
                    .tipoEnvio(tipoDelivery)
                    .formaPago(formaPagoEfectivo)
                    .build());

            // Detalle del pedido 1 del cliente 3
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p3_1).cantidad(2).articulo(hamburguesaClasica).build());
            

            // Detalle del pedido 1 del cliente 3
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p3_1).cantidad(1).articulo(insumoCocaCola).build());

                    
            // Pedidos 2 del cliente 3
            Pedido p3_2 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(3))
                    .horasEstimadaFinalizacion(LocalTime.of(20, 30))
                    .total(11200.0)
                    .totalCosto(7000.0)
                    .cliente(cliente3)
                    .sucursal(sucursal)
                    .estado(estadoConfirmado)
                    .tipoEnvio(tipoTakeAway)
                    .formaPago(formaPagoMercadoPago)
                    .build());
            
            // Detalles del pedido 2 del cliente 3
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p3_2).cantidad(1).articulo(pizzaMozzarella).build());
            
            // Detalles del pedido 2 del cliente 3
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p3_2).cantidad(2).articulo(pizzaMargherita).build());

            
            // Pedidos 3 del cliente 3 (con promoción)

            Pedido p3_3 = pedidoRepo.save(Pedido.builder()
                .fechaPedido(LocalDate.now().minusDays(1))
                .horasEstimadaFinalizacion(LocalTime.of(21, 0))
                .total(promo1.getPrecioPromocional())
                .totalCosto(
                        insumoCervezaArtesanal.getPrecioVenta().doubleValue() +
                        hamburguesaClasica.getPrecioVenta().doubleValue()
                )
                .cliente(cliente3)
                .sucursal(sucursal)
                .estado(estadoEntregado)
                .tipoEnvio(tipoDelivery)
                .formaPago(formaPagoMercadoPago)
                .build());

                // Asociar la promoción al pedido
                detallePedidoPromocionRepo.save(DetallePedidoPromocion.builder()
                .pedido(p3_3)
                .promocion(promo1)
                .cantidadPromocion(1)
                .precioTotalPromocion(promo1.getPrecioPromocional())
                .ahorroTotal(
                        // calcula cuánto se hubiera pagado sin promo menos el precio promocional
                        insumoCervezaArtesanal.getPrecioVenta().doubleValue() +
                        hamburguesaClasica.getPrecioVenta().doubleValue()
                        - promo1.getPrecioPromocional()
                )
                .build());


            
            // Pedido 1 del cliente 4
            Pedido p4_1 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(4))
                    .horasEstimadaFinalizacion(LocalTime.of(18, 45))
                    .total(6400.0)
                    .totalCosto(4200.0)
                    .cliente(cliente4)
                    .sucursal(sucursal)
                    .estado(estadoPendiente)
                    .tipoEnvio(tipoLocal)
                    .formaPago(formaPagoEfectivo)
                    .build());
            
            // Detalles del pedido 1 del cliente 4
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p4_1).cantidad(1).articulo(pizzaMozzarella).build());


            // Pedidos 2 del cliente 4
            Pedido p4_2 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(2))
                    .horasEstimadaFinalizacion(LocalTime.of(20, 0))
                    .total(9800.0)
                    .totalCosto(6100.0)
                    .cliente(cliente4)
                    .sucursal(sucursal)
                    .estado(estadoEnPreparacion)
                    .tipoEnvio(tipoDelivery)
                    .formaPago(formaPagoEfectivo)
                    .build());

            // Detalles del pedido 2 del cliente 4
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p4_2).cantidad(2).articulo(hamburguesaClasica).build());

            // Detalles del pedido 2 del cliente 4
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p4_2).cantidad(1).articulo(pizzaMozzarella).build());

            
            // Pedidos 3 del cliente 4
            Pedido p4_3 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(1))
                    .horasEstimadaFinalizacion(LocalTime.of(21, 15))
                    .total(5500.0)
                    .totalCosto(3500.0)
                    .cliente(cliente4)
                    .sucursal(sucursal)
                    .estado(estadoConfirmado)
                    .tipoEnvio(tipoTakeAway)
                    .formaPago(formaPagoMercadoPago)
                    .build());
                
            // Detalles del pedido 3 del cliente 4
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p4_3).cantidad(1).articulo(pizzaMozzarella).build());

            

            // Pedido 1 del cliente 5 
            Pedido p5_1 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(3))
                    .horasEstimadaFinalizacion(LocalTime.of(19, 30))
                    .total(7200.0)
                    .totalCosto(4800.0)
                    .cliente(cliente5)
                    .sucursal(sucursal)
                    .estado(estadoEntregado)
                    .tipoEnvio(tipoDelivery)
                    .formaPago(formaPagoMercadoPago)
                    .build());

            // Detalles del pedido 1 del cliente 5
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p5_1).cantidad(2).articulo(pizzaPepperoni).build());


            // Pedido 2 del cliente 5
            Pedido p5_2 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(1))
                    .horasEstimadaFinalizacion(LocalTime.of(20, 45))
                    .total(4300.0)
                    .totalCosto(2900.0)
                    .cliente(cliente5)
                    .sucursal(sucursal)
                    .estado(estadoPendiente)
                    .tipoEnvio(tipoLocal)
                    .formaPago(formaPagoEfectivo)
                    .build());
            
            // Detalles del pedido 2 del cliente 5
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p5_2).cantidad(1).articulo(hamburguesaClasica).build());
            
            // Detalles del pedido 2 del cliente 5
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p5_2).cantidad(1).articulo(insumoCocaCola).build());


            
            // Pedido 1 del cliente 6
            Pedido p6_1 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(6))
                    .horasEstimadaFinalizacion(LocalTime.of(18, 0))
                    .total(6800.0)
                    .totalCosto(4500.0)
                    .cliente(cliente6)
                    .sucursal(sucursal)
                    .estado(estadoConfirmado)
                    .tipoEnvio(tipoDelivery)
                    .formaPago(formaPagoEfectivo)
                    .build());
                
            // Detalles del pedido 1 del cliente 6
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p6_1).cantidad(1).articulo(pizzaMozzarella).build());


            // Pedido 2 del cliente 6
            Pedido p6_2 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(4))
                    .horasEstimadaFinalizacion(LocalTime.of(20, 0))
                    .total(10200.0)
                    .totalCosto(6500.0)
                    .cliente(cliente6)
                    .sucursal(sucursal)
                    .estado(estadoEntregado)
                    .tipoEnvio(tipoTakeAway)
                    .formaPago(formaPagoEfectivo)
                    .build());

            // Detalles del pedido 2 del cliente 6
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p6_2).cantidad(2).articulo(hamburguesaClasica).build());


            // Pedido 3 del cliente 6
            Pedido p6_3 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(2))
                    .horasEstimadaFinalizacion(LocalTime.of(21, 0))
                    .total(5300.0)
                    .totalCosto(3300.0)
                    .cliente(cliente6)
                    .sucursal(sucursal)
                    .estado(estadoEnPreparacion)
                    .tipoEnvio(tipoLocal)
                    .formaPago(formaPagoMercadoPago)
                    .build());


            // Detalles del pedido 3 del cliente 6
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p6_3).cantidad(1).articulo(pizzaMozzarella).build());


            // Pedido 4 del cliente 6
            Pedido p6_4 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now().minusDays(1))
                    .horasEstimadaFinalizacion(LocalTime.of(19, 30))
                    .total(8900.0)
                    .totalCosto(5700.0)
                    .cliente(cliente6)
                    .sucursal(sucursal)
                    .estado(estadoPendiente)
                    .tipoEnvio(tipoDelivery)
                    .formaPago(formaPagoMercadoPago)
                    .build());


            // Detalles del pedido 4 del cliente 6
            detallePedidoRepo.save(DetallePedido.builder()
                    .pedido(p6_4).cantidad(2).articulo(pizzaMozzarella).build());

            
            // Pedido 1 del cliente 8 (con promoción)
            Pedido p8_1 = pedidoRepo.save(Pedido.builder()
                .fechaPedido(LocalDate.now())
                .horasEstimadaFinalizacion(LocalTime.of(20, 30))
                .total(promo2.getPrecioPromocional())
                .totalCosto(0.0) // ajustar según coste real si lo deseas
                .cliente(cliente8)
                .sucursal(sucursal)
                .estado(estadoConfirmado)
                .tipoEnvio(tipoTakeAway)
                .formaPago(formaPagoEfectivo)
                .build());

            // Asociar la promoción al pedido p8_1

            detallePedidoPromocionRepo.save(DetallePedidoPromocion.builder()
                .pedido(p8_1)
                .promocion(promo2)
                .cantidadPromocion(1)
                .precioTotalPromocion(promo2.getPrecioPromocional())
                .ahorroTotal(
                    insumoCervezaArtesanal.getPrecioVenta().doubleValue() +
                    hamburguesaClasica.getPrecioVenta().doubleValue()
                    - promo2.getPrecioPromocional()
                )
                .build());


            // --------------------------------------------------------------------------------------------------------------------------------
            // HISTORIAL DE PEDIDOS
            // --------------------------------------------------------------------------------------------------------------------------------

            // Historial para el pedido del Cliente 1
            HistorialPedido historialPedido1 = historialPedidoRepo.save(HistorialPedido.builder()
                    .cliente(cliente1)
                    .pedido(pedidoCliente1)
                    .fechaRegistro(LocalDateTime.now().minusDays(2))
                    .observacion("Pedido entregado correctamente")
                    .build());

            // Historial adicional para Cliente 1 (pedido anterior)
            HistorialPedido historialPedidoAnterior = historialPedidoRepo.save(HistorialPedido.builder()
                    .cliente(cliente1)
                    .pedido(pedidoCliente1)
                    .fechaRegistro(LocalDateTime.now().minusDays(15))
                    .observacion("Historial de pedido anterior del cliente")
                    .build());

            // Si tienes un estado DEMORADO, usa estadoDemorado, si no, crea uno arriba y
            // úsalo aquí
            Estado estadoDemorado = estadoRepo.findByNombre("DEMORADO")
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("DEMORADO").build()));
            for (int i = 1; i <= 2; i++) {
                Pedido pedidoDemorado = pedidoRepo.save(Pedido.builder()
                        .estado(estadoDemorado)
                        .cliente(i % 2 == 0 ? cliente1 : cliente3)
                        .sucursal(sucursal)
                        .fechaPedido(LocalDate.now())
                        .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(70 + i * 5))
                        .tipoEnvio(tipoTakeAway)
                        .formaPago(formaPagoEfectivo)
                        .build());
                detallePedidoRepo.save(DetallePedido.builder()
                        .pedido(pedidoDemorado)
                        .cantidad(1)
                        .articulo(hamburguesaVegetariana)
                        .build());
            }

            for (int i = 1; i <= 3; i++) {
                Pedido pedidoListo = pedidoRepo.save(Pedido.builder()
                        .estado(estadoListo)
                        .cliente(i % 2 == 0 ? cliente1 : cliente3)
                        .sucursal(sucursal)
                        .fechaPedido(LocalDate.now())
                        .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(80 + i * 5))
                        .tipoEnvio(tipoLocal)
                        .formaPago(formaPagoEfectivo)
                        .build());
                detallePedidoRepo.save(DetallePedido.builder()
                        .pedido(pedidoListo)
                        .cantidad(1)
                        .articulo(pizzaCuatroQuesos)
                        .build());
            }

            // Verificación: listar y mostrar entidades para probar interacciones
            // System.out.println("=== Verificación de datos cargados ===");
            // System.out.println("Paises: " + paisRepo.findAll());
            // System.out.println("Provincias: " + provinciaRepo.findAll());
            // System.out.println("Localidades: " + localidadRepo.findAll());
            // System.out.println("Usuarios: " + usuarioRepo.findAll());
            // System.out.println("Empresas: " + empresaRepo.findAll());
            // System.out.println("Domicilios: " + domicilioRepo.findAll());
            // System.out.println("Sucursales: " + sucursalRepo.findAll());
            // System.out.println("UnidadMedida: " + unidadMedidaRepo.findAll());
            // System.out.println("Categorias: " + categoriaRepo.findAll());
            // System.out.println("Imagenes: " + imagenRepo.findAll());
            // System.out.println("Insumos: " + articuloInsumoRepo.findAll());
            // System.out.println("Manufacturados: " + articuloManufacturadoRepo.findAll());
            // System.out.println("Detalles Manufacturado: " + amdRepo.findAll());
            // System.out.println("Promociones: " + promocionRepo.findAll());
            // System.out.println("Clientes: " + clienteRepo.findAll());
            // System.out.println("Pedidos: " + pedidoRepo.findAll());
            // System.out.println("DetallePedido: " + detallePedidoRepo.findAll());
            // System.out.println("Facturas: " + facturaRepo.findAll());
            // System.out.println("Datos MercadoPago: " + datosMPRepo.findAll());
            // System.out.println("Horarios: " + horarioRepo.findAll());
        };
    }
}