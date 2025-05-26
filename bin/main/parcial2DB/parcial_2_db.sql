-- Procedimientos Almacenados

-- 1. CrearPedidoCompleto
CREATE PROCEDURE CrearPedidoCompleto (
    IN p_cliente_id BIGINT,
    IN p_domicilio_entrega_id BIGINT,
    IN p_sucursal_id BIGINT,
    IN p_tipo_envio VARCHAR(255),
    IN p_forma_pago VARCHAR(255),
    IN p_detalles_pedido JSON -- Formato: [{\"articulo_id\": 1, \"cantidad\": 2}, ...]
)
BEGIN
    DECLARE v_pedido_id BIGINT;
    DECLARE v_total DECIMAL(10, 2) DEFAULT 0.00;
    DECLARE v_total_costo DECIMAL(10, 2) DEFAULT 0.00;
    -- Iniciar transacción
    START TRANSACTION;

    -- Crear el pedido
    INSERT INTO Pedido (fecha_pedido, hora_estimada_finalizacion, tipo_envio, forma_pago, estado, cliente_id, domicilio_entrega_id, sucursal_id)
    VALUES (NOW(), NOW() + INTERVAL '1' HOUR, p_tipo_envio, p_forma_pago, 'PENDIENTE', p_cliente_id, p_domicilio_entrega_id, p_sucursal_id);
    SET v_pedido_id = LAST_INSERT_ID();

    -- Iterar sobre los detalles del pedido (conceptual, la implementación real varía según el SGBD)
    -- FOR item IN JSON_TABLE(p_detalles_pedido, '$[*]' COLUMNS (articulo_id BIGINT PATH '$.articulo_id', cantidad INT PATH '$.cantidad'))
    -- DO
        -- DECLARE v_precio_venta DECIMAL(10,2);
        -- DECLARE v_costo_articulo DECIMAL(10,2);
        -- DECLARE v_subtotal DECIMAL(10,2);

        -- Obtener precio y costo del artículo (considerar si es insumo o manufacturado)
        -- SELECT precio_venta, costo INTO v_precio_venta, v_costo_articulo FROM Articulo WHERE id = item.articulo_id;
        -- SET v_subtotal = v_precio_venta * item.cantidad;

        -- Insertar detalle del pedido
        -- INSERT INTO DetallePedido (cantidad, sub_total, articulo_id, pedido_id)
        -- VALUES (item.cantidad, v_subtotal, item.articulo_id, v_pedido_id);

        -- Actualizar total y total_costo del pedido
        -- SET v_total = v_total + v_subtotal;
        -- SET v_total_costo = v_total_costo + (v_costo_articulo * item.cantidad);

        -- Descontar stock (lógica más compleja para manufacturados)
        -- UPDATE Articulo SET stock_actual = stock_actual - item.cantidad WHERE id = item.articulo_id;
    -- END FOR;

    -- Actualizar totales en el pedido
    UPDATE Pedido SET total = v_total, total_costo = v_total_costo WHERE id = v_pedido_id;

    -- Confirmar transacción
    COMMIT;
END;

-- 2. ActualizarStockArticulo
CREATE PROCEDURE ActualizarStockArticulo (
    IN p_articulo_id BIGINT,
    IN p_cantidad_a_modificar INT -- Positivo para sumar al stock (ej. reintegrar), Negativo para restar del stock (ej. consumir/vender)
)
BEGIN
    DECLARE v_es_manufacturado BOOLEAN DEFAULT FALSE;
    DECLARE v_cursor_done BOOLEAN DEFAULT FALSE;
    DECLARE v_componente_insumo_id BIGINT;
    DECLARE v_cantidad_componente_necesaria DECIMAL(10,2); -- Cantidad del insumo para 1 unidad del manufacturado

    -- Cursor para iterar sobre los componentes de un artículo manufacturado
    DECLARE cur_componentes CURSOR FOR
        SELECT amd.articulo_insumo_id, amd.cantidad
        FROM ArticuloManufacturadoDetalle amd
        WHERE amd.articulo_manufacturado_id = p_articulo_id AND amd.fecha_baja IS NULL;

    -- Handler para cuando el cursor no encuentra más filas
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_done = TRUE;

    -- Iniciar transacción para asegurar la atomicidad de las actualizaciones de stock
    START TRANSACTION;

    -- Determinar si el artículo es manufacturado
    -- Se asume que un artículo es manufacturado si tiene una entrada en la tabla ArticuloManufacturado
    SELECT EXISTS (SELECT 1 FROM ArticuloManufacturado WHERE id = p_articulo_id AND fecha_baja IS NULL) INTO v_es_manufacturado;

    IF v_es_manufacturado THEN
        -- Es un artículo manufacturado: modificar el stock de sus insumos componentes.
        -- Si p_cantidad_a_modificar es +N (ej. se reintegran N manufacturados),
        -- el stock de cada insumo componente aumenta en (N * cantidad_de_insumo_por_manufacturado).
        -- Si p_cantidad_a_modificar es -N (ej. se consumen/venden N manufacturados),
        -- el stock de cada insumo componente disminuye en (N * cantidad_de_insumo_por_manufacturado).
        OPEN cur_componentes;

        read_loop: LOOP
            FETCH cur_componentes INTO v_componente_insumo_id, v_cantidad_componente_necesaria;
            IF v_cursor_done THEN
                LEAVE read_loop;
            END IF;

            -- Actualizar el stock del insumo componente
            UPDATE ArticuloInsumo
            SET stock_actual = stock_actual + (p_cantidad_a_modificar * v_cantidad_componente_necesaria)
            WHERE id = v_componente_insumo_id;
        END LOOP read_loop;

        CLOSE cur_componentes;
    ELSE
        -- Es un artículo insumo (o un artículo simple cuyo stock se maneja en ArticuloInsumo):
        -- Actualizar directamente su stock en la tabla ArticuloInsumo.
        UPDATE ArticuloInsumo
        SET stock_actual = stock_actual + p_cantidad_a_modificar
        WHERE id = p_articulo_id;
    END IF;

    -- Confirmar la transacción
    COMMIT;
END;

-- 3. ActualizarPreciosArticulosPorCategoria
CREATE PROCEDURE ActualizarPreciosArticulosPorCategoria (
    IN p_categoria_id BIGINT,
    IN p_porcentaje_aumento DECIMAL(5, 2) -- Ej: 10.00 para 10%, -5.00 para -5%
)
BEGIN
    -- Iniciar transacción
    START TRANSACTION;

    UPDATE Articulo
    SET precio_venta = precio_venta * (1 + (p_porcentaje_aumento / 100.00))
    WHERE categoria_id = p_categoria_id
      AND fecha_baja IS NULL;

    -- Aquí se podría añadir lógica para registrar la modificación de precios en una tabla de auditoría si existiera.

    -- Confirmar transacción
    COMMIT;
END;

-- Funciones

-- 1. CalcularTotalPedido
CREATE FUNCTION CalcularTotalPedido (
    p_pedido_id BIGINT
)
RETURNS DECIMAL(10, 2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total DECIMAL(10, 2);
    SELECT SUM(sub_total) INTO v_total
    FROM DetallePedido
    WHERE pedido_id = p_pedido_id AND fecha_baja IS NULL;
    RETURN IFNULL(v_total, 0.00);
END;

-- 2. VerificarStockDisponible
CREATE FUNCTION VerificarStockDisponible (
    p_articulo_id BIGINT,
    p_cantidad_solicitada INT
)
RETURNS BOOLEAN
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_stock_disponible BOOLEAN DEFAULT FALSE;
    DECLARE v_tipo_articulo VARCHAR(50); -- Asumiendo una forma de diferenciar

    -- Determinar tipo de artículo y verificar stock correspondientemente
    -- SELECT tipo INTO v_tipo_articulo FROM Articulo WHERE id = p_articulo_id;
    -- IF v_tipo_articulo = 'INSUMO' THEN
    --    SELECT (stock_actual >= p_cantidad_solicitada) INTO v_stock_disponible
    --    FROM ArticuloInsumo WHERE id = p_articulo_id;
    -- ELSE IF v_tipo_articulo = 'MANUFACTURADO' THEN
        -- Para manufacturados, iterar sobre ArticuloManufacturadoDetalle,
        -- verificar stock de cada ArticuloInsumo componente.
        -- v_stock_disponible = TRUE; -- Asumir inicialmente
        -- FOR componente IN (SELECT amd.articulo_insumo_id, amd.cantidad AS cant_necesaria_componente
        --                    FROM ArticuloManufacturadoDetalle amd
        --                    WHERE amd.articulo_manufacturado_id = p_articulo_id AND amd.fecha_baja IS NULL)
        -- DO
        --    IF NOT VerificarStockDisponible(componente.articulo_insumo_id, p_cantidad_solicitada * componente.cant_necesaria_componente) THEN
        --        v_stock_disponible = FALSE;
        --        EXIT; -- Salir del bucle si un componente no tiene stock
        --    END IF;
        -- END FOR;
    -- END IF;
    RETURN IFNULL(v_stock_disponible, FALSE);
END;

-- 3. ObtenerPrecioVentaVigenteArticulo
CREATE FUNCTION ObtenerPrecioVentaVigenteArticulo (
    p_articulo_id BIGINT,
    p_fecha_consulta DATE
)
RETURNS DECIMAL(10, 2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_precio_base DECIMAL(10, 2);
    DECLARE v_precio_promocion DECIMAL(10, 2) DEFAULT NULL;

    SELECT precio_venta INTO v_precio_base FROM Articulo WHERE id = p_articulo_id AND fecha_baja IS NULL;

    -- Buscar la promoción más ventajosa activa para el artículo
    SELECT MIN(pd.precio_promocional) INTO v_precio_promocion
    FROM Promocion p
    JOIN PromocionDetalle pd ON p.id = pd.promocion_id
    WHERE pd.articulo_id = p_articulo_id
      AND p_fecha_consulta BETWEEN p.fecha_desde AND p.fecha_hasta
      AND p.fecha_baja IS NULL AND pd.fecha_baja IS NULL;

    IF v_precio_promocion IS NOT NULL AND v_precio_promocion < v_precio_base THEN
        RETURN v_precio_promocion;
    ELSE
        RETURN v_precio_base;
    END IF;
END;

-- Triggers

-- 1. DescontarStockAlCrearDetallePedido
CREATE TRIGGER DescontarStockAlCrearDetallePedido
AFTER INSERT ON DetallePedido
FOR EACH ROW
BEGIN
    DECLARE v_es_manufacturado BOOLEAN;
    -- Determinar si el NEW.articulo_id corresponde a un ArticuloManufacturado
    -- SELECT EXISTS (SELECT 1 FROM ArticuloManufacturado WHERE id = NEW.articulo_id) INTO v_es_manufacturado;

    -- IF v_es_manufacturado THEN
        -- Para Articulos Manufacturados, descontar stock de sus componentes (ArticuloInsumo)
        -- FOR componente IN (SELECT amd.articulo_insumo_id, amd.cantidad AS cantidad_componente
        --                   FROM ArticuloManufacturadoDetalle amd
        --                   WHERE amd.articulo_manufacturado_id = NEW.articulo_id AND amd.fecha_baja IS NULL)
        -- DO
        --    UPDATE ArticuloInsumo
        --    SET stock_actual = stock_actual - (NEW.cantidad * componente.cantidad_componente)
        --    WHERE id = componente.articulo_insumo_id;
        -- END FOR;
    -- ELSE
        -- Para Articulos Insumo (o artículos simples)
        UPDATE ArticuloInsumo
        SET stock_actual = stock_actual - NEW.cantidad
        WHERE id = NEW.articulo_id; -- Asumiendo que NEW.articulo_id es de un Insumo
    -- END IF;
END;

-- 2. ValidarStockAntesDeInsertarDetallePedido
CREATE TRIGGER ValidarStockAntesDeInsertarDetallePedido
BEFORE INSERT ON DetallePedido
FOR EACH ROW
BEGIN
    IF NOT VerificarStockDisponible(NEW.articulo_id, NEW.cantidad) THEN
        SIGNAL SQLSTATE '45000' -- Código de error personalizado
        SET MESSAGE_TEXT = 'Stock no disponible para el artículo solicitado o cantidad insuficiente.';
    END IF;
END;

-- 3. ActualizarFechaModificacionPedido
CREATE TRIGGER ActualizarFechaModificacionPedido
BEFORE UPDATE ON Pedido
FOR EACH ROW
BEGIN
    -- Solo actualiza si hay cambios reales en campos monitoreados, excluyendo la propia fecha_modificacion
    IF OLD.estado <> NEW.estado OR
       OLD.total <> NEW.total OR
       OLD.hora_estimada_finalizacion <> NEW.hora_estimada_finalizacion OR
       OLD.tipo_envio <> NEW.tipo_envio OR
       OLD.forma_pago <> NEW.forma_pago
       -- Añadir otros campos relevantes
    THEN
        SET NEW.fecha_modificacion = NOW();
    END IF;
END;

-- Vistas

-- 1. VistaPedidosDetalladosClientes
CREATE VIEW VistaPedidosDetalladosClientes AS
SELECT
    p.id AS pedido_id,
    p.fecha_pedido,
    p.hora_estimada_finalizacion,
    p.total AS pedido_total,
    p.estado AS pedido_estado,
    p.tipo_envio,
    p.forma_pago,
    c.id AS cliente_id,
    u.nombre AS cliente_nombre,
    u.apellido AS cliente_apellido,
    u.email AS cliente_email,
    u.telefono AS cliente_telefono,
    dom.calle AS domicilio_calle,
    dom.numero AS domicilio_numero,
    loc_dom.denominacion AS domicilio_localidad,
    dp.id AS detalle_pedido_id,
    a.denominacion AS articulo_denominacion,
    dp.cantidad AS articulo_cantidad,
    (dp.sub_total / dp.cantidad) AS articulo_precio_unitario, -- Precio al momento de la compra
    dp.sub_total AS articulo_subtotal,
    f.id AS factura_id,
    f.fecha_facturacion
FROM Pedido p
JOIN Cliente c ON p.cliente_id = c.id
JOIN Usuario u ON c.usuario_id = u.id
LEFT JOIN Domicilio dom ON p.domicilio_entrega_id = dom.id
LEFT JOIN Localidad loc_dom ON dom.localidad_id = loc_dom.id
JOIN DetallePedido dp ON p.id = dp.pedido_id
JOIN Articulo a ON dp.articulo_id = a.id
LEFT JOIN Factura f ON p.id = f.pedido_id
WHERE p.fecha_baja IS NULL AND dp.fecha_baja IS NULL AND c.fecha_baja IS NULL AND u.fecha_baja IS NULL;

-- 2. VistaArticulosConDetallesCompletos
CREATE VIEW VistaArticulosConDetallesCompletos AS
SELECT
    a.id AS articulo_id,
    a.denominacion AS articulo_denominacion,
    a.descripcion,
    a.precio_venta,
    -- Diferenciar tipo de artículo basado en existencia en ArticuloInsumo o ArticuloManufacturado
    CASE
        WHEN ai.id IS NOT NULL THEN 'INSUMO'
        WHEN am.id IS NOT NULL THEN 'MANUFACTURADO'
        ELSE 'DESCONOCIDO'
    END AS tipo_articulo,
    COALESCE(ai.stock_actual, 0) AS stock_actual_insumo, -- Stock real para insumo
    -- Para manufacturado, el stock se calcula en base a componentes, aquí se muestra tiempo estimado
    am.tiempo_estimado_minutos,
    a.costo,
    cat.denominacion AS categoria_denominacion,
    um.denominacion AS unidad_medida_denominacion,
    (SELECT GROUP_CONCAT(ia.url SEPARATOR ', ') FROM ImagenArticulo ia WHERE ia.articulo_id = a.id AND ia.fecha_baja IS NULL) AS imagenes_urls,
    am_detalle.descripcion_componentes
FROM Articulo a
LEFT JOIN ArticuloInsumo ai ON a.id = ai.id
LEFT JOIN ArticuloManufacturado am ON a.id = am.id
LEFT JOIN Categoria cat ON a.categoria_id = cat.id
LEFT JOIN UnidadMedida um ON a.unidad_medida_id = um.id
LEFT JOIN (
    SELECT
        amd.articulo_manufacturado_id,
        GROUP_CONCAT(CONCAT(a_comp.denominacion, ' (', amd.cantidad, ' ', um_comp.denominacion, ')') SEPARATOR '; ') AS descripcion_componentes
    FROM ArticuloManufacturadoDetalle amd
    JOIN ArticuloInsumo ai_comp ON amd.articulo_insumo_id = ai_comp.id
    JOIN Articulo a_comp ON ai_comp.id = a_comp.id
    JOIN UnidadMedida um_comp ON a_comp.unidad_medida_id = um_comp.id
    WHERE amd.fecha_baja IS NULL AND ai_comp.fecha_baja IS NULL AND a_comp.fecha_baja IS NULL
    GROUP BY amd.articulo_manufacturado_id
) am_detalle ON am.id = am_detalle.articulo_manufacturado_id
WHERE a.fecha_baja IS NULL;
