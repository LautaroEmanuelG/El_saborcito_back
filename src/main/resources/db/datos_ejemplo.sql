-- Archivo: e:\WorkSpace\Projects\El_Saborcito\El_saborcito_back\datos_ejemplo.sql
-- Script de Inserción de Datos de Ejemplo para "El Saborcito" (MySQL)

-- Desactivar revisión de claves foráneas temporalmente para carga masiva
SET FOREIGN_KEY_CHECKS=0;

-- Vaciar tablas (opcional, pero útil para ejecuciones repetidas)
-- Se debe hacer en orden inverso a la creación o con FOREIGN_KEY_CHECKS=0
DELETE FROM DetallePedido;
DELETE FROM Factura;
DELETE FROM Pedido;
DELETE FROM PromocionDetalle;
DELETE FROM Promocion;
DELETE FROM ArticuloManufacturadoDetalle;
DELETE FROM ArticuloInsumo; -- Asume que Articulo es padre y esta es hija
DELETE FROM ArticuloManufacturado; -- Asume que Articulo es padre y esta es hija
DELETE FROM Articulo; -- Tabla padre
DELETE FROM ImagenArticulo;
DELETE FROM Categoria;
DELETE FROM UnidadMedida;
DELETE FROM Domicilio;
DELETE FROM Cliente;
DELETE FROM Empleado;
DELETE FROM Usuario;
DELETE FROM HorarioAtencion;
DELETE FROM Sucursal;
DELETE FROM Empresa;
DELETE FROM Localidad;
DELETE FROM Provincia;
DELETE FROM Pais;


-- 1. Pais
INSERT INTO Pais (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion) VALUES
(1, NOW(), NOW(), NULL, 'Argentina'),
(2, NOW(), NOW(), NULL, 'Uruguay');

-- 2. Provincia
INSERT INTO Provincia (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, pais_id) VALUES
(1, NOW(), NOW(), NULL, 'Mendoza', 1),
(2, NOW(), NOW(), NULL, 'San Juan', 1),
(3, NOW(), NOW(), NULL, 'Montevideo', 2);

-- 3. Localidad
INSERT INTO Localidad (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, provincia_id) VALUES
(1, NOW(), NOW(), NULL, 'Ciudad de Mendoza', 1),
(2, NOW(), NOW(), NULL, 'Godoy Cruz', 1),
(3, NOW(), NOW(), NULL, 'Guaymallén', 1),
(4, NOW(), NOW(), NULL, 'Ciudad de San Juan', 2),
(5, NOW(), NOW(), NULL, 'Pocitos', 3);

-- 4. Empresa
INSERT INTO Empresa (id, fecha_creacion, fecha_modificacion, fecha_baja, nombre, razon_social, cuil, logo, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'El Saborcito Central', 'El Saborcito S.A.', '30-12345678-9', 'logo_empresa.png', false);

-- 5. Sucursal
-- Asumimos que el domicilio de la sucursal se crea aquí directamente o se relaciona con un Domicilio existente.
-- Por simplicidad, no crearemos una entrada separada en Domicilio para la sucursal aquí,
-- pero en un modelo completo, Sucursal tendría una FK a Domicilio.
INSERT INTO Sucursal (id, fecha_creacion, fecha_modificacion, fecha_baja, nombre, es_casa_matriz, empresa_id, domicilio_id, logo, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'Saborcito Centro', true, 1, NULL, 'logo_suc_centro.png', false), -- Domicilio_id se actualizará después
(2, NOW(), NOW(), NULL, 'Saborcito Godoy Cruz', false, 1, NULL, 'logo_suc_gc.png', false); -- Domicilio_id se actualizará después

-- 6. HorarioAtencion (para Sucursales)
-- Asumiendo que HorarioAtencion tiene dia_semana (ej. 1=Lunes, 7=Domingo)
INSERT INTO HorarioAtencion (id, fecha_creacion, fecha_modificacion, fecha_baja, dia_semana, horario_desde, horario_hasta, sucursal_id) VALUES
(1, NOW(), NOW(), NULL, 'LUNES', '09:00:00', '23:00:00', 1),
(2, NOW(), NOW(), NULL, 'MARTES', '09:00:00', '23:00:00', 1),
(3, NOW(), NOW(), NULL, 'MIERCOLES', '09:00:00', '23:00:00', 1),
(4, NOW(), NOW(), NULL, 'JUEVES', '09:00:00', '23:00:00', 1),
(5, NOW(), NOW(), NULL, 'VIERNES', '09:00:00', '00:00:00', 1), -- Hasta medianoche
(6, NOW(), NOW(), NULL, 'SABADO', '10:00:00', '00:00:00', 1),
(7, NOW(), NOW(), NULL, 'DOMINGO', '11:00:00', '15:00:00', 1),
(8, NOW(), NOW(), NULL, 'LUNES', '10:00:00', '22:00:00', 2),
(9, NOW(), NOW(), NULL, 'MARTES', '10:00:00', '22:00:00', 2);


-- 7. Usuario
INSERT INTO Usuario (id, fecha_creacion, fecha_modificacion, fecha_baja, auth0_id, username, nombre, apellido, telefono, email, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'auth0|user1', 'cliente1', 'Juan', 'Pérez', '2611234567', 'juan.perez@example.com', false),
(2, NOW(), NOW(), NULL, 'auth0|user2', 'cliente2', 'Ana', 'Gómez', '2617654321', 'ana.gomez@example.com', false),
(3, NOW(), NOW(), NULL, 'auth0|emp1', 'empleado1', 'Carlos', 'Rodríguez', '2615550011', 'carlos.rodriguez@elsaborcito.com', false),
(4, NOW(), NOW(), NULL, 'auth0|emp2', 'empleado2', 'Laura', 'Fernández', '2615550022', 'laura.fernandez@elsaborcito.com', false),
(5, NOW(), NOW(), NULL, 'auth0|admin1', 'admin_sabor', 'Admin', 'Saborcito', '2610000000', 'admin@elsaborcito.com', false);

-- 8. Cliente
INSERT INTO Cliente (id, fecha_creacion, fecha_modificacion, fecha_baja, usuario_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 1, false),
(2, NOW(), NOW(), NULL, 2, false);

-- 9. Empleado
-- Asumiendo que Rol es un ENUM o String en la tabla Empleado
INSERT INTO Empleado (id, fecha_creacion, fecha_modificacion, fecha_baja, tipo_rol, usuario_id, sucursal_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'COCINERO', 3, 1, false),
(2, NOW(), NOW(), NULL, 'CAJERO', 4, 1, false),
(3, NOW(), NOW(), NULL, 'ADMINISTRADOR', 5, 1, false); -- Admin general, puede estar asociado a la casa matriz

-- 10. Domicilio
INSERT INTO Domicilio (id, fecha_creacion, fecha_modificacion, fecha_baja, calle, numero, cp, piso, nro_depto, informacion_adicional, cliente_id, localidad_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'San Martín', 123, 5500, NULL, NULL, 'Frente a la plaza', 1, 1, false),
(2, NOW(), NOW(), NULL, 'Las Heras', 456, 5501, 1, 'A', NULL, 1, 2, false),
(3, NOW(), NOW(), NULL, 'Belgrano', 789, 5500, NULL, NULL, 'Portón verde', 2, 1, false),
(4, NOW(), NOW(), NULL, '9 de Julio', 101, 5500, NULL, NULL, 'Casa central Saborcito', NULL, 1, false), -- Domicilio Sucursal 1
(5, NOW(), NOW(), NULL, 'Rivadavia', 202, 5501, NULL, NULL, 'Sucursal Godoy Cruz Saborcito', NULL, 2, false); -- Domicilio Sucursal 2

-- Actualizar FK de domicilio en sucursales
UPDATE Sucursal SET domicilio_id = 4 WHERE id = 1;
UPDATE Sucursal SET domicilio_id = 5 WHERE id = 2;

-- 11. UnidadMedida
INSERT INTO UnidadMedida (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, abreviatura, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'Kilogramo', 'kg', false),
(2, NOW(), NOW(), NULL, 'Gramo', 'g', false),
(3, NOW(), NOW(), NULL, 'Litro', 'L', false),
(4, NOW(), NOW(), NULL, 'Mililitro', 'ml', false),
(5, NOW(), NOW(), NULL, 'Unidad', 'u', false),
(6, NOW(), NOW(), NULL, 'Porción', 'porción', false);

-- 12. Categoria
INSERT INTO Categoria (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, es_insumo, categoria_padre_id, es_eliminado) VALUES
-- Categorías de Insumos
(1, NOW(), NOW(), NULL, 'Harinas', true, NULL, false),
(2, NOW(), NOW(), NULL, 'Lácteos', true, NULL, false),
(3, NOW(), NOW(), NULL, 'Carnes Rojas', true, NULL, false),
(4, NOW(), NOW(), NULL, 'Vegetales Frescos', true, NULL, false),
(5, NOW(), NOW(), NULL, 'Salsas y Condimentos', true, NULL, false),
-- Categorías de Productos Manufacturados
(10, NOW(), NOW(), NULL, 'Pizzas', false, NULL, false),
(11, NOW(), NOW(), NULL, 'Empanadas', false, NULL, false),
(12, NOW(), NOW(), NULL, 'Lomos', false, NULL, false),
(13, NOW(), NOW(), NULL, 'Hamburguesas', false, NULL, false),
(14, NOW(), NOW(), NULL, 'Bebidas sin Alcohol', false, NULL, false),
(15, NOW(), NOW(), NULL, 'Bebidas con Alcohol', false, 14, false), -- Subcategoría de Bebidas
(16, NOW(), NOW(), NULL, 'Postres', false, NULL, false);

-- 13. Articulo (Tabla padre, se inserta a través de hijas ArticuloInsumo y ArticuloManufacturado)
-- Los IDs deben ser únicos a través de ArticuloInsumo y ArticuloManufacturado si se referencian genéricamente.
-- Aquí se asume que ArticuloInsumo y ArticuloManufacturado tienen sus propios IDs y Articulo es una tabla abstracta o con un tipo.
-- Si Articulo tiene su propio ID autoincremental y las hijas lo usan como FK y PK:
-- INSERT INTO Articulo (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, descripcion, costo, precio_venta, es_eliminado, categoria_id, unidad_medida_id) VALUES ...
-- Luego en ArticuloInsumo: INSERT INTO ArticuloInsumo (id, stock_actual, stock_maximo, stock_minimo, es_para_elaborar) VALUES (articulo_id_ref, ...)

-- Por simplicidad y asumiendo una estrategia de "tabla por clase concreta" o "tabla por jerarquía con discriminador"
-- donde ArticuloInsumo y ArticuloManufacturado son tablas que contienen todos los campos de Articulo:

-- 13.1 ArticuloInsumo
INSERT INTO ArticuloInsumo (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, descripcion, costo, precio_venta, es_eliminado, categoria_id, unidad_medida_id, stock_actual, stock_maximo, stock_minimo, es_para_elaborar) VALUES
(1, NOW(), NOW(), NULL, 'Harina 000', 'Harina de trigo común', 150.00, 0.00, false, 1, 1, 50.0, 100.0, 10.0, true),
(2, NOW(), NOW(), NULL, 'Queso Mozzarella', 'Queso mozzarella fresco', 800.00, 0.00, false, 2, 1, 20.0, 50.0, 5.0, true),
(3, NOW(), NOW(), NULL, 'Tomate Triturado', 'Lata de tomate triturado', 100.00, 0.00, false, 5, 2, 100.0, 200.0, 20.0, true),
(4, NOW(), NOW(), NULL, 'Carne Molida Especial', 'Carne de ternera molida', 1200.00, 0.00, false, 3, 1, 15.0, 30.0, 3.0, true),
(5, NOW(), NOW(), NULL, 'Pan de Hamburguesa', 'Pan artesanal con sésamo', 50.00, 0.00, false, 1, 5, 100.0, 200.0, 20.0, true),
(6, NOW(), NOW(), NULL, 'Lechuga Fresca', 'Lechuga mantecosa', 200.00, 0.00, false, 4, 1, 5.0, 10.0, 1.0, true),
(7, NOW(), NOW(), NULL, 'Cebolla Morada', 'Cebolla morada fresca', 180.00, 0.00, false, 4, 1, 7.0, 15.0, 2.0, true),
(8, NOW(), NOW(), NULL, 'Gaseosa Cola 1.5L', 'Bebida gaseosa sabor cola', 250.00, 400.00, false, 14, 5, 50.0, 100.0, 10.0, false), -- Este es un insumo que también se vende
(9, NOW(), NOW(), NULL, 'Cerveza Rubia 1L', 'Cerveza tipo Pilsen', 300.00, 500.00, false, 15, 5, 40.0, 80.0, 5.0, false); -- Insumo que se vende

-- 13.2 ArticuloManufacturado
INSERT INTO ArticuloManufacturado (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, descripcion, costo, precio_venta, es_eliminado, categoria_id, unidad_medida_id, tiempo_estimado_minutos, preparacion) VALUES
(101, NOW(), NOW(), NULL, 'Pizza Mozzarella Grande', 'Pizza clásica con salsa de tomate y mozzarella', 0.00, 1800.00, false, 10, 5, 20, 'Estirar masa, agregar salsa, mozzarella y hornear.'),
(102, NOW(), NOW(), NULL, 'Empanada de Carne (unidad)', 'Empanada criolla de carne cortada a cuchillo', 0.00, 200.00, false, 11, 5, 15, 'Rellenar disco de empanada, repulgar y hornear.'),
(103, NOW(), NOW(), NULL, 'Lomo Completo', 'Lomo de ternera con jamón, queso, lechuga, tomate, huevo y mayonesa, en pan árabe.', 0.00, 2500.00, false, 12, 5, 25, 'Cocinar lomo, armar sandwich con ingredientes.'),
(104, NOW(), NOW(), NULL, 'Hamburguesa Clásica', 'Hamburguesa con carne, lechuga, tomate, cebolla y aderezos.', 0.00, 1500.00, false, 13, 5, 15, 'Cocinar medallón de carne, armar hamburguesa.');

-- 14. ImagenArticulo
INSERT INTO ImagenArticulo (id, fecha_creacion, fecha_modificacion, fecha_baja, url, articulo_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'http://example.com/images/pizza_mozzarella.jpg', 101, false),
(2, NOW(), NOW(), NULL, 'http://example.com/images/empanada_carne.jpg', 102, false),
(3, NOW(), NOW(), NULL, 'http://example.com/images/lomo_completo.jpg', 103, false),
(4, NOW(), NOW(), NULL, 'http://example.com/images/hamburguesa_clasica.jpg', 104, false),
(5, NOW(), NOW(), NULL, 'http://example.com/images/gaseosa_cola.jpg', 8, false),
(6, NOW(), NOW(), NULL, 'http://example.com/images/cerveza_rubia.jpg', 9, false);

-- 15. ArticuloManufacturadoDetalle (Componentes de los manufacturados)
-- Pizza Mozzarella (101) usa Harina (1), Queso (2), Tomate (3)
INSERT INTO ArticuloManufacturadoDetalle (id, fecha_creacion, fecha_modificacion, fecha_baja, cantidad, articulo_manufacturado_id, articulo_insumo_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 0.300, 101, 1, false), -- 300g de Harina por pizza
(2, NOW(), NOW(), NULL, 0.250, 101, 2, false), -- 250g de Queso Mozzarella por pizza
(3, NOW(), NOW(), NULL, 0.150, 101, 3, false), -- 150g de Tomate Triturado por pizza
-- Empanada de Carne (102) usa Harina (1 para masa), Carne Molida (4)
(4, NOW(), NOW(), NULL, 0.050, 102, 1, false), -- 50g de Harina por empanada (para la masa)
(5, NOW(), NOW(), NULL, 0.100, 102, 4, false), -- 100g de Carne Molida por empanada
-- Hamburguesa Clásica (104) usa Pan (5), Carne Molida (4), Lechuga (6)
(6, NOW(), NOW(), NULL, 1.000, 104, 5, false), -- 1 Pan de Hamburguesa
(7, NOW(), NOW(), NULL, 0.150, 104, 4, false), -- 150g de Carne Molida
(8, NOW(), NOW(), NULL, 0.050, 104, 6, false); -- 50g de Lechuga

-- 16. Promocion
INSERT INTO Promocion (id, fecha_creacion, fecha_modificacion, fecha_baja, denominacion, fecha_desde, fecha_hasta, hora_desde, hora_hasta, descripcion_descuento, tipo_promocion, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 'Promo Pizza + Gaseosa', CURDATE(), CURDATE() + INTERVAL 7 DAY, '12:00:00', '23:00:00', 'Llevá una Pizza Mozzarella y una Gaseosa Cola con descuento', 'PROMOCION', false),
(2, NOW(), NOW(), NULL, 'Happy Hour Cervezas', CURDATE(), CURDATE() + INTERVAL 30 DAY, '18:00:00', '20:00:00', '2x1 en Cervezas Rubias', 'HAPPY_HOUR', false);

-- 17. PromocionDetalle
-- Promo Pizza (101) + Gaseosa (8)
INSERT INTO PromocionDetalle (id, fecha_creacion, fecha_modificacion, fecha_baja, cantidad, precio_promocional, articulo_id, promocion_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 1, 1700.00, 101, 1, false), -- Pizza en promo
(2, NOW(), NOW(), NULL, 1, 300.00, 8, 1, false),   -- Gaseosa en promo
-- Happy Hour Cerveza (9)
(3, NOW(), NOW(), NULL, 2, 500.00, 9, 2, false);   -- 2 Cervezas al precio de una (precio promocional es el total por las 2)

-- 18. Pedido
INSERT INTO Pedido (id, fecha_creacion, fecha_modificacion, fecha_baja, fecha_pedido, hora_estimada_finalizacion, total, total_costo, estado, tipo_envio, forma_pago, cliente_id, domicilio_entrega_id, sucursal_id, empleado_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 1 HOUR, 2200.00, 800.00, 'ENTREGADO', 'DELIVERY', 'MERCADO_PAGO', 1, 1, 1, 2, false),
(2, NOW(), NOW(), NULL, NOW() - INTERVAL 1 HOUR, NOW() + INTERVAL 30 MINUTE, 1900.00, 700.00, 'PREPARACION', 'TAKE_AWAY', 'EFECTIVO', 2, NULL, 1, 1, false),
(3, NOW(), NOW(), NULL, NOW() - INTERVAL 30 MINUTE, NOW() + INTERVAL 1 HOUR, 400.00, 150.00, 'PENDIENTE', 'DELIVERY', 'MERCADO_PAGO', 1, 2, 2, NULL, false);

-- 19. DetallePedido
-- Pedido 1: Pizza Mozzarella (101) x1, Gaseosa Cola (8) x1
INSERT INTO DetallePedido (id, fecha_creacion, fecha_modificacion, fecha_baja, cantidad, sub_total, articulo_id, pedido_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, 1, 1800.00, 101, 1, false),
(2, NOW(), NOW(), NULL, 1, 400.00, 8, 1, false),
-- Pedido 2: Empanada de Carne (102) x6, Cerveza Rubia (9) x1
(3, NOW(), NOW(), NULL, 6, 1200.00, 102, 2, false), -- 6 * 200
(4, NOW(), NOW(), NULL, 1, 500.00, 9, 2, false),
-- Pedido 3: Gaseosa Cola (8) x1
(5, NOW(), NOW(), NULL, 1, 400.00, 8, 3, false);

-- 20. Factura
INSERT INTO Factura (id, fecha_creacion, fecha_modificacion, fecha_baja, fecha_facturacion, monto_descuento, forma_pago, total_venta, pedido_id, es_eliminado) VALUES
(1, NOW(), NOW(), NULL, NOW() - INTERVAL 1 HOUR, 0.00, 'MERCADO_PAGO', 2200.00, 1, false);
-- Factura para pedido 2 y 3 se generarían cuando cambien de estado.


-- Reactivar revisión de claves foráneas
SET FOREIGN_KEY_CHECKS=1;

-- Nota:
-- Los costos de ArticuloManufacturado no se calcularon automáticamente aquí, se dejaron en 0.00.
-- En un sistema real, se calcularían basados en los costos de sus ArticuloInsumo componentes.
-- Los totales de Pedido también se insertaron directamente, pero podrían calcularse con la función CalcularTotalPedido.
-- Este script es una base, ajustar según necesidad y lógica específica del sistema (ej. manejo de stock de manufacturados).
