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
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
            ArticuloManufacturadoRepository articuloManufacturadoRepo,
            ArticuloManufacturadoDetalleRepository amdRepo,
            PromocionRepository promocionRepo,
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
            HistorialPedidoRepository historialPedidoRepo) {
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
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("CONFIRMADO").build()));
            Estado estadoEnPreparacion = estadoRepo.findByNombre("EN_PREPARACION")
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("EN_PREPARACION").build()));
            Estado estadoListo = estadoRepo.findByNombre("LISTO")
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("LISTO").build()));
            Estado estadoEnDelivery = estadoRepo.findByNombre("EN_DELIVERY")
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("EN_DELIVERY").build()));
            Estado estadoEntregado = estadoRepo.findByNombre("ENTREGADO")
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("ENTREGADO").build()));
            Estado estadoCancelado = estadoRepo.findByNombre("CANCELADO")
                    .orElseGet(() -> estadoRepo.save(Estado.builder().nombre("CANCELADO").build())); // Inicializar
                                                                                                     // Tipos de Envío
            TipoEnvio tipoDelivery = tipoEnvioRepo.findByNombre("DELIVERY")
                    .orElseGet(() -> tipoEnvioRepo.save(TipoEnvio.builder().nombre("DELIVERY").build()));
            TipoEnvio tipoTakeAway = tipoEnvioRepo.findByNombre("TAKE_AWAY")
                    .orElseGet(() -> tipoEnvioRepo.save(TipoEnvio.builder().nombre("TAKE_AWAY").build()));
            TipoEnvio tipoLocal = tipoEnvioRepo.findByNombre("EN_LOCAL")
                    .orElseGet(() -> tipoEnvioRepo.save(TipoEnvio.builder().nombre("EN_LOCAL").build())); // Inicializar
                                                                                                          // Formas de
                                                                                                          // Pago
            FormaPago formaPagoEfectivo = formaPagoRepo.findByNombre("EFECTIVO")
                    .orElseGet(() -> formaPagoRepo.save(FormaPago.builder().nombre("EFECTIVO").build()));
            FormaPago formaPagoTarjeta = formaPagoRepo.findByNombre("TARJETA")
                    .orElseGet(() -> formaPagoRepo.save(FormaPago.builder().nombre("TARJETA").build()));
            FormaPago formaPagoMercadoPago = formaPagoRepo.findByNombre("MERCADO_PAGO")
                    .orElseGet(() -> formaPagoRepo.save(FormaPago.builder().nombre("MERCADO_PAGO").build()));
            FormaPago formaPagoTransferencia = formaPagoRepo.findByNombre("TRANSFERENCIA")
                    .orElseGet(() -> formaPagoRepo.save(FormaPago.builder().nombre("TRANSFERENCIA").build()));
            // 4. Catálogo de productos - Creamos las categorías principales y subcategorías
            // -- Categorías principales
            Categoria categoriaSandwiches = categoriaRepo.save(
                    Categoria.builder().denominacion("Sandwiches").sucursal(sucursal).build());
            Categoria categoriaPizzas = categoriaRepo.save(
                    Categoria.builder().denominacion("Pizzas").sucursal(sucursal).build());
            Categoria categoriaBebidas = categoriaRepo.save(
                    Categoria.builder().denominacion("Bebidas").sucursal(sucursal).build());
            Categoria categoriaInsumos = categoriaRepo.save(
                    Categoria.builder().denominacion("Insumos").sucursal(sucursal).build());

            // -- Subcategorías
            // Subcategorías de Sandwiches
            Categoria categoriaHamburguesas = categoriaRepo.save(
                    Categoria.builder().denominacion("Hamburguesas")
                            .tipoCategoria(categoriaSandwiches)
                            .sucursal(sucursal).build());
            Categoria categoriaLomos = categoriaRepo.save(
                    Categoria.builder().denominacion("Lomos")
                            .tipoCategoria(categoriaSandwiches)
                            .sucursal(sucursal).build());

            // Subcategorías de Bebidas
            Categoria categoriaGaseosas = categoriaRepo.save(
                    Categoria.builder().denominacion("Gaseosas")
                            .tipoCategoria(categoriaBebidas)
                            .sucursal(sucursal).build());
            Categoria categoriaAguas = categoriaRepo.save(
                    Categoria.builder().denominacion("Aguas")
                            .tipoCategoria(categoriaBebidas)
                            .sucursal(sucursal).build());
            Categoria categoriaTragos = categoriaRepo.save(
                    Categoria.builder().denominacion("Tragos")
                            .tipoCategoria(categoriaBebidas)
                            .sucursal(sucursal).build());

            // 5. Imágenes para cada tipo de producto
            Imagen imagenHamburguesa = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/hamburguesa.png")
                            .build());
            Imagen imagenLomo = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/lomo.png")
                            .build());
            Imagen imagenPizza = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/pizza.png")
                            .build());
            Imagen imagenGaseosa = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/gaseosa.png")
                            .build());
            Imagen imagenAgua = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/agua.png")
                            .build());
            Imagen imagenTrago = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/trago.png")
                            .build());
            Imagen imagenDefault = imagenRepo
                    .save(Imagen.builder().url("http://ejemplo.com/default.png")
                            .build());

            // 6. Unidades de medida
            UnidadMedida unidadGramos = unidadMedidaRepo
                    .save(UnidadMedida.builder().denominacion("Gramos").build());
            UnidadMedida unidadLitros = unidadMedidaRepo
                    .save(UnidadMedida.builder().denominacion("Litros").build());
            UnidadMedida unidadUnidad = unidadMedidaRepo
                    .save(UnidadMedida.builder().denominacion("Unidad").build());

            // 5. Creamos artículos insumo (ingredientes y productos no elaborados)
            // -- Ingredientes para elaboración (esParaElaborar = true)
            ArticuloInsumo insumoHarina = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Harina 000")
                    .precioVenta(1000.0)
                    .categoria(categoriaPizzas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(700.0)
                    .stockActual(10000)
                    .stockMaximo(20000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoQueso = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Queso Mozzarella")
                    .precioVenta(2500.0)
                    .categoria(categoriaPizzas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(2000.0)
                    .stockActual(5000)
                    .stockMaximo(8000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoCarne = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Carne Molida Premium")
                    .precioVenta(3500.0)
                    .categoria(categoriaHamburguesas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(3000.0)
                    .stockActual(4000)
                    .stockMaximo(7000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoPan = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Pan de Hamburguesa")
                    .precioVenta(1500.0)
                    .categoria(categoriaHamburguesas)
                    .unidadMedida(unidadUnidad)
                    .precioCompra(1200.0)
                    .stockActual(100)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoLechuga = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Lechuga Fresca")
                    .precioVenta(800.0)
                    .categoria(categoriaHamburguesas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(600.0)
                    .stockActual(2000)
                    .stockMaximo(3000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoTomate = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Tomate")
                    .precioVenta(900.0)
                    .categoria(categoriaHamburguesas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(700.0)
                    .stockActual(3000)
                    .stockMaximo(5000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoCebolla = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Cebolla")
                    .precioVenta(600.0)
                    .categoria(categoriaHamburguesas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(400.0)
                    .stockActual(2500)
                    .stockMaximo(4000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoPanLomo = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Pan de Lomo")
                    .precioVenta(1700.0)
                    .categoria(categoriaLomos)
                    .unidadMedida(unidadUnidad)
                    .precioCompra(1400.0)
                    .stockActual(80)
                    .stockMaximo(150)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoCarneLomo = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Bife de Lomo")
                    .precioVenta(5000.0)
                    .categoria(categoriaLomos)
                    .unidadMedida(unidadGramos)
                    .precioCompra(4500.0)
                    .stockActual(3000)
                    .stockMaximo(5000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            ArticuloInsumo insumoSalsaTomate = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Salsa de Tomate")
                    .precioVenta(1200.0)
                    .categoria(categoriaPizzas)
                    .unidadMedida(unidadGramos)
                    .precioCompra(900.0)
                    .stockActual(4000)
                    .stockMaximo(6000)
                    .esParaElaborar(true)
                    .imagen(imagenDefault)
                    .build());

            // -- Productos terminados que no se elaboran (esParaElaborar = false)
            ArticuloInsumo insumoCocaCola = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Coca Cola 500ml")
                    .precioVenta(1800.0)
                    .categoria(categoriaGaseosas)
                    .unidadMedida(unidadLitros)
                    .precioCompra(1300.0)
                    .stockActual(200)
                    .stockMaximo(300)
                    .esParaElaborar(false)
                    .imagen(imagenGaseosa)
                    .build());

            ArticuloInsumo insumoAguaMineral = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Agua Mineral 500ml")
                    .precioVenta(1200.0)
                    .categoria(categoriaAguas)
                    .unidadMedida(unidadLitros)
                    .precioCompra(800.0)
                    .stockActual(150)
                    .stockMaximo(250)
                    .esParaElaborar(false)
                    .imagen(imagenAgua)
                    .build());

            ArticuloInsumo insumoFernet = articuloInsumoRepo.save(ArticuloInsumo.builder()
                    .denominacion("Fernet 750ml")
                    .precioVenta(6000.0)
                    .categoria(categoriaTragos)
                    .unidadMedida(unidadLitros)
                    .precioCompra(4500.0)
                    .stockActual(50)
                    .stockMaximo(100)
                    .esParaElaborar(false)
                    .imagen(imagenTrago)
                    .build());

            // 7. Productos manufacturados (hamburguesas, pizzas, etc)
            // Pizza Mozzarella
            ArticuloManufacturado pizzaMozzarella = articuloManufacturadoRepo
                    .save(ArticuloManufacturado.builder()
                            .denominacion("Pizza Mozzarella")
                            .descripcion("Pizza con queso mozzarella y salsa de tomate")
                            .tiempoEstimadoMinutos(20)
                            .precioVenta(4500.0)
                            .categoria(categoriaPizzas)
                            .imagen(imagenPizza)
                            .build());

            // Detalles de Pizza Mozzarella
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

            // Hamburguesa Clásica
            ArticuloManufacturado hamburguesaClasica = articuloManufacturadoRepo
                    .save(ArticuloManufacturado.builder()
                            .denominacion("Hamburguesa Clásica")
                            .descripcion("Hamburguesa con lechuga, tomate y cebolla")
                            .tiempoEstimadoMinutos(15)
                            .precioVenta(3800.0)
                            .categoria(categoriaHamburguesas)
                            .imagen(imagenHamburguesa)
                            .build());

            // Detalles de Hamburguesa Clásica
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

            // Lomo Completo
            ArticuloManufacturado lomoCompleto = articuloManufacturadoRepo
                    .save(ArticuloManufacturado.builder()
                            .denominacion("Lomo Completo")
                            .descripcion("Lomo con jamón, queso, lechuga y tomate")
                            .tiempoEstimadoMinutos(20)
                            .precioVenta(5500.0)
                            .categoria(categoriaLomos)
                            .imagen(imagenLomo)
                            .build());

            // Detalles de Lomo Completo
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

            // 8. Usuarios con diferentes roles
            // Usuario empleado
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

            // Usuario cliente (para el sistema)
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

            Usuario usuarioCliente2 = usuarioRepo.save(Usuario.builder()
                    .auth0Id("auth0|4")
                    .username("cliente2")
                    .email("cliente2@ejemplo.com")
                    .password("pass")
                    .nombre("Carlos")
                    .apellido("Rodríguez")
                    .telefono("+544444444")
                    .fechaNacimiento(LocalDate.of(1988, 3, 10))
                    .rol(Rol.CLIENTE)
                    .estado(true).fechaRegistro(LocalDateTime.now())
                    .fechaUltimaModificacion(LocalDateTime.now())
                    .build());

            // 9. Clientes (entidad Cliente, relacionada con Usuario)
            // Crear domicilios para clientes
            Domicilio domicilioCliente1 = domicilioRepo.save(Domicilio.builder()
                    .calle("Belgrano")
                    .numero(456)
                    .cp("5500")
                    .usuario(usuarioCliente1)
                    .localidad(localidad)
                    .build());

            Domicilio domicilioCliente2 = domicilioRepo.save(Domicilio.builder()
                    .calle("Colón")
                    .numero(789)
                    .cp("5500")
                    .usuario(usuarioCliente2)
                    .localidad(localidad)
                    .build());

            // Crear entidades Cliente
            Cliente cliente1 = clienteRepo.save(Cliente.builder()
                    .usuario(usuarioCliente1)
                    .build());

            Cliente cliente2 = clienteRepo.save(Cliente.builder()
                    .usuario(usuarioCliente2)
                    .build()); // 9. Creación de empleados con diferentes roles
            // Empleado cocinero
            Empleado empleadoCocinero = empleadoRepo.save(Empleado.builder()
                    .legajo("C001")
                    .fechaIngreso(LocalDate.now().minusMonths(6))
                    .sucursal(sucursal)
                    .usuario(usuarioEmpleado)
                    .build());

            // Empleado cajero
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
                    .legajo("CA001")
                    .fechaIngreso(LocalDate.now().minusMonths(3))
                    .sucursal(sucursal)
                    .usuario(usuarioCajero)
                    .build());

            // Empleado delivery
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
                    .legajo("D001")
                    .fechaIngreso(LocalDate.now().minusMonths(1))
                    .sucursal(sucursal)
                    .usuario(usuarioDelivery)
                    .build());

            // 10. Pedidos, detalles de pedido y facturas
            // Pedido del Cliente 1
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

            // Detalles del pedido del Cliente 1
            DetallePedido detallePedido1_1 = detallePedidoRepo.save(DetallePedido.builder()
                    .cantidad(1)
                    .pedido(pedidoCliente1)
                    .articulo(hamburguesaClasica)
                    .build());

            DetallePedido detallePedido1_2 = detallePedidoRepo.save(DetallePedido.builder()
                    .cantidad(1)
                    .pedido(pedidoCliente1)
                    .articulo(insumoCocaCola)
                    .build()); // Factura del pedido del Cliente 1
            Factura facturaCliente1 = facturaRepo.save(Factura.builder()
                    .fechaFacturacion(LocalDate.now().minusDays(2))
                    .formaPago(formaPagoEfectivo)
                    .totalVenta(5600.0)
                    .pedido(pedidoCliente1)
                    .build());// Pedido del Cliente 2
            Pedido pedidoCliente2 = pedidoRepo.save(Pedido.builder()
                    .fechaPedido(LocalDate.now())
                    .horasEstimadaFinalizacion(LocalTime.of(21, 15))
                    .total(10200.0) // 2 pizzas + Agua Mineral
                    .totalCosto(5000.0)
                    .cliente(cliente2)
                    .sucursal(sucursal) // Añadimos la sucursal
                    .estado(estadoConfirmado) // Estado del pedido
                    .tipoEnvio(tipoTakeAway) // Tipo de envío
                    .formaPago(formaPagoMercadoPago) // Forma de pago
                    .build());

            // Detalles del pedido del Cliente 2
            DetallePedido detallePedido2_1 = detallePedidoRepo.save(DetallePedido.builder()
                    .cantidad(2)
                    .pedido(pedidoCliente2)
                    .articulo(pizzaMozzarella)
                    .build());

            DetallePedido detallePedido2_2 = detallePedidoRepo.save(DetallePedido.builder()
                    .cantidad(1)
                    .pedido(pedidoCliente2)
                    .articulo(insumoAguaMineral)
                    .build()); // Factura del pedido del Cliente 2 con datos de Mercado Pago
            Factura facturaCliente2 = facturaRepo.save(Factura.builder()
                    .fechaFacturacion(LocalDate.now())
                    .mpPaymentId(123456789)
                    .mpMerchantOrderId(987654321)
                    .mpPreferenceId("pref_123456789")
                    .mpPaymentType("credit_card")
                    .formaPago(formaPagoMercadoPago)
                    .totalVenta(10200.0)
                    .pedido(pedidoCliente2)
                    .build());
            // Datos de Mercado Pago para la factura del Cliente 2
            DatosMercadoPago datosMPCliente2 = datosMPRepo.save(DatosMercadoPago.builder()
                    .dateCreate(LocalDate.now())
                    .dateApproved(LocalDate.now())
                    .dateLastUpdate(LocalDate.now())
                    .paymentType("credit_card")
                    .paymentMethod("mastercard")
                    .status("approved")
                    .statusDetail("accredited")
                    .factura(facturaCliente2)
                    .build());

            // 11. Promociones
            LocalDate fechaInicio = LocalDate.now();
            LocalDate fechaFin = LocalDate.now().plusDays(30);

            // Promoción hamburguesa + gaseosa (combo)
            Promocion promocionComboHamburguesa = promocionRepo.save(Promocion.builder()
                    .denominacion("Combo Hamburguesa + Bebida")
                    .fechaDesde(fechaInicio)
                    .fechaHasta(fechaFin)
                    .horaDesde(LocalTime.of(10, 0))
                    .horaHasta(LocalTime.of(20, 0))
                    .precioPromocional(5040.0) // 10% de descuento sobre 5600 (3800 + 1800)
                    .articulo(hamburguesaClasica)
                    .build());

            // Promoción 2x1 en pizzas
            Promocion promocion2x1Pizzas = promocionRepo.save(Promocion.builder()
                    .denominacion("2x1 en Pizzas")
                    .fechaDesde(fechaInicio)
                    .fechaHasta(fechaFin)
                    .horaDesde(LocalTime.of(10, 0))
                    .horaHasta(LocalTime.of(20, 0))
                    .precioPromocional(4500.0) // Precio de una sola pizza
                    .articulo(pizzaMozzarella)
                    .build());

            // 12. Historial de Pedidos
            // Historial para el pedido del Cliente 1
            HistorialPedido historialPedido1 = historialPedidoRepo.save(HistorialPedido.builder()
                    .cliente(cliente1)
                    .pedido(pedidoCliente1)
                    .fechaRegistro(LocalDateTime.now().minusDays(2))
                    .observacion("Pedido entregado correctamente")
                    .build());

            // Historial para el pedido del Cliente 2
            HistorialPedido historialPedido2 = historialPedidoRepo.save(HistorialPedido.builder()
                    .cliente(cliente2)
                    .pedido(pedidoCliente2)
                    .fechaRegistro(LocalDateTime.now())
                    .observacion("Pedido confirmado, en espera de preparación")
                    .build());

            // Historial adicional para cambio de estado del Pedido 2
            HistorialPedido historialPedido2EnPreparacion = historialPedidoRepo.save(HistorialPedido.builder()
                    .cliente(cliente2)
                    .pedido(pedidoCliente2)
                    .fechaRegistro(LocalDateTime.now().plusHours(1))
                    .observacion("Pedido en preparación")
                    .build());

            // Historial adicional para Cliente 1 (pedido anterior)
            HistorialPedido historialPedidoAnterior = historialPedidoRepo.save(HistorialPedido.builder()
                    .cliente(cliente1)
                    .pedido(pedidoCliente1)
                    .fechaRegistro(LocalDateTime.now().minusDays(15))
                    .observacion("Historial de pedido anterior del cliente")
                    .build());

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