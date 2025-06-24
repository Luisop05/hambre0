-- Procedimiento almacenado para registrar una donación completa
-- Este procedimiento maneja la transacción completa de crear una donación
-- incluyendo actualizar las estadísticas del donante

DELIMITER //

DROP PROCEDURE IF EXISTS sp_registrar_donacion_completa//

CREATE PROCEDURE sp_registrar_donacion_completa(
    IN p_id_donante INT,
    IN p_id_receptor INT,
    IN p_observaciones VARCHAR(255),
    IN p_nombre_alimento VARCHAR(100),
    IN p_cantidad DECIMAL(10,2),
    IN p_unidad_medida VARCHAR(20),
    IN p_calorias DECIMAL(10,2),
    OUT p_id_donacion INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE v_error INT DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET v_error = 1;
    
    START TRANSACTION;
    
    -- Validar que el donante existe
    IF NOT EXISTS (SELECT 1 FROM donante WHERE id_donante = p_id_donante) THEN
        SET p_mensaje = 'Error: Donante no encontrado';
        SET p_id_donacion = -1;
        ROLLBACK;
    -- Validar que el receptor existe
    ELSEIF NOT EXISTS (SELECT 1 FROM receptor WHERE id_receptor = p_id_receptor) THEN
        SET p_mensaje = 'Error: Receptor no encontrado';
        SET p_id_donacion = -1;
        ROLLBACK;
    ELSE
        -- Insertar la donación
        INSERT INTO donacion (fecha, id_donante, id_receptor, estado, observaciones)
        VALUES (CURDATE(), p_id_donante, p_id_receptor, 'Pendiente', p_observaciones);
        
        IF v_error = 1 THEN
            SET p_mensaje = 'Error al insertar la donación';
            SET p_id_donacion = -1;
            ROLLBACK;
        ELSE
            SET p_id_donacion = LAST_INSERT_ID();
            
            -- Insertar el detalle de donación
            INSERT INTO detalle_donacion (id_donacion, nombre_alimento, cantidad, unidad_medida, calorias_totales)
            VALUES (p_id_donacion, p_nombre_alimento, p_cantidad, p_unidad_medida, p_calorias);
            
            IF v_error = 1 THEN
                SET p_mensaje = 'Error al insertar el detalle de donación';
                SET p_id_donacion = -1;
                ROLLBACK;
            ELSE
                -- Actualizar estadísticas del donante
                UPDATE donante 
                SET total_donaciones = IFNULL(total_donaciones, 0) + 1,
                    total_calorias_donadas = IFNULL(total_calorias_donadas, 0) + p_calorias
                WHERE id_donante = p_id_donante;
                
                IF v_error = 1 THEN
                    SET p_mensaje = 'Error al actualizar estadísticas del donante';
                    SET p_id_donacion = -1;
                    ROLLBACK;
                ELSE
                    SET p_mensaje = CONCAT('Donación registrada exitosamente con ID: ', p_id_donacion);
                    COMMIT;
                END IF;
            END IF;
        END IF;
    END IF;
END//

-- Procedimiento para generar reporte de donaciones por zona
DROP PROCEDURE IF EXISTS sp_reporte_donaciones_zona//

CREATE PROCEDURE sp_reporte_donaciones_zona(
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE
)
BEGIN
    SELECT 
        ze.nombre_zona,
        ze.ciudad,
        COUNT(DISTINCT d.id_donacion) as total_donaciones,
        COUNT(DISTINCT d.id_donante) as donantes_unicos,
        COUNT(DISTINCT r.id_receptor) as receptores_beneficiados,
        COALESCE(SUM(dd.calorias_totales), 0) as total_calorias,
        SUM(CASE WHEN d.estado = 'Entregado' THEN 1 ELSE 0 END) as donaciones_entregadas,
        SUM(CASE WHEN d.estado = 'Pendiente' THEN 1 ELSE 0 END) as donaciones_pendientes
    FROM zona_entrega ze
    LEFT JOIN receptor r ON ze.id_zona = r.id_zona_entrega
    LEFT JOIN donacion d ON r.id_receptor = d.id_receptor 
        AND d.fecha BETWEEN p_fecha_inicio AND p_fecha_fin
    LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion
    GROUP BY ze.id_zona, ze.nombre_zona, ze.ciudad
    ORDER BY total_calorias DESC;
END//

DELIMITER ;

-- Ejemplo de uso del procedimiento
-- CALL sp_registrar_donacion_completa(1, 1, 'Donación de prueba', 'Arroz', 5, 'kg', 1850, @id, @mensaje);
-- SELECT @id, @mensaje; 