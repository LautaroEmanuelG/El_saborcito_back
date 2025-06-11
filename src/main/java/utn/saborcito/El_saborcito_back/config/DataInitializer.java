// package utn.saborcito.El_saborcito_back.config;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import utn.saborcito.El_saborcito_back.models.Categoria;
// import utn.saborcito.El_saborcito_back.repositories.CategoriaRepository;
// import utn.saborcito.El_saborcito_back.repositories.PaisRepository;
// import utn.saborcito.El_saborcito_back.repositories.ProvinciaRepository;
// import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
// import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;
// import utn.saborcito.El_saborcito_back.repositories.EmpresaRepository;
// import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
// import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;
// import utn.saborcito.El_saborcito_back.repositories.UnidadMedidaRepository;
// import utn.saborcito.El_saborcito_back.repositories.ImagenRepository;
// import utn.saborcito.El_saborcito_back.repositories.ArticuloInsumoRepository;
// import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoRepository;
// import utn.saborcito.El_saborcito_back.repositories.ArticuloManufacturadoDetalleRepository;
// import utn.saborcito.El_saborcito_back.repositories.PromocionRepository;
// import utn.saborcito.El_saborcito_back.repositories.ClienteRepository;
// import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
// import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;
// import utn.saborcito.El_saborcito_back.repositories.FacturaRepository;
// import utn.saborcito.El_saborcito_back.repositories.DatosMercadoPagoRepository;
// import utn.saborcito.El_saborcito_back.repositories.HorarioAtencionRepository;
// import utn.saborcito.El_saborcito_back.repositories.EstadoRepository;
// import utn.saborcito.El_saborcito_back.repositories.TipoEnvioRepository;
// import utn.saborcito.El_saborcito_back.repositories.FormaPagoRepository;
// import utn.saborcito.El_saborcito_back.repositories.EmpleadoRepository;
// import utn.saborcito.El_saborcito_back.repositories.HistorialPedidoRepository;
// import utn.saborcito.El_saborcito_back.models.Pais;
// import utn.saborcito.El_saborcito_back.models.Provincia;
// import utn.saborcito.El_saborcito_back.models.Localidad;
// import utn.saborcito.El_saborcito_back.models.Usuario;
// import utn.saborcito.El_saborcito_back.models.Empresa;
// import utn.saborcito.El_saborcito_back.models.Domicilio;
// import utn.saborcito.El_saborcito_back.models.Sucursal;
// import utn.saborcito.El_saborcito_back.models.UnidadMedida;
// import utn.saborcito.El_saborcito_back.models.Imagen;
// import utn.saborcito.El_saborcito_back.models.ArticuloInsumo;
// import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
// import utn.saborcito.El_saborcito_back.models.ArticuloManufacturadoDetalle;
// import utn.saborcito.El_saborcito_back.models.Promocion;
// import utn.saborcito.El_saborcito_back.models.Cliente;
// import utn.saborcito.El_saborcito_back.models.Pedido;
// import utn.saborcito.El_saborcito_back.models.DetallePedido;
// import utn.saborcito.El_saborcito_back.models.Factura;
// import utn.saborcito.El_saborcito_back.models.DatosMercadoPago;
// import utn.saborcito.El_saborcito_back.models.HorarioAtencion;
// import utn.saborcito.El_saborcito_back.models.Estado;
// import utn.saborcito.El_saborcito_back.models.TipoEnvio;
// import utn.saborcito.El_saborcito_back.models.FormaPago;
// import utn.saborcito.El_saborcito_back.models.Empleado;
// import utn.saborcito.El_saborcito_back.models.HistorialPedido;
// import utn.saborcito.El_saborcito_back.enums.Rol;
// import utn.saborcito.El_saborcito_back.enums.DiaSemana;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;

// /**
//  * 🚀 Inicializador de datos para la aplicación
//  * Carga datos de ejemplo cuando la aplicación arranca
//  */
// @Configuration
// public class DataInitializer {

//     /**
//      * 📋 Bean que se ejecuta al iniciar la aplicación para cargar datos de prueba
//      */
//     @Bean
//     public CommandLineRunner initData(
//             PaisRepository paisRepo,
//             ProvinciaRepository provinciaRepo,
//             LocalidadRepository localidadRepo,
//             UsuarioRepository usuarioRepo,
//             EmpresaRepository empresaRepo,
//             DomicilioRepository domicilioRepo,
//             SucursalRepository sucursalRepo,
//             UnidadMedidaRepository unidadMedidaRepo,
//             CategoriaRepository categoriaRepo,
//             ImagenRepository imagenRepo,
//             ArticuloInsumoRepository articuloInsumoRepo,
//             ArticuloManufacturadoRepository articuloManufacturadoRepo,
//             ArticuloManufacturadoDetalleRepository amdRepo,
//             PromocionRepository promocionRepo,
//             ClienteRepository clienteRepo,
//             PedidoRepository pedidoRepo,
//             DetallePedidoRepository detallePedidoRepo,
//             FacturaRepository facturaRepo,
//             DatosMercadoPagoRepository datosMPRepo,
//             HorarioAtencionRepository horarioRepo,
//             EstadoRepository estadoRepo,
//             TipoEnvioRepository tipoEnvioRepo,
//             FormaPagoRepository formaPagoRepo,
//             EmpleadoRepository empleadoRepo,
//             HistorialPedidoRepository historialPedidoRepo) {
//         return args -> {
//             // 1. Geografía y usuarios
//             Pais pais = paisRepo.save(Pais.builder().nombre("Argentina").build());
//             Provincia provincia = provinciaRepo
//                     .save(Provincia.builder().nombre("Mendoza").pais(pais).build());
//             Localidad localidad = localidadRepo
//                     .save(Localidad.builder().nombre("Ciudad de Mendoza").provincia(provincia)
//                             .build());
//             Usuario usuario = usuarioRepo.save(Usuario.builder().auth0Id("auth0|1").username("admin")
//                     .email("admin@ejemplo.com").password("pass").nombre("Admin").apellido("User")
//                     .telefono("+541111111")
//                     .fechaNacimiento(LocalDate.of(1990, 1, 1)).rol(Rol.ADMIN).estado(true)
//                     .fechaRegistro(LocalDateTime.now()).fechaUltimaModificacion(LocalDateTime.now())
//                     .build());

//             // 2. Empresa, sucursal y domicilio
//             Empresa empresa = empresaRepo.save(Empresa.builder().nombre("El Saborcito SA")
//                     .razonSocial("El Saborcito SRL").cuil("20-12345678-9").build());

//             // Primero creamos el domicilio sin persistirlo
//             Domicilio domicilio = Domicilio.builder()
//                     .calle("San Martín")
//                     .numero(123)
//                     .cp("5500")
//                     .usuario(usuario)
//                     .localidad(localidad)
//                     .build();

//             // Luego creamos la sucursal con el domicilio (se persistirá en cascada)
//             Sucursal sucursal = sucursalRepo
//                     .save(Sucursal.builder().nombre("Sucursal Centro").empresa(empresa)
//                             .domicilio(domicilio).build());

//             // Agregamos horarios de atención para la sucursal
//             for (DiaSemana dia : DiaSemana.values()) {
//                 // Horario de 10 a 22 de lunes a jueves, 10 a 0 viernes y sábado, cerrado
//                 // domingo
//                 LocalTime apertura = LocalTime.of(10, 0);
//                 LocalTime cierre;

//                 if (dia == DiaSemana.VIERNES || dia == DiaSemana.SABADO) {
//                     cierre = LocalTime.of(0, 0); // Hasta medianoche
//                 } else if (dia == DiaSemana.DOMINGO) {
//                     cierre = null; // Cerrado los domingos
//                     apertura = null;
//                 } else {
//                     cierre = LocalTime.of(22, 0); // De lunes a jueves
//                 }

//                 if (apertura != null && cierre != null) {
//                     horarioRepo.save(HorarioAtencion.builder()
//                             .diaSemana(dia)
//                             .apertura(apertura)
//                             .cierre(cierre)
//                             .sucursal(sucursal).build());
//                 }
//             } // 3. Inicializar Estados de Pedidos
//               // Comprobar si ya existen los estados antes de crearlos
//             Estado estadoPendiente = estadoRepo.findByNombre("PENDIENTE")
//                     .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("PENDIENTE").build()));
//             Estado estadoConfirmado = estadoRepo.findByNombre("CONFIRMADO")
//                     .orElseGet(() -> estadoRepo
//                             .save(Estado.builder().nombre("CONFIRMADO").build()));
//             Estado estadoEnPreparacion = estadoRepo.findByNombre("EN_PREPARACION")
//                     .orElseGet(() -> estadoRepo
//                             .save(Estado.builder().nombre("EN_PREPARACION").build()));
//             Estado estadoListo = estadoRepo.findByNombre("LISTO")
//                     .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("LISTO").build()));
//             Estado estadoEnDelivery = estadoRepo.findByNombre("EN_DELIVERY")
//                     .orElseGet(() -> estadoRepo
//                             .save(Estado.builder().nombre("EN_DELIVERY").build()));
//             Estado estadoEntregado = estadoRepo.findByNombre("ENTREGADO")
//                     .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("ENTREGADO").build()));
//             Estado estadoCancelado = estadoRepo.findByNombre("CANCELADO")
//                     .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("CANCELADO").build())); // Inicializar
//                                                                                                      // Tipos
//                                                                                                      // de
//                                                                                                      // Envío
//             TipoEnvio tipoDelivery = tipoEnvioRepo.findByNombre("DELIVERY")
//                     .orElseGet(() -> tipoEnvioRepo
//                             .save(TipoEnvio.builder().nombre("DELIVERY").build()));
//             TipoEnvio tipoTakeAway = tipoEnvioRepo.findByNombre("TAKE_AWAY")
//                     .orElseGet(() -> tipoEnvioRepo
//                             .save(TipoEnvio.builder().nombre("TAKE_AWAY").build()));
//             TipoEnvio tipoLocal = tipoEnvioRepo.findByNombre("EN_LOCAL")
//                     .orElseGet(() -> tipoEnvioRepo
//                             .save(TipoEnvio.builder().nombre("EN_LOCAL").build())); // Inicializar
//                                                                                     // Formas
//                                                                                     // de
//                                                                                     // Pago
//             FormaPago formaPagoEfectivo = formaPagoRepo.findByNombre("EFECTIVO")
//                     .orElseGet(() -> formaPagoRepo
//                             .save(FormaPago.builder().nombre("EFECTIVO").build()));
//             FormaPago formaPagoTarjeta = formaPagoRepo.findByNombre("TARJETA")
//                     .orElseGet(() -> formaPagoRepo
//                             .save(FormaPago.builder().nombre("TARJETA").build()));
//             FormaPago formaPagoMercadoPago = formaPagoRepo.findByNombre("MERCADO_PAGO")
//                     .orElseGet(() -> formaPagoRepo
//                             .save(FormaPago.builder().nombre("MERCADO_PAGO").build()));
//             FormaPago formaPagoTransferencia = formaPagoRepo.findByNombre("TRANSFERENCIA")
//                     .orElseGet(() -> formaPagoRepo
//                             .save(FormaPago.builder().nombre("TRANSFERENCIA").build()));
//             // 4. Catálogo de productos - Creamos las categorías principales y subcategorías
//             // -- Categorías principales
//             Categoria categoriaSandwiches = categoriaRepo.save(
//                     Categoria.builder().denominacion("Sandwiches").sucursal(sucursal).build());
//             Categoria categoriaPizzas = categoriaRepo.save(
//                     Categoria.builder().denominacion("Pizzas").sucursal(sucursal).build());
//             Categoria categoriaBebidas = categoriaRepo.save(
//                     Categoria.builder().denominacion("Bebidas").sucursal(sucursal).build());
//             Categoria categoriaInsumos = categoriaRepo.save(
//                     Categoria.builder().denominacion("Insumos").sucursal(sucursal).build());

//             // -- Subcategorías
//             // Subcategorías de Sandwiches
//             Categoria categoriaHamburguesas = categoriaRepo.save(
//                     Categoria.builder().denominacion("Hamburguesas")
//                             .tipoCategoria(categoriaSandwiches)
//                             .sucursal(sucursal).build());
//             Categoria categoriaLomos = categoriaRepo.save(
//                     Categoria.builder().denominacion("Lomos")
//                             .tipoCategoria(categoriaSandwiches)
//                             .sucursal(sucursal).build());

//             // Subcategorías de Bebidas
//             Categoria categoriaGaseosas = categoriaRepo.save(
//                     Categoria.builder().denominacion("Gaseosas")
//                             .tipoCategoria(categoriaBebidas)
//                             .sucursal(sucursal).build());
//             Categoria categoriaAguas = categoriaRepo.save(
//                     Categoria.builder().denominacion("Aguas")
//                             .tipoCategoria(categoriaBebidas)
//                             .sucursal(sucursal).build());
//             Categoria categoriaTragos = categoriaRepo.save(
//                     Categoria.builder().denominacion("Tragos")
//                             .tipoCategoria(categoriaBebidas)
//                             .sucursal(sucursal).build());

//             // 5. Imágenes para cada tipo de producto
//             Imagen imagenHamburguesa = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/hamburguesas/hamburguesa.png")
//                             .build());
//             Imagen imagenLomo = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/lomos/lomo.png")
//                             .build());
//             Imagen imagenPizza = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/pizza/pizza.png")
//                             .build());
//             Imagen imagenGaseosa = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/bebidas/gaseosa.png")
//                             .build());
//             Imagen imagenAgua = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/bebidas/agua.png")
//                             .build());
//             Imagen imagenTrago = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/bebidas/trago.png")
//                             .build());
//             Imagen imagenDefault = imagenRepo
//                     .save(Imagen.builder().url("/img/default.png")
//                             .build());

//             // Nuevas imágenes para pizzas
//             Imagen imagenPizzaMuzarella = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/pizza/pizzamuzarella.png")
//                             .build());
//             Imagen imagenPizzaCuatroQuesos = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/pizza/pizzacuatroquesos.png")
//                             .build());
//             Imagen imagenPizzaHawaiana = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/pizza/pizzahawaiana.png")
//                             .build());
//             Imagen imagenPizzaMargherita = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/pizza/pizzamargherita.png")
//                             .build());
//             Imagen imagenPizzaPepperoni = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/pizza/pizzapepperoni.png")
//                             .build());

//             // Nuevas imágenes para hamburguesas
//             Imagen imagenHamburguesaBbq = imagenRepo
//                     .save(Imagen.builder()
//                             .url("/img/productos/hamburguesas/hamburguesabbq.png")
//                             .build());
//             Imagen imagenHamburguesaClasicaNueva = imagenRepo
//                     .save(Imagen.builder().url(
//                             "/img/productos/hamburguesas/hamburguesaclásica.png")
//                             .build());
//             Imagen imagenHamburguesaConQueso = imagenRepo
//                     .save(Imagen.builder().url(
//                             "/img/productos/hamburguesas/hamburguesaconqueso.png")
//                             .build());
//             Imagen imagenHamburguesaVegetariana = imagenRepo
//                     .save(Imagen.builder().url(
//                             "/img/productos/hamburguesas/hamburguesavegetariana.png")
//                             .build());

//             // Nuevas imágenes para bebidas
//             Imagen imagenAguaMineralNueva = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/bebidas/aguamineral.jpg")
//                             .build());
//             Imagen imagenCervezaArtesanal = imagenRepo
//                     .save(Imagen.builder()
//                             .url("/img/productos/bebidas/cervezaartesanal.png")
//                             .build());
//             Imagen imagenCocaColaNueva = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/bebidas/cocacola.jpg")
//                             .build());
//             Imagen imagenJugoDeNaranja = imagenRepo
//                     .save(Imagen.builder().url("/img/productos/bebidas/jugodenaranja.png")
//                             .build());

//             // 6. Unidades de medida
//             UnidadMedida unidadGramos = unidadMedidaRepo
//                     .save(UnidadMedida.builder().denominacion("Gramos").build());
//             UnidadMedida unidadLitros = unidadMedidaRepo
//                     .save(UnidadMedida.builder().denominacion("Litros").build());
//             UnidadMedida unidadUnidad = unidadMedidaRepo
//                     .save(UnidadMedida.builder().denominacion("Unidad").build());

//             // 5. Creamos artículos insumo (ingredientes y productos no elaborados)
//             // -- Ingredientes para elaboración (esParaElaborar = true)
//             ArticuloInsumo insumoHarina = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Harina 000")
//                     .precioVenta(1000.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(700.0)
//                     .stockActual(10000)
//                     .stockMaximo(20000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoQueso = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Queso Mozzarella")
//                     .precioVenta(2500.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(2000.0)
//                     .stockActual(5000)
//                     .stockMaximo(8000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoCarne = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Carne Molida Premium")
//                     .precioVenta(3500.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(3000.0)
//                     .stockActual(4000)
//                     .stockMaximo(7000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoPan = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Pan de Hamburguesa")
//                     .precioVenta(1500.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadUnidad)
//                     .precioCompra(1200.0)
//                     .stockActual(100)
//                     .stockMaximo(200)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoLechuga = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Lechuga Fresca")
//                     .precioVenta(800.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(600.0)
//                     .stockActual(2000)
//                     .stockMaximo(3000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoTomate = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Tomate")
//                     .precioVenta(900.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(700.0)
//                     .stockActual(3000)
//                     .stockMaximo(5000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoCebolla = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Cebolla")
//                     .precioVenta(600.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(400.0)
//                     .stockActual(2500)
//                     .stockMaximo(4000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoPanLomo = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Pan de Lomo")
//                     .precioVenta(1700.0)
//                     .categoria(categoriaLomos)
//                     .unidadMedida(unidadUnidad)
//                     .precioCompra(1400.0)
//                     .stockActual(80)
//                     .stockMaximo(150)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoCarneLomo = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Bife de Lomo")
//                     .precioVenta(5000.0)
//                     .categoria(categoriaLomos)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(4500.0)
//                     .stockActual(3000)
//                     .stockMaximo(5000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoSalsaTomate = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Salsa de Tomate")
//                     .precioVenta(1200.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(900.0)
//                     .stockActual(4000)
//                     .stockMaximo(6000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             // Insumos adicionales para pizzas y hamburguesas
//             ArticuloInsumo insumoQuesoProvolone = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Queso Provolone")
//                     .precioVenta(2800.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(2200.0)
//                     .stockActual(1000)
//                     .stockMaximo(2000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoQuesoRoquefort = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Queso Roquefort")
//                     .precioVenta(3000.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(2500.0)
//                     .stockActual(800)
//                     .stockMaximo(1500)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoQuesoParmesano = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Queso Parmesano")
//                     .precioVenta(3200.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(2600.0)
//                     .stockActual(1200)
//                     .stockMaximo(2200)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoJamon = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Jamón Cocido")
//                     .precioVenta(2000.0)
//                     .categoria(categoriaPizzas) // También podría ser para sandwiches
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(1500.0)
//                     .stockActual(3000)
//                     .stockMaximo(5000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoAnana = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Ananá en Rodajas")
//                     .precioVenta(1500.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos) // O unidad si son latas
//                     .precioCompra(1000.0)
//                     .stockActual(500)
//                     .stockMaximo(1000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoAlbahaca = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Albahaca Fresca")
//                     .precioVenta(500.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(300.0)
//                     .stockActual(1000)
//                     .stockMaximo(2000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoPepperoni = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Pepperoni")
//                     .precioVenta(2600.0)
//                     .categoria(categoriaPizzas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(2000.0)
//                     .stockActual(1500)
//                     .stockMaximo(2500)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoSalsaBBQ = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Salsa BBQ")
//                     .precioVenta(1300.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos) // O Litros si es comprada a granel
//                     .precioCompra(900.0)
//                     .stockActual(1000)
//                     .stockMaximo(1500)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoPanceta = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Panceta Ahumada")
//                     .precioVenta(2200.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(1700.0)
//                     .stockActual(2000)
//                     .stockMaximo(3000)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoQuesoCheddar = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Queso Cheddar")
//                     .precioVenta(2700.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadGramos)
//                     .precioCompra(2100.0)
//                     .stockActual(1500)
//                     .stockMaximo(2500)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             ArticuloInsumo insumoMedallonVegetariano = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Medallón Vegetariano")
//                     .precioVenta(1800.0)
//                     .categoria(categoriaHamburguesas)
//                     .unidadMedida(unidadUnidad)
//                     .precioCompra(1300.0)
//                     .stockActual(100)
//                     .stockMaximo(200)
//                     .esParaElaborar(true)
//                     .imagen(imagenDefault)
//                     .build());

//             // -- Productos terminados que no se elaboran (esParaElaborar = false)
//             ArticuloInsumo insumoCocaCola = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Coca Cola 500ml")
//                     .precioVenta(1800.0)
//                     .categoria(categoriaGaseosas)
//                     .unidadMedida(unidadLitros)
//                     .precioCompra(1300.0)
//                     .stockActual(200)
//                     .stockMaximo(300)
//                     .esParaElaborar(false)
//                     .imagen(imagenCocaColaNueva) // Usar la nueva imagen directamente
//                     .build());

//             ArticuloInsumo insumoAguaMineral = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Agua Mineral 500ml")
//                     .precioVenta(1200.0)
//                     .categoria(categoriaAguas)
//                     .unidadMedida(unidadLitros)
//                     .precioCompra(800.0)
//                     .stockActual(150)
//                     .stockMaximo(250)
//                     .esParaElaborar(false)
//                     .imagen(imagenAguaMineralNueva) // Usar la nueva imagen directamente
//                     .build());

//             ArticuloInsumo insumoFernet = articuloInsumoRepo.save(ArticuloInsumo.builder()
//                     .denominacion("Fernet 750ml")
//                     .precioVenta(6000.0)
//                     .categoria(categoriaTragos)
//                     .unidadMedida(unidadLitros)
//                     .precioCompra(4500.0)
//                     .stockActual(50)
//                     .stockMaximo(100)
//                     .esParaElaborar(false)
//                     .imagen(imagenTrago)
//                     .build());

//             // Nuevas bebidas (ArticuloInsumo)
//             ArticuloInsumo insumoCervezaArtesanal = articuloInsumoRepo
//                     .save(ArticuloInsumo.builder()
//                             .denominacion("Cerveza Artesanal")
//                             .precioVenta(2500.0)
//                             .categoria(categoriaTragos) // O una nueva categoría Cervezas si
//                                                         // se prefiere
//                             .unidadMedida(unidadLitros)
//                             .precioCompra(1800.0)
//                             .stockActual(100)
//                             .stockMaximo(150)
//                             .esParaElaborar(false)
//                             .imagen(imagenCervezaArtesanal)
//                             .build());

//             ArticuloInsumo insumoJugoDeNaranja = articuloInsumoRepo
//                     .save(ArticuloInsumo.builder()
//                             .denominacion("Jugo de Naranja")
//                             .precioVenta(1500.0)
//                             .categoria(categoriaBebidas) // O subcategoría Jugos
//                             .unidadMedida(unidadLitros)
//                             .precioCompra(1000.0)
//                             .stockActual(120)
//                             .stockMaximo(200)
//                             .esParaElaborar(false)
//                             .imagen(imagenJugoDeNaranja)
//                             .build());

//             // 7. Productos manufacturados (hamburguesas, pizzas, etc)
//             // Pizza Mozzarella (se mantiene o actualiza si es necesario)
//             ArticuloManufacturado pizzaMozzarella = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder()
//                             .denominacion("Pizza Mozzarella")
//                             .descripcion("Pizza con queso mozzarella y salsa de tomate")
//                             .tiempoEstimadoMinutos(20)
//                             .precioVenta(4500.0)
//                             .categoria(categoriaPizzas)
//                             .imagen(imagenPizzaMuzarella)
//                             .build());

//             // Detalles de Pizza Mozzarella
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(pizzaMozzarella)
//                     .articuloInsumo(insumoHarina)
//                     .cantidad(200)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(pizzaMozzarella)
//                     .articuloInsumo(insumoQueso)
//                     .cantidad(150)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(pizzaMozzarella)
//                     .articuloInsumo(insumoSalsaTomate)
//                     .cantidad(100)
//                     .build());

//             // Nuevas Pizzas
//             ArticuloManufacturado pizzaCuatroQuesos = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder().denominacion("Pizza Cuatro Quesos")
//                             .descripcion("Deliciosa pizza con mozzarella, provolone, roquefort y parmesano.")
//                             .precioVenta(5200.0).tiempoEstimadoMinutos(22)
//                             .categoria(categoriaPizzas).imagen(imagenPizzaCuatroQuesos)
//                             .build());
//             // Detalles Pizza Cuatro Quesos
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
//                     .articuloInsumo(insumoHarina).cantidad(200).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
//                     .articuloInsumo(insumoSalsaTomate).cantidad(80).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
//                     .articuloInsumo(insumoQueso).cantidad(100).build()); // Mozzarella
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
//                     .articuloInsumo(insumoQuesoProvolone).cantidad(50).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
//                     .articuloInsumo(insumoQuesoRoquefort).cantidad(50).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaCuatroQuesos)
//                     .articuloInsumo(insumoQuesoParmesano).cantidad(30).build());

//             ArticuloManufacturado pizzaHawaiana = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder().denominacion("Pizza Hawaiana")
//                             .descripcion("Exótica pizza con jamón, ananá y mozzarella.")
//                             .precioVenta(5000.0).tiempoEstimadoMinutos(20)
//                             .categoria(categoriaPizzas).imagen(imagenPizzaHawaiana)
//                             .build());
//             // Detalles Pizza Hawaiana
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
//                     .articuloInsumo(insumoHarina).cantidad(200).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
//                     .articuloInsumo(insumoSalsaTomate).cantidad(100).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
//                     .articuloInsumo(insumoQueso).cantidad(150).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
//                     .articuloInsumo(insumoJamon).cantidad(100).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaHawaiana)
//                     .articuloInsumo(insumoAnana).cantidad(80).build());

//             ArticuloManufacturado pizzaMargherita = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder().denominacion("Pizza Margherita")
//                             .descripcion("Clásica pizza con tomate, mozzarella fresca y albahaca.")
//                             .precioVenta(4800.0).tiempoEstimadoMinutos(18)
//                             .categoria(categoriaPizzas).imagen(imagenPizzaMargherita)
//                             .build());
//             // Detalles Pizza Margherita
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
//                     .articuloInsumo(insumoHarina).cantidad(200).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
//                     .articuloInsumo(insumoSalsaTomate).cantidad(120).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
//                     .articuloInsumo(insumoQueso).cantidad(180).build()); // Mozzarella fresca
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
//                     .articuloInsumo(insumoAlbahaca).cantidad(20).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaMargherita)
//                     .articuloInsumo(insumoTomate).cantidad(100).build()); // Rodajas de tomate
//                                                                           // fresco

//             ArticuloManufacturado pizzaPepperoni = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder().denominacion("Pizza Pepperoni")
//                             .descripcion("Sabrosa pizza con abundante pepperoni y mozzarella.")
//                             .precioVenta(5300.0).tiempoEstimadoMinutos(22)
//                             .categoria(categoriaPizzas).imagen(imagenPizzaPepperoni)
//                             .build());
//             // Detalles Pizza Pepperoni
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
//                     .articuloInsumo(insumoHarina).cantidad(200).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
//                     .articuloInsumo(insumoSalsaTomate).cantidad(100).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
//                     .articuloInsumo(insumoQueso).cantidad(150).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(pizzaPepperoni)
//                     .articuloInsumo(insumoPepperoni).cantidad(120).build());

//             // Hamburguesa Clásica (se crea una nueva instancia con la nueva imagen)
//             ArticuloManufacturado hamburguesaClasica = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder()
//                             .denominacion("Hamburguesa Clásica")
//                             .descripcion("Hamburguesa con lechuga, tomate y cebolla")
//                             .tiempoEstimadoMinutos(15)
//                             .precioVenta(3800.0)
//                             .categoria(categoriaHamburguesas)
//                             .imagen(imagenHamburguesaClasicaNueva) // Nueva imagen
//                             .build());

//             // Detalles de Hamburguesa Clásica
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaClasica)
//                     .articuloInsumo(insumoCarne)
//                     .cantidad(150)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaClasica)
//                     .articuloInsumo(insumoPan)
//                     .cantidad(1)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaClasica)
//                     .articuloInsumo(insumoLechuga)
//                     .cantidad(30)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaClasica)
//                     .articuloInsumo(insumoTomate)
//                     .cantidad(50)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaClasica)
//                     .articuloInsumo(insumoCebolla)
//                     .cantidad(30)
//                     .build());

//             // Nuevas Hamburguesas
//             ArticuloManufacturado hamburguesaBbq = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder()
//                             .denominacion("Hamburguesa BBQ")
//                             .descripcion("Hamburguesa con salsa BBQ, panceta y cebolla caramelizada.")
//                             .precioVenta(4200.0).tiempoEstimadoMinutos(18)
//                             .categoria(categoriaHamburguesas).imagen(imagenHamburguesaBbq)
//                             .build());
//             // Detalles Hamburguesa BBQ
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
//                     .articuloInsumo(insumoCarne).cantidad(180).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
//                     .articuloInsumo(insumoPan).cantidad(1).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
//                     .articuloInsumo(insumoQuesoCheddar).cantidad(40).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
//                     .articuloInsumo(insumoPanceta).cantidad(50).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
//                     .articuloInsumo(insumoSalsaBBQ).cantidad(30).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaBbq)
//                     .articuloInsumo(insumoCebolla).cantidad(30).build()); // Cebolla caramelizada o
//                                                                           // frita

//             ArticuloManufacturado hamburguesaConQueso = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder().denominacion("Hamburguesa con Queso")
//                             .descripcion("Jugosa hamburguesa con doble queso cheddar.")
//                             .precioVenta(4000.0).tiempoEstimadoMinutos(16)
//                             .categoria(categoriaHamburguesas)
//                             .imagen(imagenHamburguesaConQueso).build());
//             // Detalles Hamburguesa con Queso
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
//                     .articuloInsumo(insumoCarne).cantidad(150).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
//                     .articuloInsumo(insumoPan).cantidad(1).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
//                     .articuloInsumo(insumoQuesoCheddar).cantidad(80).build()); // Doble cheddar
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
//                     .articuloInsumo(insumoLechuga).cantidad(20).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder().articuloManufacturado(hamburguesaConQueso)
//                     .articuloInsumo(insumoTomate).cantidad(30).build());

//             ArticuloManufacturado hamburguesaVegetariana = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder().denominacion("Hamburguesa Vegetariana")
//                             .descripcion("Deliciosa hamburguesa a base de plantas con vegetales frescos.")
//                             .precioVenta(3900.0).tiempoEstimadoMinutos(15)
//                             .categoria(categoriaHamburguesas)
//                             .imagen(imagenHamburguesaVegetariana).build());
//             // Detalles Hamburguesa Vegetariana
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaVegetariana)
//                     .articuloInsumo(insumoMedallonVegetariano).cantidad(1).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoPan)
//                     .cantidad(1).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoLechuga)
//                     .cantidad(40).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoTomate)
//                     .cantidad(60).build());
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(hamburguesaVegetariana).articuloInsumo(insumoCebolla)
//                     .cantidad(30).build());

//             // Lomo Completo
//             ArticuloManufacturado lomoCompleto = articuloManufacturadoRepo
//                     .save(ArticuloManufacturado.builder()
//                             .denominacion("Lomo Completo")
//                             .descripcion("Lomo con jamón, queso, lechuga y tomate")
//                             .tiempoEstimadoMinutos(20)
//                             .precioVenta(5500.0)
//                             .categoria(categoriaLomos)
//                             .imagen(imagenLomo)
//                             .build());

//             // Detalles de Lomo Completo
//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(lomoCompleto)
//                     .articuloInsumo(insumoCarneLomo)
//                     .cantidad(200)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(lomoCompleto)
//                     .articuloInsumo(insumoPanLomo)
//                     .cantidad(1)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(lomoCompleto)
//                     .articuloInsumo(insumoLechuga)
//                     .cantidad(30)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(lomoCompleto)
//                     .articuloInsumo(insumoTomate)
//                     .cantidad(50)
//                     .build());

//             amdRepo.save(ArticuloManufacturadoDetalle.builder()
//                     .articuloManufacturado(lomoCompleto)
//                     .articuloInsumo(insumoQueso)
//                     .cantidad(50)
//                     .build());

//             // 8. Usuarios con diferentes roles
//             // Usuario empleado
//             Usuario usuarioEmpleado = usuarioRepo.save(Usuario.builder()
//                     .auth0Id("auth0|2")
//                     .username("empleado")
//                     .email("empleado@ejemplo.com")
//                     .password("pass")
//                     .nombre("Juan")
//                     .apellido("Pérez")
//                     .telefono("+542222222")
//                     .fechaNacimiento(LocalDate.of(1995, 5, 15))
//                     .rol(Rol.CAJERO)
//                     .estado(true)
//                     .fechaRegistro(LocalDateTime.now())
//                     .fechaUltimaModificacion(LocalDateTime.now())
//                     .build());

//             // Usuario cliente (para el sistema)
//             Usuario usuarioCliente1 = usuarioRepo.save(Usuario.builder()
//                     .auth0Id("auth0|3")
//                     .username("cliente1")
//                     .email("cliente1@ejemplo.com")
//                     .password("pass")
//                     .nombre("María")
//                     .apellido("González")
//                     .telefono("+543333333")
//                     .fechaNacimiento(LocalDate.of(1992, 8, 20))
//                     .rol(Rol.CLIENTE)
//                     .estado(true)
//                     .fechaRegistro(LocalDateTime.now())
//                     .fechaUltimaModificacion(LocalDateTime.now())
//                     .build());

//             Usuario usuarioCliente2 = usuarioRepo.save(Usuario.builder()
//                     .auth0Id("auth0|4")
//                     .username("cliente2")
//                     .email("cliente2@ejemplo.com")
//                     .password("pass")
//                     .nombre("Carlos")
//                     .apellido("Rodríguez")
//                     .telefono("+544444444")
//                     .fechaNacimiento(LocalDate.of(1988, 3, 10))
//                     .rol(Rol.CLIENTE)
//                     .estado(true).fechaRegistro(LocalDateTime.now())
//                     .fechaUltimaModificacion(LocalDateTime.now())
//                     .build());

//             // 9. Clientes (entidad Cliente, relacionada con Usuario)
//             // Crear domicilios para clientes
//             Domicilio domicilioCliente1 = domicilioRepo.save(Domicilio.builder()
//                     .calle("Belgrano")
//                     .numero(456)
//                     .cp("5500")
//                     .usuario(usuarioCliente1)
//                     .localidad(localidad)
//                     .build());

//             Domicilio domicilioCliente2 = domicilioRepo.save(Domicilio.builder()
//                     .calle("Colón")
//                     .numero(789)
//                     .cp("5500")
//                     .usuario(usuarioCliente2)
//                     .localidad(localidad)
//                     .build()); // Crear entidades Cliente
//             Cliente cliente1 = clienteRepo.save(Cliente.builder()
//                     .id(usuarioCliente1.getId())
//                     .auth0Id(usuarioCliente1.getAuth0Id())
//                     .username(usuarioCliente1.getUsername())
//                     .email(usuarioCliente1.getEmail())
//                     .password(usuarioCliente1.getPassword())
//                     .nombre(usuarioCliente1.getNombre())
//                     .apellido(usuarioCliente1.getApellido())
//                     .telefono(usuarioCliente1.getTelefono())
//                     .fechaNacimiento(usuarioCliente1.getFechaNacimiento())
//                     .rol(usuarioCliente1.getRol())
//                     .estado(usuarioCliente1.getEstado())
//                     .fechaRegistro(usuarioCliente1.getFechaRegistro())
//                     .fechaUltimaModificacion(usuarioCliente1.getFechaUltimaModificacion())
//                     .domicilios(usuarioCliente1.getDomicilios())
//                     .imagen(usuarioCliente1.getImagen())
//                     .build());

//             Cliente cliente2 = clienteRepo.save(Cliente.builder()
//                     .id(usuarioCliente2.getId())
//                     .auth0Id(usuarioCliente2.getAuth0Id())
//                     .username(usuarioCliente2.getUsername())
//                     .email(usuarioCliente2.getEmail())
//                     .password(usuarioCliente2.getPassword())
//                     .nombre(usuarioCliente2.getNombre())
//                     .apellido(usuarioCliente2.getApellido())
//                     .telefono(usuarioCliente2.getTelefono())
//                     .fechaNacimiento(usuarioCliente2.getFechaNacimiento())
//                     .rol(usuarioCliente2.getRol())
//                     .estado(usuarioCliente2.getEstado())
//                     .fechaRegistro(usuarioCliente2.getFechaRegistro())
//                     .fechaUltimaModificacion(usuarioCliente2.getFechaUltimaModificacion())
//                     .domicilios(usuarioCliente2.getDomicilios())
//                     .imagen(usuarioCliente2.getImagen())
//                     .build());

//             // Empleado cocinero
//             Empleado empleadoCocinero = empleadoRepo.save(Empleado.builder()
//                     .id(usuarioEmpleado.getId())
//                     .auth0Id(usuarioEmpleado.getAuth0Id())
//                     .username(usuarioEmpleado.getUsername())
//                     .email(usuarioEmpleado.getEmail())
//                     .password(usuarioEmpleado.getPassword())
//                     .nombre(usuarioEmpleado.getNombre())
//                     .apellido(usuarioEmpleado.getApellido())
//                     .telefono(usuarioEmpleado.getTelefono())
//                     .fechaNacimiento(usuarioEmpleado.getFechaNacimiento())
//                     .rol(usuarioEmpleado.getRol())
//                     .estado(usuarioEmpleado.getEstado())
//                     .fechaRegistro(usuarioEmpleado.getFechaRegistro())
//                     .fechaUltimaModificacion(usuarioEmpleado.getFechaUltimaModificacion())
//                     .domicilios(usuarioEmpleado.getDomicilios())
//                     .imagen(usuarioEmpleado.getImagen())
//                     .legajo("C001")
//                     .fechaIngreso(LocalDate.now().minusMonths(6))
//                     .sucursal(sucursal)
//                     .primerLogin(true) // Agregando primerLogin como true para nuevos empleados
//                     .build());

//             // Empleado cajero
//             Usuario usuarioCajero = usuarioRepo.save(Usuario.builder()
//                     .auth0Id("auth0|5")
//                     .username("cajero")
//                     .email("cajero@ejemplo.com")
//                     .password("pass")
//                     .nombre("Ana")
//                     .apellido("Martínez")
//                     .telefono("+545555555")
//                     .fechaNacimiento(LocalDate.of(1993, 4, 12))
//                     .rol(Rol.CAJERO)
//                     .estado(true)
//                     .fechaRegistro(LocalDateTime.now())
//                     .fechaUltimaModificacion(LocalDateTime.now())
//                     .build());
//             Empleado empleadoCajero = empleadoRepo.save(Empleado.builder()
//                     .id(usuarioCajero.getId())
//                     .auth0Id(usuarioCajero.getAuth0Id())
//                     .username(usuarioCajero.getUsername())
//                     .email(usuarioCajero.getEmail())
//                     .password(usuarioCajero.getPassword())
//                     .nombre(usuarioCajero.getNombre())
//                     .apellido(usuarioCajero.getApellido())
//                     .telefono(usuarioCajero.getTelefono())
//                     .fechaNacimiento(usuarioCajero.getFechaNacimiento())
//                     .rol(usuarioCajero.getRol())
//                     .estado(usuarioCajero.getEstado())
//                     .fechaRegistro(usuarioCajero.getFechaRegistro())
//                     .fechaUltimaModificacion(usuarioCajero.getFechaUltimaModificacion())
//                     .domicilios(usuarioCajero.getDomicilios())
//                     .imagen(usuarioCajero.getImagen())
//                     .legajo("CA001")
//                     .fechaIngreso(LocalDate.now().minusMonths(3))
//                     .sucursal(sucursal)
//                     .primerLogin(true) // Agregando primerLogin como true para nuevos empleados
//                     .build());

//             // Empleado delivery
//             Usuario usuarioDelivery = usuarioRepo.save(Usuario.builder()
//                     .auth0Id("auth0|6")
//                     .username("delivery")
//                     .email("delivery@ejemplo.com")
//                     .password("pass")
//                     .nombre("Pedro")
//                     .apellido("Gómez")
//                     .telefono("+546666666")
//                     .fechaNacimiento(LocalDate.of(1997, 8, 25))
//                     .rol(Rol.DELIVERY)
//                     .estado(true)
//                     .fechaRegistro(LocalDateTime.now())
//                     .fechaUltimaModificacion(LocalDateTime.now())
//                     .build());
//             Empleado empleadoDelivery = empleadoRepo.save(Empleado.builder()
//                     .id(usuarioDelivery.getId())
//                     .auth0Id(usuarioDelivery.getAuth0Id())
//                     .username(usuarioDelivery.getUsername())
//                     .email(usuarioDelivery.getEmail())
//                     .password(usuarioDelivery.getPassword())
//                     .nombre(usuarioDelivery.getNombre())
//                     .apellido(usuarioDelivery.getApellido())
//                     .telefono(usuarioDelivery.getTelefono())
//                     .fechaNacimiento(usuarioDelivery.getFechaNacimiento())
//                     .rol(usuarioDelivery.getRol())
//                     .estado(usuarioDelivery.getEstado())
//                     .fechaRegistro(usuarioDelivery.getFechaRegistro())
//                     .fechaUltimaModificacion(usuarioDelivery.getFechaUltimaModificacion())
//                     .domicilios(usuarioDelivery.getDomicilios())
//                     .imagen(usuarioDelivery.getImagen())
//                     .legajo("D001")
//                     .fechaIngreso(LocalDate.now().minusMonths(1))
//                     .sucursal(sucursal)
//                     .primerLogin(true) // Agregando primerLogin como true para el nuevo empleado
//                     .build());

//             // 10. Pedidos, detalles de pedido y facturas
//             // Pedido del Cliente 1
//             Pedido pedidoCliente1 = pedidoRepo.save(Pedido.builder()
//                     .fechaPedido(LocalDate.now().minusDays(2))
//                     .horasEstimadaFinalizacion(LocalTime.of(20, 30))
//                     .total(5600.0) // Hamburguesa + Coca Cola
//                     .totalCosto(4300.0)
//                     .cliente(cliente1)
//                     .sucursal(sucursal) // Vinculamos a la sucursal
//                     .estado(estadoEntregado) // Estado del pedido
//                     .tipoEnvio(tipoDelivery) // Tipo de envío
//                     .formaPago(formaPagoEfectivo) // Forma de pago
//                     .build());

//             // Detalles del pedido del Cliente 1
//             DetallePedido detallePedido1_1 = detallePedidoRepo.save(DetallePedido.builder()
//                     .cantidad(1)
//                     .pedido(pedidoCliente1)
//                     .articulo(hamburguesaClasica)
//                     .build());

//             DetallePedido detallePedido1_2 = detallePedidoRepo.save(DetallePedido.builder()
//                     .cantidad(1)
//                     .pedido(pedidoCliente1)
//                     .articulo(insumoCocaCola)
//                     .build()); // Factura del pedido del Cliente 1
//             Factura facturaCliente1 = facturaRepo.save(Factura.builder()
//                     .fechaFacturacion(LocalDate.now().minusDays(2))
//                     .formaPago(formaPagoEfectivo)
//                     .totalVenta(5600.0)
//                     .pedido(pedidoCliente1)
//                     .build());// Pedido del Cliente 2
//             Pedido pedidoCliente2 = pedidoRepo.save(Pedido.builder()
//                     .fechaPedido(LocalDate.now())
//                     .horasEstimadaFinalizacion(LocalTime.of(21, 15))
//                     .total(10200.0) // 2 pizzas + Agua Mineral
//                     .totalCosto(5000.0)
//                     .cliente(cliente2)
//                     .sucursal(sucursal) // Añadimos la sucursal
//                     .estado(estadoConfirmado) // Estado del pedido
//                     .tipoEnvio(tipoTakeAway) // Tipo de envío
//                     .formaPago(formaPagoMercadoPago) // Forma de pago
//                     .build());

//             // Detalles del pedido del Cliente 2
//             DetallePedido detallePedido2_1 = detallePedidoRepo.save(DetallePedido.builder()
//                     .cantidad(2)
//                     .pedido(pedidoCliente2)
//                     .articulo(pizzaMozzarella)
//                     .build());

//             DetallePedido detallePedido2_2 = detallePedidoRepo.save(DetallePedido.builder()
//                     .cantidad(1)
//                     .pedido(pedidoCliente2)
//                     .articulo(insumoAguaMineral)
//                     .build()); // Factura del pedido del Cliente 2 con datos de Mercado Pago
//             Factura facturaCliente2 = facturaRepo.save(Factura.builder()
//                     .fechaFacturacion(LocalDate.now())
//                     .mpPaymentId(123456789)
//                     .mpMerchantOrderId(987654321)
//                     .mpPreferenceId("pref_123456789")
//                     .mpPaymentType("credit_card")
//                     .formaPago(formaPagoMercadoPago)
//                     .totalVenta(10200.0)
//                     .pedido(pedidoCliente2)
//                     .build());
//             // Datos de Mercado Pago para la factura del Cliente 2
//             DatosMercadoPago datosMPCliente2 = datosMPRepo.save(DatosMercadoPago.builder()
//                     .dateCreate(LocalDate.now())
//                     .dateApproved(LocalDate.now())
//                     .dateLastUpdate(LocalDate.now())
//                     .paymentType("credit_card")
//                     .paymentMethod("mastercard")
//                     .status("approved")
//                     .statusDetail("accredited")
//                     .factura(facturaCliente2)
//                     .build());

//             // 11. Promociones
//             LocalDate fechaInicio = LocalDate.now();
//             LocalDate fechaFin = LocalDate.now().plusDays(30);

//             // Promoción hamburguesa + gaseosa (combo)
//             Promocion promocionComboHamburguesa = promocionRepo.save(Promocion.builder()
//                     .denominacion("Combo Hamburguesa + Bebida")
//                     .fechaDesde(fechaInicio)
//                     .fechaHasta(fechaFin)
//                     .horaDesde(LocalTime.of(10, 0))
//                     .horaHasta(LocalTime.of(20, 0))
//                     .precioPromocional(5040.0) // 10% de descuento sobre 5600 (3800 + 1800)
//                     .articulo(hamburguesaClasica)
//                     .build());

//             // Promoción 2x1 en pizzas
//             Promocion promocion2x1Pizzas = promocionRepo.save(Promocion.builder()
//                     .denominacion("2x1 en Pizzas")
//                     .fechaDesde(fechaInicio)
//                     .fechaHasta(fechaFin)
//                     .horaDesde(LocalTime.of(10, 0))
//                     .horaHasta(LocalTime.of(20, 0))
//                     .precioPromocional(4500.0) // Precio de una sola pizza
//                     .articulo(pizzaMozzarella)
//                     .build());

//             // 12. Historial de Pedidos
//             // Historial para el pedido del Cliente 1
//             HistorialPedido historialPedido1 = historialPedidoRepo.save(HistorialPedido.builder()
//                     .cliente(cliente1)
//                     .pedido(pedidoCliente1)
//                     .fechaRegistro(LocalDateTime.now().minusDays(2))
//                     .observacion("Pedido entregado correctamente")
//                     .build());

//             // Historial para el pedido del Cliente 2
//             HistorialPedido historialPedido2 = historialPedidoRepo.save(HistorialPedido.builder()
//                     .cliente(cliente2)
//                     .pedido(pedidoCliente2)
//                     .fechaRegistro(LocalDateTime.now())
//                     .observacion("Pedido confirmado, en espera de preparación")
//                     .build());

//             // Historial adicional para cambio de estado del Pedido 2
//             HistorialPedido historialPedido2EnPreparacion = historialPedidoRepo
//                     .save(HistorialPedido.builder()
//                             .cliente(cliente2)
//                             .pedido(pedidoCliente2)
//                             .fechaRegistro(LocalDateTime.now().plusHours(1))
//                             .observacion("Pedido en preparación")
//                             .build());

//             // Historial adicional para Cliente 1 (pedido anterior)
//             HistorialPedido historialPedidoAnterior = historialPedidoRepo.save(HistorialPedido.builder()
//                     .cliente(cliente1)
//                     .pedido(pedidoCliente1)
//                     .fechaRegistro(LocalDateTime.now().minusDays(15))
//                     .observacion("Historial de pedido anterior del cliente")
//                     .build());
//             // --- Pedidos de prueba para KANBAN MODULO COCINA ---
//             for (int i = 1; i <= 4; i++) {
//                 Pedido pedidoPendiente = pedidoRepo.save(Pedido.builder()
//                         .estado(estadoPendiente)
//                         .cliente(i % 2 == 0 ? cliente1 : cliente2)
//                         .sucursal(sucursal)
//                         .fechaPedido(LocalDate.now())
//                         .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(30 + i * 5))
//                         .tipoEnvio(i % 2 == 0 ? tipoDelivery : tipoLocal)
//                         .formaPago(i % 2 == 0 ? formaPagoEfectivo : formaPagoTarjeta)
//                         .build());
//                 detallePedidoRepo.save(DetallePedido.builder()
//                         .pedido(pedidoPendiente)
//                         .cantidad(1 + i % 2)
//                         .articulo(pizzaMozzarella)
//                         .build());
//             }

//             for (int i = 1; i <= 2; i++) {
//                 Pedido pedidoEnProceso = pedidoRepo.save(Pedido.builder()
//                         .estado(estadoEnPreparacion)
//                         .cliente(i % 2 == 0 ? cliente1 : cliente2)
//                         .sucursal(sucursal)
//                         .fechaPedido(LocalDate.now())
//                         .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(40 + i * 5))
//                         .tipoEnvio(tipoTakeAway)
//                         .formaPago(formaPagoMercadoPago)
//                         .build());
//                 detallePedidoRepo.save(DetallePedido.builder()
//                         .pedido(pedidoEnProceso)
//                         .cantidad(2)
//                         .articulo(hamburguesaBbq)
//                         .build());
//             }

//             for (int i = 1; i <= 2; i++) {
//                 Pedido pedidoDemorado = pedidoRepo.save(Pedido.builder()
//                         .estado(estadoListo) // Cambia a estadoDemorado si tienes uno, si no usa LISTO para simular
//                         .cliente(i % 2 == 0 ? cliente1 : cliente2)
//                         .sucursal(sucursal)
//                         .fechaPedido(LocalDate.now())
//                         .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(60 + i * 5))
//                         .tipoEnvio(tipoDelivery)
//                         .formaPago(formaPagoTransferencia)
//                         .build());
//                 detallePedidoRepo.save(DetallePedido.builder()
//                         .pedido(pedidoDemorado)
//                         .cantidad(1)
//                         .articulo(pizzaPepperoni)
//                         .build());
//             }

// // Si tienes un estado DEMORADO, usa estadoDemorado, si no, crea uno arriba y úsalo aquí
//             Estado estadoDemorado = estadoRepo.findByNombre("DEMORADO")
//                     .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("DEMORADO").build()));
//             for (int i = 1; i <= 2; i++) {
//                 Pedido pedidoDemorado = pedidoRepo.save(Pedido.builder()
//                         .estado(estadoDemorado)
//                         .cliente(i % 2 == 0 ? cliente1 : cliente2)
//                         .sucursal(sucursal)
//                         .fechaPedido(LocalDate.now())
//                         .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(70 + i * 5))
//                         .tipoEnvio(tipoTakeAway)
//                         .formaPago(formaPagoEfectivo)
//                         .build());
//                 detallePedidoRepo.save(DetallePedido.builder()
//                         .pedido(pedidoDemorado)
//                         .cantidad(1)
//                         .articulo(hamburguesaVegetariana)
//                         .build());
//             }

//             for (int i = 1; i <= 3; i++) {
//                 Pedido pedidoListo = pedidoRepo.save(Pedido.builder()
//                         .estado(estadoListo)
//                         .cliente(i % 2 == 0 ? cliente1 : cliente2)
//                         .sucursal(sucursal)
//                         .fechaPedido(LocalDate.now())
//                         .horasEstimadaFinalizacion(LocalTime.now().plusMinutes(80 + i * 5))
//                         .tipoEnvio(tipoLocal)
//                         .formaPago(formaPagoTarjeta)
//                         .build());
//                 detallePedidoRepo.save(DetallePedido.builder()
//                         .pedido(pedidoListo)
//                         .cantidad(1)
//                         .articulo(pizzaCuatroQuesos)
//                         .build());
//             }

//             //  Verificación: listar y mostrar entidades para probar interacciones
//              System.out.println("=== Verificación de datos cargados ===");
//              System.out.println("Paises: " + paisRepo.findAll());
//              System.out.println("Provincias: " + provinciaRepo.findAll());
//              System.out.println("Localidades: " + localidadRepo.findAll());
//              System.out.println("Usuarios: " + usuarioRepo.findAll());
//              System.out.println("Empresas: " + empresaRepo.findAll());
//              System.out.println("Domicilios: " + domicilioRepo.findAll());
//              System.out.println("Sucursales: " + sucursalRepo.findAll());
//              System.out.println("UnidadMedida: " + unidadMedidaRepo.findAll());
//              System.out.println("Categorias: " + categoriaRepo.findAll());
//              System.out.println("Imagenes: " + imagenRepo.findAll());
//              System.out.println("Insumos: " + articuloInsumoRepo.findAll());
//              System.out.println("Manufacturados: " + articuloManufacturadoRepo.findAll());
//              System.out.println("Detalles Manufacturado: " + amdRepo.findAll());
//              System.out.println("Promociones: " + promocionRepo.findAll());
//              System.out.println("Clientes: " + clienteRepo.findAll());
//              System.out.println("Pedidos: " + pedidoRepo.findAll());
//              System.out.println("DetallePedido: " + detallePedidoRepo.findAll());
//              System.out.println("Facturas: " + facturaRepo.findAll());
//              System.out.println("Datos MercadoPago: " + datosMPRepo.findAll());
//              System.out.println("Horarios: " + horarioRepo.findAll());
//         };
//     }
// }