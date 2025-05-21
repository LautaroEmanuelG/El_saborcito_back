# 🏛️ Elementos de Base de Datos para "El Saborcito" 🍕

Este documento describe una selección de procedimientos almacenados, funciones, triggers y vistas representativos del sistema de e-commerce "El Saborcito". Cada elemento se explica en términos de su propósito, el flujo de información involucrado y las tablas afectadas.

## 🗺️ Modelo de Entidad-Relación (Conceptual)

A continuación, se presentan los elementos de base de datos seleccionados, seguidos de una tabla resumen que ilustra algunas de las relaciones clave en el sistema.

## 🗄️ Procedimientos Almacenados

### 1. `CrearPedidoCompleto` 🛒✨

- Propósito: Encapsula la lógica completa para crear un nuevo pedido, incluyendo sus detalles, el cálculo inicial de totales y la actualización de stock. Esto asegura la atomicidad de la operación.
- Por qué se eligió: Es una operación central en un sistema de e-commerce. Demuestra la gestión transaccional de múltiples inserciones y actualizaciones, crucial para la integridad de los datos de pedidos y stock.
- Flujo de Información:
  1. Recibe como entrada el ID del cliente (`p_cliente_id`), ID del domicilio (`p_domicilio_entrega_id`), ID de la sucursal (`p_sucursal_id`), tipo de envío, forma de pago y un JSON con los detalles de los artículos del pedido (`p_detalles_pedido`).
  2. Inicia una transacción.
  3. Inserta un nuevo registro en la tabla `Pedido` con los datos proporcionados y establece el estado inicial como 'PENDIENTE'. Obtiene el ID del pedido recién creado.
  4. (Conceptual) Itera sobre cada ítem en `p_detalles_pedido`:
     - Obtiene el `precio_venta` y `costo` del artículo desde la tabla `Articulo`.
     - Calcula el `sub_total` para el detalle.
     - Inserta un nuevo registro en `DetallePedido` asociándolo al `v_pedido_id`.
     - Acumula el `sub_total` al `v_total` del pedido y el costo al `v_total_costo`.
     - Actualiza el `stock_actual` en `Articulo` (o `ArticuloInsumo` / `ArticuloManufacturadoDetalle` para componentes).
  5. Actualiza los campos `total` y `total_costo` en el registro del `Pedido` correspondiente al `v_pedido_id`.
  6. Confirma la transacción. Si ocurre algún error, se revierte.
- Tablas Afectadas:
  - `Pedido` (INSERT, UPDATE): Se crea el pedido y luego se actualizan sus totales.
  - `DetallePedido` (INSERT por cada ítem): Se guardan los artículos del pedido.
  - `Articulo` (SELECT, UPDATE): Se consulta precio y costo, y se actualiza el stock.
  - `ArticuloInsumo` (UPDATE): Se descuenta stock si el artículo es insumo.
  - `ArticuloManufacturadoDetalle` (SELECT): Para obtener los componentes de un manufacturado y descontar su stock de `ArticuloInsumo`.
  - `Cliente` (SELECT, referenciado por `p_cliente_id`): Para asociar el pedido.
  - `Domicilio` (SELECT, referenciado por `p_domicilio_entrega_id`): Para asociar el domicilio de entrega.
  - `Sucursal` (SELECT, referenciado por `p_sucursal_id`): Para asociar la sucursal.

### 🛍️ 2. `ActualizarStockArticulo`

**Propósito:**
Este procedimiento almacenado se encarga de actualizar el stock de un artículo específico. Es crucial para mantener la integridad del inventario después de ventas, devoluciones, o ajustes manuales.

**Por qué se eligió:**
Es un componente fundamental en cualquier sistema de gestión de inventario. Permite centralizar la lógica de actualización de stock, asegurando que se realice de manera consistente y correcta, especialmente considerando si el artículo es un insumo o un producto manufacturado (cuyo stock depende de sus componentes).

**Tablas Afectadas:**

- `ArticuloInsumo`: Actualiza el campo `stock_actual`.
- `ArticuloManufacturadoDetalle`: Se consulta para determinar los insumos componentes de un artículo manufacturado y las cantidades necesarias.
- `ArticuloManufacturado`: Se consulta para determinar si un artículo es manufacturado.

**Flujo de Información:**

1. **Entrada:**

   - `p_articulo_id` (BIGINT): ID del artículo cuyo stock se va a modificar.
   - `p_cantidad_a_modificar` (INT): La cantidad que se sumará (si es positiva, ej. por una devolución o reingreso) o restará (si es negativa, ej. por una venta o consumo) al stock actual.

2. **Proceso:**

   - Inicia una transacción para asegurar que todas las actualizaciones de stock se realicen de forma atómica.
   - Verifica si el `p_articulo_id` corresponde a un artículo manufacturado consultando la tabla `ArticuloManufacturado`.
   - **Si es un artículo manufacturado:**
     - Abre un cursor (`cur_componentes`) para iterar sobre cada `ArticuloInsumo` que compone el artículo manufacturado, obteniendo el `articulo_insumo_id` y la `cantidad` necesaria de cada insumo para producir una unidad del manufacturado (desde `ArticuloManufacturadoDetalle`).
     - Para cada insumo componente, calcula la cantidad total a modificar en el stock del insumo: `p_cantidad_a_modificar * cantidad_componente_necesaria`.
     - Actualiza el `stock_actual` en la tabla `ArticuloInsumo` para ese insumo componente sumando la cantidad calculada.
   - **Si NO es un artículo manufacturado (es decir, es un insumo o un artículo simple cuyo stock se maneja directamente en `ArticuloInsumo`):**
     - Actualiza directamente el `stock_actual` en la tabla `ArticuloInsumo` para el `p_articulo_id` sumando `p_cantidad_a_modificar`.
   - Confirma la transacción.

3. **Salida:** El stock de los artículos (insumos o componentes de manufacturados) se actualiza en la base de datos.

---

### 📈 3. `ActualizarPreciosArticulosPorCategoria` 🏷️📈

- Propósito: Permite actualizar los precios de venta de todos los artículos pertenecientes a una categoría específica, aplicando un porcentaje de aumento o disminución.
- Por qué se eligió: Es útil para ajustes de precios masivos basados en estrategias comerciales o cambios en costos de proveedores por categoría. Demuestra operaciones de actualización masiva controlada.
- Flujo de Información:
  1. Recibe como entrada el ID de la `Categoria` (`p_categoria_id`) y el porcentaje de ajuste (`p_porcentaje_aumento`).
  2. Inicia una transacción.
  3. Selecciona todos los `Articulo` que pertenecen a la `p_categoria_id` y no están dados de baja.
  4. Para cada artículo seleccionado, calcula el nuevo `precio_venta` aplicando el `p_porcentaje_aumento` al `precio_venta` actual.
  5. Actualiza el campo `precio_venta` en la tabla `Articulo` para dichos artículos.
  6. (Opcional) Podría registrar esta modificación masiva en una tabla de auditoría de precios.
  7. Confirma la transacción.
- Tablas Afectadas:
  - `Articulo` (SELECT, UPDATE): Se identifican y actualizan los precios de los artículos.
  - `Categoria` (SELECT, referenciado por `p_categoria_id`): Para filtrar los artículos.

## ⚙️ Funciones

### 1. `CalcularTotalPedido` 🧮🛍️

- Propósito: Calcula el monto total de un pedido sumando los subtotales de todos sus detalles activos.
- Por qué se eligió: Es un cálculo fundamental y reutilizable en varias partes del sistema (creación de pedido, facturación, reportes). Centralizar este cálculo asegura consistencia.
- Flujo de Información:
  1. Recibe como entrada el ID del `Pedido` (`p_pedido_id`).
  2. Selecciona todos los registros de `DetallePedido` que estén asociados al `p_pedido_id` y no estén dados de baja.
  3. Suma los valores del campo `sub_total` de los detalles seleccionados.
  4. Devuelve la suma total. Si no hay detalles o el pedido no existe, devuelve 0.00.
- Tablas Afectadas:
  - `DetallePedido` (SELECT): Se leen los subtotales de los ítems del pedido.
  - `Pedido` (referenciado por `p_pedido_id`).

### 2. `VerificarStockDisponible` ✅📦

- Propósito: Verifica si la cantidad solicitada de un artículo específico (sea insumo o manufacturado) está disponible en el inventario. Para manufacturados, implica chequear el stock de sus componentes.
- Por qué se eligió: Es una validación crítica antes de confirmar un pedido o añadir un artículo al carrito. Previene ventas de productos sin stock y es clave para la fiabilidad del inventario.
- Flujo de Información:
  1. Recibe el ID del artículo (`p_articulo_id`) y la cantidad deseada (`p_cantidad_solicitada`).
  2. Determina si el `p_articulo_id` es un `ArticuloInsumo` o `ArticuloManufacturado`.
  3. Si es `ArticuloInsumo`:
     - Consulta su `stock_actual` en la tabla `ArticuloInsumo`.
     - Compara si `stock_actual` es mayor o igual a `p_cantidad_solicitada`. Devuelve TRUE o FALSE.
  4. Si es `ArticuloManufacturado`:
     - Consulta la tabla `ArticuloManufacturadoDetalle` para obtener todos sus `ArticuloInsumo` componentes y la cantidad necesaria de cada uno (`cant_necesaria_componente`).
     - Para cada componente, llama recursivamente (o lógicamente) a `VerificarStockDisponible` con el ID del insumo componente y la cantidad total necesaria (`p_cantidad_solicitada` \* `cant_necesaria_componente`).
     - Si todos los componentes tienen stock suficiente, devuelve TRUE. Si alguno falla, devuelve FALSE.
  5. Devuelve el resultado booleano.
- Tablas Afectadas:
  - `Articulo` (SELECT): Para determinar el tipo de artículo (conceptual, podría ser directo a `ArticuloInsumo` o `ArticuloManufacturado`).
  - `ArticuloInsumo` (SELECT): Se consulta el `stock_actual`.
  - `ArticuloManufacturadoDetalle` (SELECT): Para obtener los componentes de un manufacturado y sus cantidades.

### 3. `ObtenerPrecioVentaVigenteArticulo` 💲📅

- Propósito: Devuelve el precio de venta final de un artículo en una fecha específica, considerando el precio base y cualquier promoción activa aplicable.
- Por qué se eligió: Esencial para mostrar precios correctos al cliente en tiempo real y para la facturación. Involucra lógica de negocio para determinar el precio más ventajoso considerando promociones.
- Flujo de Información:
  1. Recibe el ID del `Articulo` (`p_articulo_id`) y la fecha para la cual se quiere el precio (`p_fecha_consulta`).
  2. Obtiene el `precio_venta` base del artículo desde la tabla `Articulo`.
  3. Busca en la tabla `PromocionDetalle` si existe alguna promoción activa para ese `p_articulo_id` en la `p_fecha_consulta`. Una promoción está activa si `p_fecha_consulta` está entre `Promocion.fecha_desde` y `Promocion.fecha_hasta`.
  4. Si encuentra una o más promociones activas, selecciona la que ofrezca el `precio_promocional` más bajo.
  5. Compara el mejor `precio_promocional` encontrado con el `precio_venta` base.
  6. Devuelve el menor de los dos precios. Si no hay promociones activas, devuelve el `precio_venta` base.
- Tablas Afectadas:
  - `Articulo` (SELECT): Para obtener el `precio_venta` base.
  - `Promocion` (SELECT): Para verificar las fechas de vigencia de las promociones.
  - `PromocionDetalle` (SELECT): Para encontrar promociones aplicables al artículo y obtener el `precio_promocional`.

## ⚡ Triggers

### 1. `DescontarStockAlCrearDetallePedido` 📉🛍️

- Propósito: Descuenta automáticamente el stock del artículo (o sus componentes si es manufacturado) cuando se inserta un nuevo detalle en un pedido.
- Por qué se eligió: Automatiza una tarea crítica para mantener la sincronización del inventario con las ventas. Asegura que el stock se reduzca en tiempo real al confirmarse un ítem en el pedido.
- Flujo de Información:
  1. Se activa `AFTER INSERT` en la tabla `DetallePedido`. Accede a los datos del nuevo detalle insertado (`NEW.articulo_id`, `NEW.cantidad`).
  2. Determina si `NEW.articulo_id` es un `ArticuloInsumo` o `ArticuloManufacturado`.
  3. Si es `ArticuloInsumo`:
     - Actualiza `ArticuloInsumo` decrementando `stock_actual` en `NEW.cantidad` para el `NEW.articulo_id`.
  4. Si es `ArticuloManufacturado`:
     - Consulta `ArticuloManufacturadoDetalle` para obtener los `articulo_insumo_id` y `cantidad_componente` de cada insumo que compone el manufacturado.
     - Para cada insumo componente, actualiza `ArticuloInsumo` decrementando `stock_actual` en (`NEW.cantidad` \* `cantidad_componente`).
- Tablas Afectadas:
  - `DetallePedido` (AFTER INSERT, origen del trigger).
  - `ArticuloInsumo` (UPDATE): Se descuenta el stock.
  - `ArticuloManufacturado` (SELECT, para identificarlo como tal).
  - `ArticuloManufacturadoDetalle` (SELECT): Para obtener los componentes y sus cantidades.

### 2. `ValidarStockAntesDeInsertarDetallePedido` 🛡️🛒

- Propósito: Antes de insertar un detalle de pedido, verifica si hay stock suficiente para el artículo solicitado. Si no hay, impide la inserción.
- Por qué se eligió: Actúa como una salvaguarda temprana para prevenir que se añadan al pedido artículos sin disponibilidad, mejorando la experiencia del usuario y la integridad de los datos.
- Flujo de Información:
  1. Se activa `BEFORE INSERT` en la tabla `DetallePedido`. Accede a los datos que se intentan insertar (`NEW.articulo_id`, `NEW.cantidad`).
  2. Llama a la función `VerificarStockDisponible(NEW.articulo_id, NEW.cantidad)`.
  3. Si la función devuelve `FALSE` (no hay stock suficiente):
     - Se genera una excepción (SIGNAL SQLSTATE '45000') que cancela la operación de `INSERT`.
  4. Si la función devuelve `TRUE`, el `INSERT` prosigue.
- Tablas Afectadas:
  - `DetallePedido` (BEFORE INSERT, origen del trigger).
  - Utiliza la función `VerificarStockDisponible` que a su vez accede a `ArticuloInsumo` y `ArticuloManufacturadoDetalle`.

### 3. `ActualizarFechaModificacionPedido` ✏️📅

- Propósito: Actualiza automáticamente el campo `fecha_modificacion` de un pedido cada vez que se modifica alguno de sus campos relevantes (estado, total, etc.).
- Por qué se eligió: Útil para auditoría y seguimiento de cambios en los pedidos. Evita la necesidad de actualizar este campo manualmente desde la lógica de la aplicación.
- Flujo de Información:
  1. Se activa `BEFORE UPDATE` en la tabla `Pedido`. Accede a los valores antiguos (`OLD.*`) y nuevos (`NEW.*`) de la fila que se está actualizando.
  2. Compara si alguno de los campos monitoreados (ej. `OLD.estado` vs `NEW.estado`, `OLD.total` vs `NEW.total`) ha cambiado.
  3. Si se detecta un cambio en alguno de los campos monitoreados:
     - Establece `NEW.fecha_modificacion` a la fecha y hora actual (`NOW()`).
  4. La operación de `UPDATE` continúa con el campo `fecha_modificacion` actualizado (si hubo cambios) o sin modificar (si no los hubo).
- Tablas Afectadas:
  - `Pedido` (BEFORE UPDATE, origen del trigger y tabla modificada).

## 📊 Vistas

### 1. `VistaPedidosDetalladosClientes` 📄👥

- Propósito: Proporciona una visión desnormalizada y completa de los pedidos, incluyendo detalles del cliente, domicilio de entrega, artículos del pedido y factura asociada.
- Por qué se eligió: Simplifica consultas complejas para reportes o visualizaciones que requieren información combinada de múltiples tablas. Mejora el rendimiento de lectura para estos casos de uso.
- Flujo de Información:
  1. La vista define una consulta `SELECT` que une (`JOIN`) múltiples tablas.
  2. Cuando se consulta la vista, el SGBD ejecuta este `SELECT` subyacente.
  3. Combina campos de `Pedido` (ID, fecha, total, estado), `Cliente` (a través de `Usuario` para nombre, email), `Domicilio` (calle, número, localidad), `DetallePedido` (artículo, cantidad, subtotal) y `Factura` (ID, fecha).
  4. Filtra los registros para excluir aquellos marcados como eliminados (`fecha_baja IS NULL`).
  5. Devuelve un conjunto de resultados donde cada fila representa un detalle de un pedido con toda la información relacionada.
- Tablas Involucradas: `Pedido`, `Cliente`, `Usuario`, `Domicilio`, `Localidad`, `DetallePedido`, `Articulo`, `Factura`.

### 2. `VistaArticulosConDetallesCompletos` 🍔📋

- Propósito: Ofrece una vista detallada de los artículos, incluyendo su tipo (insumo o manufacturado), stock (para insumos), tiempo estimado (para manufacturados), categoría, unidad de medida, URLs de imágenes y descripción de componentes (para manufacturados).
- Por qué se eligió: Facilita la consulta de información completa sobre los artículos para listados de productos, catálogos o gestión de inventario, evitando joins complejos en cada consulta.
- Flujo de Información:
  1. La vista define una consulta `SELECT` que une (`JOIN` y `LEFT JOIN`) múltiples tablas.
  2. Determina el `tipo_articulo` ('INSUMO', 'MANUFACTURADO') basado en la existencia del ID del artículo en `ArticuloInsumo` o `ArticuloManufacturado`.
  3. Obtiene `stock_actual_insumo` de `ArticuloInsumo` (si aplica).
  4. Obtiene `tiempo_estimado_minutos` de `ArticuloManufacturado` (si aplica).
  5. Agrupa y concatena las URLs de `ImagenArticulo` para cada artículo.
  6. Para artículos manufacturados, utiliza una subconsulta para agrupar y concatenar la descripción de sus componentes (`ArticuloManufacturadoDetalle` unido con `ArticuloInsumo`, `Articulo` y `UnidadMedida` para los nombres y cantidades de los componentes).
  7. Une con `Categoria` y `UnidadMedida` para obtener sus denominaciones.
  8. Filtra los registros para excluir aquellos marcados como eliminados.
  9. Devuelve un conjunto de resultados donde cada fila representa un artículo con todos sus detalles relevantes.
- Tablas Involucradas: `Articulo`, `ArticuloInsumo`, `ArticuloManufacturado`, `Categoria`, `UnidadMedida`, `ImagenArticulo`, `ArticuloManufacturadoDetalle`.

## 📝 Tabla Resumen de Entidades y Relaciones Clave

| Entidad Origen                 | Relación (Cardinalidad) | Entidad Destino         | Descripción de la Relación                                     |
| ------------------------------ | ----------------------- | ----------------------- | -------------------------------------------------------------- |
| `Pedido`                       | Muchos a Uno            | `Cliente`               | Un pedido pertenece a un cliente.                              |
| `Pedido`                       | Muchos a Uno            | `Domicilio`             | Un pedido se entrega en un domicilio.                          |
| `Pedido`                       | Muchos a Uno            | `Sucursal`              | Un pedido se procesa en una sucursal.                          |
| `DetallePedido`                | Muchos a Uno            | `Pedido`                | Un detalle de pedido pertenece a un pedido.                    |
| `DetallePedido`                | Muchos a Uno            | `Articulo`              | Un detalle de pedido es sobre un artículo.                     |
| `Factura`                      | Uno a Uno               | `Pedido`                | Una factura se genera para un pedido.                          |
| `Articulo`                     | Muchos a Uno            | `Categoria`             | Un artículo pertenece a una categoría.                         |
| `Articulo`                     | Muchos a Uno            | `UnidadMedida`          | Un artículo tiene una unidad de medida.                        |
| `ArticuloManufacturadoDetalle` | Muchos a Uno            | `ArticuloManufacturado` | Un detalle de manufacturado pertenece a un art. manufacturado. |
| `ArticuloManufacturadoDetalle` | Muchos a Uno            | `ArticuloInsumo`        | Un detalle de manufacturado especifica un insumo componente.   |
| `PromocionDetalle`             | Muchos a Uno            | `Promocion`             | Un detalle de promoción pertenece a una promoción.             |
| `PromocionDetalle`             | Muchos a Uno            | `Articulo`              | Un detalle de promoción aplica a un artículo.                  |
| `ImagenArticulo`               | Muchos a Uno            | `Articulo`              | Una imagen pertenece a un artículo.                            |
| `Cliente`                      | Uno a Uno               | `Usuario`               | Un cliente tiene una cuenta de usuario.                        |
| `Empleado`                     | Uno a Uno               | `Usuario`               | Un empleado tiene una cuenta de usuario.                       |
| `Empleado`                     | Muchos a Uno            | `Sucursal`              | Un empleado trabaja en una sucursal.                           |
| `Sucursal`                     | Muchos a Uno            | `Empresa`               | Una sucursal pertenece a una empresa.                          |
| `Domicilio`                    | Muchos a Uno            | `Localidad`             | Un domicilio está en una localidad.                            |
| `Localidad`                    | Muchos a Uno            | `Provincia`             | Una localidad está en una provincia.                           |
| `Provincia`                    | Muchos a Uno            | `Pais`                  | Una provincia está en un país.                                 |

---
