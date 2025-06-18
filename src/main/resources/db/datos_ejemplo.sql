-- #################################################################################################
-- #                                                                                               #
-- #                      SCRIPT DE INSERCIÓN DE DATOS DE EJEMPLO - EL SABORCITO                     #
-- #                                                                                               #
-- #################################################################################################

-- Seleccionar la base de datos a utilizar
USE saborcito_db;

-- Deshabilitar la verificación de claves foráneas para la carga masiva
SET FOREIGN_KEY_CHECKS=0;

-- Iniciar una transacción para asegurar la atomicidad de la operación
START TRANSACTION;

-- =================================================================================================
--  sección 1: DATOS GEOGRÁFICOS
-- =================================================================================================
-- Insertar país
INSERT INTO pais (id, nombre) VALUES (1, 'Argentina');

-- Insertar provincias
INSERT INTO provincia (id, nombre, pais_id) VALUES (1, 'Mendoza', 1), (2, 'San Juan', 1), (3, 'San Luis', 1);

-- Insertar localidades
INSERT INTO localidad (id, nombre, alta, provincia_id) VALUES 
(1, 'Mendoza', 1, 1), 
(2, 'Godoy Cruz', 1, 1), 
(3, 'Guaymallén', 1, 1),
(4, 'San Rafael', 1, 1),
(5, 'San Martín', 1, 1);

-- =================================================================================================
-- sección 2: EMPRESA Y SUCURSAL
-- =================================================================================================
-- Insertar empresa
INSERT INTO empresa (id, nombre, razon_social, cuil, alta) VALUES (1, 'El Saborcito', 'El Saborcito S.A.', 30123456789, 1);

-- Insertar domicilio para la sucursal
INSERT INTO domicilio (id, calle, numero, cp, piso, nro_depto, alta, latitud, longitud, localidad_id) VALUES (1, 'Av. San Martín', 900, 5500, 0, 0, 1, -32.889458, -68.845839, 1);

-- Insertar sucursal
INSERT INTO sucursal (id, nombre, horario_apertura, horario_cierre, alta, empresa_id, domicilio_id) VALUES (1, 'Sucursal Centro', '10:00:00', '23:30:00', 1, 1, 1);

-- =================================================================================================
-- sección 3: DATOS MAESTROS (ROLES, UNIDADES, CATEGORÍAS, ESTADOS, ETC.)
-- =================================================================================================
-- Insertar roles
INSERT INTO rol (id, denominacion, alta) VALUES 
(1, 'ADMIN', 1), 
(2, 'CLIENTE', 1), 
(3, 'COCINERO', 1), 
(4, 'CAJERO', 1), 
(5, 'DELIVERY', 1);

-- Insertar unidades de medida
INSERT INTO unidad_medida (id, denominacion, alta) VALUES 
(1, 'Gramos', 1), 
(2, 'Kilogramos', 1), 
(3, 'Litros', 1), 
(4, 'Mililitros', 1), 
(5, 'Unidad', 1);

-- Insertar estados de pedido
INSERT INTO estado (id, nombre) VALUES
(1, 'A_CONFIRMAR'),
(2, 'EN_PREPARACION'),
(3, 'DEMORADO'),
(4, 'LISTO'),
(5, 'EN_DELIVERY'),
(6, 'ENTREGADO'),
(7, 'CANCELADO');

-- Insertar tipos de envío
INSERT INTO tipo_envio (id, nombre) VALUES
(1, 'DELIVERY'),
(2, 'TAKE_AWAY');

-- Insertar formas de pago
INSERT INTO forma_pago (id, nombre) VALUES
(1, 'EFECTIVO'),
(2, 'MERCADO_PAGO');

-- Insertar categorías (padres primero)
INSERT INTO categoria (id, denominacion, tipo_categoria, alta, categoria_padre_id) VALUES 
(1, 'Insumos', 'INSUMO', 1, NULL),
(2, 'Bebidas', 'INSUMO', 1, NULL),
(3, 'Sandwiches', 'MANUFACTURADO', 1, NULL),
(4, 'Pizzas', 'MANUFACTURADO', 1, NULL),
(5, 'Lomos', 'MANUFACTURADO', 3, NULL),
(6, 'Hamburguesas', 'MANUFACTURADO', 3, NULL),
(7, 'Guarniciones', 'MANUFACTURADO', 1, NULL);

-- Insertar subcategorías
INSERT INTO categoria (id, denominacion, tipo_categoria, alta, categoria_padre_id) VALUES 
(8, 'Carnes', 'INSUMO', 1, 1),
(9, 'Vegetales', 'INSUMO', 1, 1),
(10, 'Panificados', 'INSUMO', 1, 1),
(11, 'Lácteos', 'INSUMO', 1, 1),
(12, 'Aderezos', 'INSUMO', 1, 1),
(13, 'Gaseosas', 'INSUMO', 1, 2),
(14, 'Aguas', 'INSUMO', 1, 2),
(15, 'Cervezas', 'INSUMO', 1, 2),


-- =================================================================================================
-- sección 4: USUARIOS Y PERSONAS (ADMIN, EMPLEADOS, CLIENTES)
-- =================================================================================================
-- 4.1 ADMIN
-- Usuario Administrador
INSERT INTO usuario (id, username, password, alta, rol_id) VALUES (1, 'elsaborcito2024@gmail.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 1);
-- Empleado para el usuario Admin
INSERT INTO empleado (id, nombre, apellido, telefono, email, fecha_nacimiento, alta, usuario_id, sucursal_id) VALUES (1, 'Admin', 'Saborcito', '2611234567', 'elsaborcito2024@gmail.com', '1990-01-01', 1, 1, 1);

-- 4.2 EMPLEADOS (5 en total, 1 admin + 4)
-- Domicilios para empleados
INSERT INTO domicilio (id, calle, numero, cp, alta, latitud, longitud, localidad_id, usuario_id) VALUES
(2, 'Patricias Mendocinas', 550, 5500, 1, -32.8908, -68.8454, 1, 2),
(3, 'Olascoaga', 1280, 5500, 1, -32.8992, -68.8495, 1, 3),
(4, 'Tiburcio Benegas', 789, 5501, 1, -32.8833, -68.8503, 2, 4),
(5, 'Paso de los Andes', 2045, 5501, 1, -32.908, -68.8552, 2, 5);

-- Usuarios para empleados
INSERT INTO usuario (id, username, password, alta, rol_id) VALUES 
(2, 'cocinero1@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 3),
(3, 'cajero1@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 4),
(4, 'delivery1@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 5),
(5, 'cocinero2@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 3);

-- Empleados
INSERT INTO empleado (id, nombre, apellido, telefono, email, fecha_nacimiento, alta, usuario_id, sucursal_id) VALUES 
(2, 'Juan', 'Perez', '2615550101', 'cocinero1@sabor.com', '1995-05-20', 1, 2, 1),
(3, 'Maria', 'Gonzalez', '2615550102', 'cajero1@sabor.com', '1998-11-15', 1, 3, 1),
(4, 'Carlos', 'Rodriguez', '2615550103', 'delivery1@sabor.com', '2000-03-10', 1, 4, 1),
(5, 'Ana', 'Martinez', '2615550104', 'cocinero2@sabor.com', '1992-07-25', 1, 5, 1);

-- 4.3 CLIENTES (15 en total)
-- Domicilios para clientes
INSERT INTO domicilio (id, calle, numero, cp, alta, latitud, longitud, localidad_id, usuario_id) VALUES
(10, 'Las Heras', 450, 5500, 1, -32.8853, -68.8435, 1, 10), (11, '9 de Julio', 1120, 5500, 1, -32.893, -68.84, 1, 11), (12, 'Colón', 330, 5500, 1, -32.8959, -68.8429, 1, 12),
(13, 'Belgrano', 880, 5501, 1, -32.8944, -68.853, 2, 13), (14, 'Rivadavia', 670, 5501, 1, -32.891, -68.855, 2, 14), (15, 'Sarmiento', 150, 5501, 1, -32.888, -68.856, 2, 15),
(16, '25 de Mayo', 220, 5519, 1, -32.88, -68.82, 3, 16), (17, 'Pedro Molina', 940, 5519, 1, -32.885, -68.815, 3, 17), (18, 'Adolfo Calle', 300, 5519, 1, -32.89, -68.81, 3, 18),
(19, 'Av. Mitre', 1800, 5600, 1, -34.61, -68.33, 4, 19), (20, 'Hipólito Yrigoyen', 2500, 5600, 1, -34.615, -68.335, 4, 20), (21, 'El Libertador', 780, 5600, 1, -34.62, -68.34, 4, 21),
(22, 'Alvear', 560, 5570, 1, -33.03, -68.48, 5, 22), (23, 'Boulogne Sur Mer', 430, 5570, 1, -33.035, -68.485, 5, 23), (24, '25 de Mayo', 110, 5570, 1, -33.04, -68.49, 5, 24);

-- Usuarios para clientes
INSERT INTO usuario (id, username, password, alta, rol_id) VALUES 
(10, 'cliente1@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (11, 'cliente2@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(12, 'cliente3@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (13, 'cliente4@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(14, 'cliente5@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (15, 'cliente6@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(16, 'cliente7@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (17, 'cliente8@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(18, 'cliente9@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (19, 'cliente10@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(20, 'cliente11@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (21, 'cliente12@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(22, 'cliente13@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2), (23, 'cliente14@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2),
(24, 'cliente15@sabor.com', '$2a$10$kavL/E63g9LzN.2s7o9p2.p1Q6CgH.e/d5B9zL0j8a7b6c5d4e3f2', 1, 2);

-- Clientes
INSERT INTO cliente (id, nombre, apellido, telefono, email, fecha_nacimiento, alta, usuario_id) VALUES
(1, 'Laura', 'Gomez', '2616111111', 'cliente1@sabor.com', '1992-08-10', 1, 10),
(2, 'Pedro', 'Sanchez', '2616222222', 'cliente2@sabor.com', '1985-12-01', 1, 11),
(3, 'Sofia', 'Diaz', '2616333333', 'cliente3@sabor.com', '2001-04-22', 1, 12),
(4, 'Martin', 'Fernandez', '2616444444', 'cliente4@sabor.com', '1999-06-30', 1, 13),
(5, 'Valentina', 'Lopez', '2616555555', 'cliente5@sabor.com', '1993-02-18', 1, 14),
(6, 'Lucas', 'Torres', '2616666666', 'cliente6@sabor.com', '1997-09-05', 1, 15),
(7, 'Camila', 'Romero', '2616777777', 'cliente7@sabor.com', '1994-07-12', 1, 16),
(8, 'Mateo', 'Suarez', '2616888888', 'cliente8@sabor.com', '1988-10-25', 1, 17),
(9, 'Isabella', 'Castro', '2616999999', 'cliente9@sabor.com', '2003-01-08', 1, 18),
(10, 'Bautista', 'Gimenez', '2617000000', 'cliente10@sabor.com', '1996-03-14', 1, 19),
(11, 'Julieta', 'Rojas', '2617111111', 'cliente11@sabor.com', '1991-11-28', 1, 20),
(12, 'Daniel', 'Acosta', '2617222222', 'cliente12@sabor.com', '1980-05-16', 1, 21),
(13, 'Paula', 'Correa', '2617333333', 'cliente13@sabor.com', '2000-02-20', 1, 22),
(14, 'Felipe', 'Moreno', '2617444444', 'cliente14@sabor.com', '1995-04-03', 1, 23),
(15, 'Agustina', 'Sosa', '2617555555', 'cliente15@sabor.com', '1998-08-19', 1, 24);


-- =================================================================================================
-- sección 5: IMÁGENES PARA ARTÍCULOS
-- =================================================================================================
-- Se insertan primero todas las imágenes para poder referenciarlas por ID
INSERT INTO imagen (id, url, eliminado) VALUES
(1, 'https://i.imgur.com/carneh.jpeg', 0), (2, 'https://i.imgur.com/lomo.jpeg', 0), (3, 'https://i.imgur.com/pollo.jpeg', 0), (4, 'https://i.imgur.com/salchicha.jpeg', 0), (5, 'https://i.imgur.com/jamon.jpeg', 0),
(6, 'https://i.imgur.com/lechuga.jpeg', 0), (7, 'https://i.imgur.com/tomate.jpeg', 0), (8, 'https://i.imgur.com/cebolla.jpeg', 0), (9, 'https://i.imgur.com/pimiento.jpeg', 0), (10, 'https://i.imgur.com/huevo.jpeg', 0),
(11, 'https://i.imgur.com/papas.jpeg', 0), (12, 'https://i.imgur.com/aceitunas.jpeg', 0), (13, 'https://i.imgur.com/panh.jpeg', 0), (14, 'https://i.imgur.com/panl.jpeg', 0), (15, 'https://i.imgur.com/pizzam.jpeg', 0),
(16, 'https://i.imgur.com/panp.jpeg', 0), (17, 'https://i.imgur.com/cheddar.jpeg', 0), (18, 'https://i.imgur.com/mozzarella.jpeg', 0), (19, 'https://i.imgur.com/quesot.jpeg', 0), (20, 'https://i.imgur.com/mayonesa.jpeg', 0),
(21, 'https://i.imgur.com/ketchup.jpeg', 0), (22, 'https://i.imgur.com/mostaza.jpeg', 0), (23, 'https://i.imgur.com/salsat.jpeg', 0), (24, 'https://i.imgur.com/salsag.jpeg', 0), (25, 'https://i.imgur.com/oregano.jpeg', 0),
(26, 'https://i.imgur.com/sal.jpeg', 0), (27, 'https://i.imgur.com/pimienta.jpeg', 0), (28, 'https://i.imgur.com/aceite.jpeg', 0), (29, 'https://i.imgur.com/vinagre.jpeg', 0), (30, 'https://i.imgur.com/harina.jpeg', 0),
(31, 'https://i.imgur.com/levadura.jpeg', 0), (32, 'https://i.imgur.com/azucar.jpeg', 0), (33, 'https://i.imgur.com/cebollac.jpeg', 0), (34, 'https://i.imgur.com/panceta.jpeg', 0), (35, 'https://i.imgur.com/champinones.jpeg', 0),
(36, 'https://i.imgur.com/rucula.jpeg', 0), (37, 'https://i.imgur.com/tomatess.jpeg', 0), (38, 'https://i.imgur.com/quesoa.jpeg', 0), (39, 'https://i.imgur.com/pepinillos.jpeg', 0), (40, 'https://i.imgur.com/bbq.jpeg', 0),
(41, 'https://i.imgur.com/cocacola500.jpeg', 0), (42, 'https://i.imgur.com/sprite500.jpeg', 0), (43, 'https://i.imgur.com/fanta500.jpeg', 0), (44, 'https://i.imgur.com/cocacola1500.jpeg', 0), (45, 'https://i.imgur.com/sprite1500.jpeg', 0),
(46, 'https://i.imgur.com/aguasg500.jpeg', 0), (47, 'https://i.imgur.com/aguacg500.jpeg', 0), (48, 'https://i.imgur.com/aguasabor500.jpeg', 0), (49, 'https://i.imgur.com/andesroja.jpeg', 0), (50, 'https://i.imgur.com/andesrubia.jpeg', 0),
(51, 'https://i.imgur.com/stella.jpeg', 0), (52, 'https://i.imgur.com/corona.jpeg', 0), (53, 'https://i.imgur.com/sobrem.jpeg', 0), (54, 'https://i.imgur.com/sobrek.jpeg', 0), (55, 'https://i.imgur.com/sobremo.jpeg', 0),
(56, 'https://i.imgur.com/hamburclasica.jpeg', 0), (57, 'https://i.imgur.com/hamburcheddar.jpeg', 0), (58, 'https://i.imgur.com/hamburcompleta.jpeg', 0), (59, 'https://i.imgur.com/hamburamericana.jpeg', 0), (60, 'https://i.imgur.com/pizzamuzza.jpeg', 0),
(61, 'https://i.imgur.com/pizzaespecial.jpeg', 0), (62, 'https://i.imgur.com/pizzafugazzeta.jpeg', 0), (63, 'https://i.imgur.com/pizzacalabresa.jpeg', 0), (64, 'https://i.imgur.com/lomosimple.jpeg', 0), (65, 'https://i.imgur.com/lomocompleto.jpeg', 0),
(66, 'https://i.imgur.com/lomocheddar.jpeg', 0), (67, 'https://i.imgur.com/papasclasicas.jpeg', 0), (68, 'https://i.imgur.com/papascheddar.jpeg', 0), (69, 'https://i.imgur.com/aroscebolla.jpeg', 0), (70, 'https://i.imgur.com/ensaladamixta.jpeg', 0);


-- =================================================================================================
-- sección 6: ARTÍCULOS (INSUMOS Y MANUFACTURADOS)
-- =================================================================================================
-- La inserción se divide en 3 partes debido a la estrategia de herencia JOINED:
-- 1. Tabla 'articulo': Contiene los datos comunes a todos los artículos.
-- 2. Tabla 'articulo_insumo': Contiene los datos específicos de los insumos.
-- 3. Tabla 'articulo_manufacturado': Contiene los datos específicos de los productos manufacturados.

-- 6.1 INSUMOS PARA ELABORAR (40)
-- Insertar en 'articulo' (parte común)
INSERT INTO articulo (id, tipo_articulo, denominacion, precio_venta, eliminado, categoria_id, unidad_medida_id, imagen_id) VALUES
-- Carnes (Cat 8)
(1, 'INSUMO', 'Carne para Hamburguesa', 0, 0, 8, 2, 1),
(2, 'INSUMO', 'Carne para Lomo', 0, 0, 8, 2, 2),
(3, 'INSUMO', 'Pollo Desmenuzado', 0, 0, 8, 2, 3),
(4, 'INSUMO', 'Salchicha', 0, 0, 8, 2, 4),
(5, 'INSUMO', 'Jamón Cocido', 0, 0, 8, 2, 5),
-- Vegetales (Cat 9)
(6, 'INSUMO', 'Lechuga', 0, 0, 9, 2, 6),
(7, 'INSUMO', 'Tomate', 0, 0, 9, 2, 7),
(8, 'INSUMO', 'Cebolla', 0, 0, 9, 2, 8),
(9, 'INSUMO', 'Pimiento Verde', 0, 0, 9, 2, 9),
(10, 'INSUMO', 'Huevo', 0, 0, 9, 5, 10),
(11, 'INSUMO', 'Papas', 0, 0, 9, 2, 11),
(12, 'INSUMO', 'Aceitunas Verdes', 0, 0, 9, 2, 12),
-- Panificados (Cat 10)
(13, 'INSUMO', 'Pan de Hamburguesa', 0, 0, 10, 5, 13),
(14, 'INSUMO', 'Pan de Lomo', 0, 0, 10, 5, 14),
(15, 'INSUMO', 'Masa para Pizza', 0, 0, 10, 5, 15),
(16, 'INSUMO', 'Pan de Panchos', 0, 0, 10, 5, 16),
-- Lácteos (Cat 11)
(17, 'INSUMO', 'Queso Cheddar', 0, 0, 11, 2, 17),
(18, 'INSUMO', 'Queso Mozzarella', 0, 0, 11, 2, 18),
(19, 'INSUMO', 'Queso Tybo', 0, 0, 11, 2, 19),
-- Aderezos (Cat 12)
(20, 'INSUMO', 'Mayonesa', 0, 0, 12, 4, 20),
(21, 'INSUMO', 'Ketchup', 0, 0, 12, 4, 21),
(22, 'INSUMO', 'Mostaza', 0, 0, 12, 4, 22),
(23, 'INSUMO', 'Salsa de Tomate', 0, 0, 12, 4, 23),
(24, 'INSUMO', 'Salsa Golf', 0, 0, 12, 4, 24),
-- Insumos Genéricos (Cat 1)
(25, 'INSUMO', 'Orégano', 0, 0, 1, 1, 25),
(26, 'INSUMO', 'Sal', 0, 0, 1, 1, 26),
(27, 'INSUMO', 'Pimienta', 0, 0, 1, 1, 27),
(28, 'INSUMO', 'Aceite de Girasol', 0, 0, 1, 4, 28),
(29, 'INSUMO', 'Vinagre de Alcohol', 0, 0, 1, 4, 29),
(30, 'INSUMO', 'Harina de Trigo', 0, 0, 1, 1, 30),
(31, 'INSUMO', 'Levadura Fresca', 0, 0, 1, 1, 31),
(32, 'INSUMO', 'Azúcar', 0, 0, 1, 1, 32),
(33, 'INSUMO', 'Cebolla Caramelizada', 0, 0, 9, 1, 33),
(34, 'INSUMO', 'Panceta', 0, 0, 8, 2, 34),
(35, 'INSUMO', 'Champiñones', 0, 0, 9, 2, 35),
(36, 'INSUMO', 'Rúcula', 0, 0, 9, 2, 36),
(37, 'INSUMO', 'Tomates Secos', 0, 0, 9, 2, 37),
(38, 'INSUMO', 'Queso Azul', 0, 0, 11, 2, 38),
(39, 'INSUMO', 'Pepinillos', 0, 0, 9, 2, 39),
(40, 'INSUMO', 'Salsa Barbacoa', 0, 0, 12, 4, 40);

-- Insertar en 'articulo_insumo' (parte específica)
INSERT INTO articulo_insumo (articulo_id, precio_compra, stock_actual, stock_minimo, es_para_elaborar) VALUES
(1, 2500.00, 10000, 2000, 1), (2, 3500.00, 8000, 1500, 1), (3, 2000.00, 5000, 1000, 1), (4, 1500.00, 200, 50, 1), (5, 1800.00, 3000, 600, 1),
(6, 500.00, 1000, 200, 1), (7, 800.00, 5000, 1000, 1), (8, 400.00, 4000, 800, 1), (9, 700.00, 2000, 400, 1), (10, 100.00, 360, 72, 1),
(11, 300.00, 20000, 4000, 1), (12, 1200.00, 1000, 200, 1), (13, 80.00, 200, 40, 1), (14, 100.00, 150, 30, 1), (15, 200.00, 100, 20, 1),
(16, 50.00, 100, 20, 1), (17, 2200.00, 2000, 400, 1), (18, 2000.00, 10000, 2000, 1), (19, 1900.00, 3000, 600, 1), (20, 800.00, 5000, 1000, 1),
(21, 800.00, 5000, 1000, 1), (22, 700.00, 3000, 600, 1), (23, 500.00, 8000, 1500, 1), (24, 900.00, 2000, 400, 1), (25, 1500.00, 500, 100, 1),
(26, 200.00, 10000, 2000, 1), (27, 2000.00, 500, 100, 1), (28, 1000.00, 10000, 2000, 1), (29, 400.00, 5000, 1000, 1), (30, 300.00, 15000, 3000, 1),
(31, 800.00, 500, 100, 1), (32, 600.00, 10000, 2000, 1), (33, 1500.00, 1000, 200, 1), (34, 2500.00, 2000, 400, 1), (35, 1800.00, 1500, 300, 1),
(36, 900.00, 800, 150, 1), (37, 2500.00, 500, 100, 1), (38, 3000.00, 1000, 200, 1), (39, 1200.00, 800, 150, 1), (40, 1100.00, 2000, 400, 1);

-- 6.2 INSUMOS PARA VENTA (15)
-- Insertar en 'articulo' (parte común)
INSERT INTO articulo (id, tipo_articulo, denominacion, precio_venta, eliminado, categoria_id, unidad_medida_id, imagen_id) VALUES
-- Gaseosas (Cat 13)
(41, 'INSUMO', 'Coca-Cola 500ml', 800, 0, 13, 5, 41),
(42, 'INSUMO', 'Sprite 500ml', 800, 0, 13, 5, 42),
(43, 'INSUMO', 'Fanta 500ml', 800, 0, 13, 5, 43),
(44, 'INSUMO', 'Coca-Cola 1.5L', 1500, 0, 13, 5, 44),
(45, 'INSUMO', 'Sprite 1.5L', 1500, 0, 13, 5, 45),
-- Aguas (Cat 14)
(46, 'INSUMO', 'Agua sin Gas 500ml', 600, 0, 14, 5, 46),
(47, 'INSUMO', 'Agua con Gas 500ml', 600, 0, 14, 5, 47),
(48, 'INSUMO', 'Agua Saborizada Manzana 500ml', 700, 0, 14, 5, 48),
-- Cervezas (Cat 15)
(49, 'INSUMO', 'Cerveza Andes Origen Roja 473ml', 1200, 0, 15, 5, 49),
(50, 'INSUMO', 'Cerveza Andes Origen Rubia 473ml', 1200, 0, 15, 5, 50),
(51, 'INSUMO', 'Cerveza Stella Artois 473ml', 1300, 0, 15, 5, 51),
(52, 'INSUMO', 'Cerveza Corona 330ml', 1400, 0, 15, 5, 52),
-- Aderezos extra (Cat 12)
(53, 'INSUMO', 'Sobre Mayonesa', 100, 0, 12, 5, 53),
(54, 'INSUMO', 'Sobre Ketchup', 100, 0, 12, 5, 54),
(55, 'INSUMO', 'Sobre Mostaza', 100, 0, 12, 5, 55);

-- Insertar en 'articulo_insumo' (parte específica)
INSERT INTO articulo_insumo (articulo_id, precio_compra, stock_actual, stock_minimo, es_para_elaborar) VALUES
(41, 400, 100, 20, 0), (42, 400, 80, 15, 0), (43, 400, 70, 15, 0), (44, 800, 50, 10, 0), (45, 800, 40, 8, 0),
(46, 300, 120, 20, 0), (47, 300, 60, 12, 0), (48, 350, 50, 10, 0), (49, 600, 70, 14, 0), (50, 600, 70, 14, 0),
(51, 650, 60, 12, 0), (52, 700, 50, 10, 0), (53, 30, 500, 100, 0), (54, 30, 500, 100, 0), (55, 30, 500, 100, 0);

-- 6.3 PRODUCTOS MANUFACTURADOS (15)
-- Insertar en 'articulo' (parte común)
INSERT INTO articulo (id, tipo_articulo, denominacion, precio_venta, eliminado, categoria_id, imagen_id) VALUES
-- Hamburguesas (Cat 6)
(56, 'MANUFACTURADO', 'Hamburguesa Clásica', 3500, 0, 6, 56),
(57, 'MANUFACTURADO', 'Hamburguesa con Cheddar', 4000, 0, 6, 57),
(58, 'MANUFACTURADO', 'Hamburguesa Completa', 4800, 0, 6, 58),
(59, 'MANUFACTURADO', 'Hamburguesa Americana', 5200, 0, 6, 59),
-- Pizzas (Cat 4)
(60, 'MANUFACTURADO', 'Pizza Mozzarella', 6000, 0, 4, 60),
(61, 'MANUFACTURADO', 'Pizza Especial', 7000, 0, 4, 61),
(62, 'MANUFACTURADO', 'Pizza Fugazzeta', 6800, 0, 4, 62),
(63, 'MANUFACTURADO', 'Pizza Calabresa', 7200, 0, 4, 63),
-- Lomos (Cat 5)
(64, 'MANUFACTURADO', 'Lomo Simple', 5000, 0, 5, 64),
(65, 'MANUFACTURADO', 'Lomo Completo', 6500, 0, 5, 65),
(66, 'MANUFACTURADO', 'Lomo con Cheddar', 6800, 0, 5, 66),
-- Guarniciones (Cat 7)
(67, 'MANUFACTURADO', 'Papas Fritas Clásicas', 2000, 0, 7, 67),
(68, 'MANUFACTURADO', 'Papas Fritas con Cheddar y Panceta', 3000, 0, 7, 68),
(69, 'MANUFACTURADO', 'Aros de Cebolla', 2500, 0, 7, 69),
(70, 'MANUFACTURADO', 'Ensalada Mixta', 1800, 0, 7, 70);

-- Insertar en 'articulo_manufacturado' (parte específica)
INSERT INTO articulo_manufacturado (articulo_id, descripcion, tiempo_estimado_minutos, preparacion) VALUES
(56, 'Medallón de carne 120g, lechuga, tomate y mayonesa.', 15, 'Cocinar la carne, armar la hamburguesa en pan tostado.'),
(57, 'Medallón de carne 120g, queso cheddar, cebolla y ketchup.', 15, 'Cocinar la carne, agregar cheddar, armar en pan tostado.'),
(58, 'Medallón de carne 120g, jamón, queso, lechuga, tomate y huevo.', 20, 'Cocinar la carne y el huevo, armar todo en pan tostado.'),
(59, 'Doble medallón de carne 120g, doble cheddar, panceta crocante y salsa barbacoa.', 25, 'Cocinar la carne y la panceta, armar con mucho queso y salsa.'),
(60, 'Salsa de tomate, mozzarella y aceitunas verdes.', 20, 'Estirar la masa, agregar salsa y mozzarella, hornear.'),
(61, 'Salsa de tomate, mozzarella, jamón, pimiento y aceitunas.', 25, 'Estirar la masa, agregar ingredientes, hornear.'),
(62, 'Mucha cebolla, mozzarella y aceite de oliva.', 25, 'Cubrir la masa con cebolla y queso, hornear.'),
(63, 'Salsa de tomate, mozzarella y salchicha parrillera.', 25, 'Agregar rodajas de salchicha sobre la mozzarella y hornear.'),
(64, 'Bife de lomo, lechuga, tomate y mayonesa.', 20, 'Cocinar el lomo, armar el sandwich en pan tostado.'),
(65, 'Bife de lomo, jamón, queso, lechuga, tomate, huevo y mayonesa.', 25, 'Cocinar el lomo y el huevo, armar con todos los ingredientes.'),
(66, 'Bife de lomo, queso cheddar, cebolla caramelizada y panceta.', 25, 'Saltear la cebolla y panceta, cocinar el lomo y armar.'),
(67, 'Porción de papas fritas.', 10, 'Freír las papas en aceite caliente hasta dorar.'),
(68, 'Porción de papas fritas bañadas en queso cheddar y panceta crocante.', 15, 'Freír las papas, cubrir con cheddar derretido y panceta.'),
(69, 'Aros de cebolla rebozados y fritos.', 15, 'Pasar los aros por rebozador y freír.'),
(70, 'Lechuga, tomate y cebolla.', 10, 'Cortar y mezclar los vegetales.');


-- =================================================================================================
-- sección 7: DETALLES DE MANUFACTURADOS (RECETAS)
-- =================================================================================================
-- Se asocian los insumos a los productos manufacturados para definir sus recetas.
INSERT INTO articulo_manufacturado_detalle (id, cantidad, articulo_manufacturado_id, articulo_insumo_id) VALUES
-- Receta Hamburguesa Clásica (56)
(1, 1, 56, 13), (2, 150, 56, 1), (3, 30, 56, 6), (4, 30, 56, 7), (5, 10, 56, 20),
-- Receta Hamburguesa con Cheddar (57)
(6, 1, 57, 13), (7, 150, 57, 1), (8, 40, 57, 17), (9, 20, 57, 8), (10, 10, 57, 21),
-- Receta Hamburguesa Completa (58)
(11, 1, 58, 13), (12, 150, 58, 1), (13, 20, 58, 5), (14, 20, 58, 19), (15, 30, 58, 6), (16, 30, 58, 7), (17, 1, 58, 10),
-- Receta Hamburguesa Americana (59)
(18, 1, 59, 13), (19, 300, 59, 1), (20, 80, 59, 17), (21, 30, 59, 34), (22, 20, 59, 40),
-- Receta Pizza Mozzarella (60)
(23, 1, 60, 15), (24, 150, 60, 23), (25, 250, 60, 18), (26, 30, 60, 12), (27, 5, 60, 25),
-- Receta Pizza Especial (61)
(28, 1, 61, 15), (29, 150, 61, 23), (30, 200, 61, 18), (31, 100, 61, 5), (32, 50, 61, 9), (33, 30, 61, 12),
-- Receta Lomo Completo (65)
(34, 1, 65, 14), (35, 200, 65, 2), (36, 40, 65, 5), (37, 40, 65, 19), (38, 50, 65, 6), (39, 50, 65, 7), (40, 1, 65, 10), (41, 20, 65, 20),
-- Receta Papas con Cheddar (68)
(42, 300, 68, 11), (43, 50, 68, 17), (44, 30, 68, 34),
-- Receta Ensalada Mixta (70)
(45, 100, 70, 6), (46, 100, 70, 7), (47, 50, 70, 8);


-- =================================================================================================
-- sección 8: PROMOCIONES (5)
-- =================================================================================================
INSERT INTO promocion (id, denominacion, fecha_desde, fecha_hasta, hora_desde, hora_hasta, precio_promocional, descuento, eliminado, imagen_id, sucursal_id) VALUES
(1, 'Promo Clásica', '2025-06-01', '2025-07-31', '12:00:00', '23:00:00', 5500, NULL, 0, 56, 1),
(2, 'Promo Pareja', '2025-06-01', '2025-07-31', '20:00:00', '23:00:00', 13000, NULL, 0, 60, 1),
(3, 'Combo Lomo Completo', '2025-06-15', '2025-08-15', '12:00:00', '16:00:00', 10000, NULL, 0, 65, 1),
(4, 'Happy Hour Cerveza', '2025-06-01', '2025-12-31', '18:00:00', '20:00:00', 1200, NULL, 0, 49, 1),
(5, 'Menú Ejecutivo', '2025-06-01', '2025-12-31', '12:00:00', '15:00:00', 5500, NULL, 0, 64, 1);

INSERT INTO promocion_detalle (id, cantidad_requerida, articulo_id, promocion_id) VALUES
-- Detalle Promo Clásica (1)
(1, 1, 56, 1), (2, 1, 67, 1), (3, 1, 41, 1),
-- Detalle Promo Pareja (2)
(4, 2, 60, 2), (5, 2, 49, 2),
-- Detalle Combo Lomo Completo (3)
(6, 1, 65, 3), (7, 1, 68, 3), (8, 1, 44, 3),
-- Detalle Happy Hour (4)
(9, 2, 49, 4),
-- Detalle Menú Ejecutivo (5)
(10, 1, 64, 5), (11, 1, 48, 5);


-- =================================================================================================
-- sección 9: PEDIDOS (30)
-- =================================================================================================
-- Se insertarán pedidos con diferentes estados y fechas para simular actividad.
-- Los totales se calcularán en la lógica de negocio, aquí se inserta un valor placeholder.
-- Pedido 1 (Entregado y Facturado)
INSERT INTO pedido (id, hora_estimada_finalizacion, fecha_pedido, total, eliminado, cliente_id, domicilio_id, sucursal_id, estado_id, tipo_envio_id, forma_pago_id) VALUES (1, '21:00:00', '2025-06-16', 12300, 0, 1, 10, 1, 6, 1, 2);
INSERT INTO detalle_pedido (id, cantidad, sub_total, eliminado, articulo_id, pedido_id) VALUES (1, 1, 7000, 0, 61, 1), (2, 1, 5300, 0, 59, 1);
INSERT INTO factura (id, fecha_facturacion, sub_total, mpt_descuento, total_venta, eliminado, pedido_id, forma_pago_id) VALUES (1, '2025-06-16 21:05:00', 12300, 0, 12300, 0, 1, 2);

-- Pedido 2 (Entregado)
INSERT INTO pedido (id, hora_estimada_finalizacion, fecha_pedido, total, eliminado, cliente_id, domicilio_id, sucursal_id, estado_id, tipo_envio_id, forma_pago_id) VALUES (2, '22:15:00', '2025-06-15', 6300, 0, 2, 11, 1, 6, 1, 1);
INSERT INTO detalle_pedido (id, cantidad, sub_total, eliminado, articulo_id, pedido_id) VALUES (3, 1, 4800, 0, 58, 2), (4, 1, 1500, 0, 44, 2);

-- Pedido 3 (En Preparación)
INSERT INTO pedido (id, hora_estimada_finalizacion, fecha_pedido, total, eliminado, cliente_id, domicilio_id, sucursal_id, estado_id, tipo_envio_id, forma_pago_id) VALUES (3, '21:45:00', '2025-06-17', 6500, 0, 3, 12, 1, 2, 2, 2);
INSERT INTO detalle_pedido (id, cantidad, sub_total, eliminado, articulo_id, pedido_id) VALUES (5, 1, 6500, 0, 65, 3);

-- Pedido 4 (A confirmar)
INSERT INTO pedido (id, hora_estimada_finalizacion, fecha_pedido, total, eliminado, cliente_id, domicilio_id, sucursal_id, estado_id, tipo_envio_id, forma_pago_id) VALUES (4, '20:30:00', '2025-06-17', 8000, 0, 4, 13, 1, 1, 1, 1);
INSERT INTO detalle_pedido (id, cantidad, sub_total, eliminado, articulo_id, pedido_id) VALUES (6, 1, 6000, 0, 60, 4), (7, 2, 2000, 0, 67, 4);

-- Pedido 5 (Cancelado)
INSERT INTO pedido (id, hora_estimada_finalizacion, fecha_pedido, total, eliminado, cliente_id, domicilio_id, sucursal_id, estado_id, tipo_envio_id, forma_pago_id) VALUES (5, '13:00:00', '2025-06-14', 4100, 0, 5, 14, 1, 7, 2, 2);
INSERT INTO detalle_pedido (id, cantidad, sub_total, eliminado, articulo_id, pedido_id) VALUES (8, 1, 3500, 0, 56, 5), (9, 1, 600, 0, 46, 5);

-- Pedidos 6-30 (simulación variada)
INSERT INTO pedido (id, hora_estimada_finalizacion, fecha_pedido, total, eliminado, cliente_id, domicilio_id, sucursal_id, estado_id, tipo_envio_id, forma_pago_id) VALUES
(6, '21:00:00', '2025-06-13', 7000, 0, 6, 15, 1, 6, 1, 2),
(7, '22:30:00', '2025-06-12', 5500, 0, 7, 16, 1, 6, 2, 1),
(8, '20:45:00', '2025-06-17', 13000, 0, 8, 17, 1, 2, 1, 2),
(9, '21:15:00', '2025-06-17', 2500, 0, 9, 18, 1, 1, 2, 1),
(10, '14:00:00', '2025-06-11', 6800, 0, 10, 19, 1, 6, 1, 2),
(11, '23:00:00', '2025-06-10', 10000, 0, 11, 20, 1, 6, 1, 2),
(12, '21:30:00', '2025-06-09', 1200, 0, 12, 21, 1, 6, 2, 1),
(13, '22:00:00', '2025-06-08', 5500, 0, 13, 22, 1, 6, 1, 2),
(14, '20:50:00', '2025-06-17', 6500, 0, 14, 23, 1, 2, 1, 1),
(15, '21:40:00', '2025-06-17', 5500, 0, 15, 24, 1, 1, 2, 2),
(16, '13:30:00', '2025-06-07', 7200, 0, 1, 10, 1, 6, 1, 2),
(17, '22:25:00', '2025-06-06', 3000, 0, 2, 11, 1, 6, 2, 1),
(18, '21:55:00', '2025-06-05', 12000, 0, 3, 12, 1, 6, 1, 2),
(19, '20:35:00', '2025-06-17', 4000, 0, 4, 13, 1, 1, 1, 1),
(20, '22:05:00', '2025-06-04', 6800, 0, 5, 14, 1, 6, 2, 2),
(21, '21:20:00', '2025-06-03', 5000, 0, 6, 15, 1, 6, 1, 1),
(22, '12:30:00', '2025-06-02', 5500, 0, 7, 16, 1, 6, 2, 2),
(23, '22:40:00', '2025-06-01', 13000, 0, 8, 17, 1, 6, 1, 2),
(24, '21:00:00', '2025-05-31', 2500, 0, 9, 18, 1, 6, 1, 1),
(25, '20:55:00', '2025-05-30', 6800, 0, 10, 19, 1, 6, 2, 2),
(26, '22:10:00', '2025-05-29', 10000, 0, 11, 20, 1, 6, 1, 2),
(27, '21:35:00', '2025-05-28', 1200, 0, 12, 21, 1, 6, 2, 1),
(28, '22:45:00', '2025-05-27', 5500, 0, 13, 22, 1, 6, 1, 2),
(29, '21:25:00', '2025-05-26', 6500, 0, 14, 23, 1, 6, 2, 2),
(30, '20:30:00', '2025-05-25', 5500, 0, 15, 24, 1, 6, 1, 1);

-- Detalles para pedidos 6-30
INSERT INTO detalle_pedido (id, cantidad, sub_total, eliminado, articulo_id, pedido_id) VALUES
(10, 1, 7000, 0, 61, 6),
(11, 1, 5500, 0, 1, 7),
(12, 1, 13000, 0, 2, 8),
(13, 1, 2500, 0, 69, 9),
(14, 1, 6800, 0, 66, 10),
(15, 1, 10000, 0, 3, 11),
(16, 1, 1200, 0, 4, 12),
(17, 1, 5500, 0, 5, 13),
(18, 1, 6500, 0, 65, 14),
(19, 1, 5500, 0, 1, 15),
(20, 1, 7200, 0, 63, 16),
(21, 1, 3000, 0, 68, 17),
(22, 2, 12000, 0, 60, 18),
(23, 1, 4000, 0, 57, 19),
(24, 1, 6800, 0, 62, 20),
(25, 1, 5000, 0, 64, 21),
(26, 1, 5500, 0, 5, 22),
(27, 1, 13000, 0, 2, 23),
(28, 1, 2500, 0, 69, 24),
(29, 1, 6800, 0, 66, 25),
(30, 1, 10000, 0, 3, 26),
(31, 1, 1200, 0, 4, 27),
(32, 1, 5500, 0, 5, 28),
(33, 1, 6500, 0, 65, 29),
(34, 1, 5500, 0, 1, 30);

-- Facturas para pedidos facturados 6-30
INSERT INTO factura (id, fecha_facturacion, sub_total, mpt_descuento, total_venta, eliminado, pedido_id, forma_pago_id) VALUES
(2, '2025-06-13 21:10:00', 7000, 0, 7000, 0, 6, 2),
(3, '2025-06-10 23:05:00', 10000, 0, 10000, 0, 11, 2),
(4, '2025-06-07 13:35:00', 7200, 0, 7200, 0, 16, 2),
(5, '2025-06-04 22:10:00', 6800, 0, 6800, 0, 20, 2),
(6, '2025-06-01 22:45:00', 13000, 0, 13000, 0, 23, 2),
(7, '2025-05-29 22:15:00', 10000, 0, 10000, 0, 26, 2),
(8, '2025-05-26 21:30:00', 6500, 0, 6500, 0, 29, 2);


-- =================================================================================================
-- sección 10: COMPRA DE INSUMOS (3)
-- =================================================================================================
-- Se registran compras de insumos para abastecer el stock.
-- El stock y precio de compra en la tabla 'articulo_insumo' ya reflejan estas compras.

-- Compra 1: Reposición semanal de carnes y panes
INSERT INTO compra_insumo (id, denominacion, fecha_compra, total_compra) VALUES
(1, 'Reposición Carnes y Panes Semana 1', '2025-06-10', 139000.00);

INSERT INTO compra_detalle (id, cantidad, precio_unitario, subtotal, compra_id, insumo_id) VALUES
(1, 20, 2500.00, 50000.00, 1, 1),  -- Carne para Hamburguesa
(2, 15, 3500.00, 52500.00, 1, 2),  -- Carne para Lomo
(3, 100, 80.00, 8000.00, 1, 13), -- Pan de Hamburguesa
(4, 100, 100.00, 10000.00, 1, 14), -- Pan de Lomo
(5, 50, 370.00, 18500.00, 1, 5); -- Jamon Cocido


-- Compra 2: Compra grande de vegetales y lácteos
INSERT INTO compra_insumo (id, denominacion, fecha_compra, total_compra) VALUES
(2, 'Compra Vegetales y Lácteos Quincena 1', '2025-06-12', 242000.00);

INSERT INTO compra_detalle (id, cantidad, precio_unitario, subtotal, compra_id, insumo_id) VALUES
(6, 100, 800.00, 80000.00, 2, 7),   -- Tomate
(7, 80, 400.00, 32000.00, 2, 8),    -- Cebolla
(8, 50, 2000.00, 100000.00, 2, 18), -- Queso Mozzarella
(9, 10, 3000.00, 30000.00, 2, 19);  -- Queso Tybo


-- Compra 3: Aderezos y bebidas
INSERT INTO compra_insumo (id, denominacion, fecha_compra, total_compra) VALUES
(3, 'Pedido Aderezos y Bebidas', '2025-06-15', 150000.00);

INSERT INTO compra_detalle (id, cantidad, precio_unitario, subtotal, compra_id, insumo_id) VALUES
(10, 50, 800.00, 40000.00, 3, 20), -- Mayonesa
(11, 50, 800.00, 40000.00, 3, 21), -- Ketchup
(12, 100, 400.00, 40000.00, 3, 41), -- Coca-Cola 500ml
(13, 50, 600.00, 30000.00, 3, 49);  -- Cerveza Andes Origen Roja


-- =================================================================================================
-- sección 11: HISTORIAL DE ESTADOS DE PEDIDOS
-- =================================================================================================
-- Se insertan los cambios de estado para algunos pedidos para simular el flujo de trabajo.
INSERT INTO historial_pedido (id, fecha_hora, estado_id, pedido_id) VALUES
-- Historial Pedido 1 (Entregado)
(1, '2025-06-16 20:30:00', 1, 1), -- A_CONFIRMAR
(2, '2025-06-16 20:32:00', 2, 1), -- EN_PREPARACION
(3, '2025-06-16 20:50:00', 4, 1), -- LISTO
(4, '2025-06-16 20:52:00', 5, 1), -- EN_DELIVERY
(5, '2025-06-16 21:00:00', 6, 1), -- ENTREGADO

-- Historial Pedido 2 (Entregado)
(6, '2025-06-15 21:45:00', 1, 2), -- A_CONFIRMAR
(7, '2025-06-15 21:47:00', 2, 2), -- EN_PREPARACION
(8, '2025-06-15 22:05:00', 4, 2), -- LISTO
(9, '2025-06-15 22:07:00', 5, 2), -- EN_DELIVERY
(10, '2025-06-15 22:15:00', 6, 2), -- ENTREGADO

-- Historial Pedido 3 (En Preparación)
(11, '2025-06-17 21:15:00', 1, 3), -- A_CONFIRMAR
(12, '2025-06-17 21:17:00', 2, 3), -- EN_PREPARACION

-- Historial Pedido 4 (A Confirmar)
(13, '2025-06-17 20:01:00', 1, 4), -- A_CONFIRMAR

-- Historial Pedido 5 (Cancelado)
(14, '2025-06-14 12:30:00', 1, 5), -- A_CONFIRMAR
(15, '2025-06-14 12:35:00', 7, 5); -- CANCELADO


-- =================================================================================================
-- Finalización
-- =================================================================================================
-- Habilitar la verificación de claves foráneas nuevamente
SET FOREIGN_KEY_CHECKS=1;

-- Confirmar la transacción
COMMIT;

-- #################################################################################################
-- #                                                                                               #
-- #                                    FIN DEL SCRIPT                                             #
-- #                                                                                               #
-- #################################################################################################
