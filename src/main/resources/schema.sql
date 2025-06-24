-- Script de creación de base de datos para Sistema Hambre Cero
-- Base de datos normalizada hasta 3FN

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS hambre_cero;
USE hambre_cero;

-- Tabla: zona_entrega
CREATE TABLE zona_entrega (
    id_zona INT PRIMARY KEY AUTO_INCREMENT,
    nombre_zona VARCHAR(100) NOT NULL,
    ciudad VARCHAR(50) NOT NULL,
    INDEX idx_ciudad (ciudad)
);

-- Tabla: donante
CREATE TABLE donante (
    id_donante INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefono VARCHAR(20),
    total_donaciones INT DEFAULT 0,
    total_calorias_donadas DECIMAL(10,2) DEFAULT 0,
    INDEX idx_documento (numero_documento),
    INDEX idx_nombre (nombre)
);

-- Tabla: receptor
CREATE TABLE receptor (
    id_receptor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100),
    id_zona_entrega INT NOT NULL,
    FOREIGN KEY (id_zona_entrega) REFERENCES zona_entrega(id_zona),
    INDEX idx_documento (numero_documento),
    INDEX idx_zona (id_zona_entrega)
);

-- Tabla: donacion
CREATE TABLE donacion (
    id_donacion INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    id_donante INT NOT NULL,
    id_receptor INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
    observaciones VARCHAR(255),
    FOREIGN KEY (id_donante) REFERENCES donante(id_donante),
    FOREIGN KEY (id_receptor) REFERENCES receptor(id_receptor),
    INDEX idx_fecha (fecha),
    INDEX idx_estado (estado),
    INDEX idx_donante (id_donante),
    INDEX idx_receptor (id_receptor)
);

-- Tabla: detalle_donacion
CREATE TABLE detalle_donacion (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_donacion INT NOT NULL,
    nombre_alimento VARCHAR(100) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    unidad_medida VARCHAR(20) NOT NULL,
    calorias_totales DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_donacion) REFERENCES donacion(id_donacion) ON DELETE CASCADE,
    INDEX idx_donacion (id_donacion)
);

-- Insertar datos de ejemplo

-- Zonas de entrega
INSERT INTO zona_entrega (nombre_zona, ciudad) VALUES 
('Centro', 'Bogotá'),
('Norte', 'Bogotá'),
('Sur', 'Bogotá'),
('Suba', 'Bogotá'),
('Kennedy', 'Bogotá');

-- Donantes
INSERT INTO donante (nombre, tipo_documento, numero_documento, email, telefono) VALUES 
('Fundación Alimentar', 'NIT', '900123456-7', 'contacto@alimentar.org', '3001234567'),
('Juan Pérez', 'CC', '1234567890', 'juan.perez@email.com', '3109876543'),
('Supermercados La Canasta', 'NIT', '900987654-3', 'donaciones@lacanasta.com', '3157654321');

-- Receptores
INSERT INTO receptor (nombre, tipo_documento, numero_documento, direccion, telefono, email, id_zona_entrega) VALUES 
('Comedor Comunitario Centro', 'NIT', '800123456-1', 'Calle 1 #10-15', '3001112233', 'comedor.centro@email.com', 1),
('María García', 'CC', '9876543210', 'Carrera 15 #45-67', '3154445566', 'maria.garcia@email.com', 2),
('Hogar Infantil Los Angelitos', 'NIT', '800456789-2', 'Avenida 30 #20-25', '3207778899', 'hogar.angelitos@email.com', 3);

-- Donaciones
INSERT INTO donacion (fecha, id_donante, id_receptor, estado, observaciones) VALUES 
(CURDATE(), 1, 1, 'Entregado', 'Donación mensual de alimentos no perecederos'),
(CURDATE() - INTERVAL 1 DAY, 2, 2, 'Pendiente', 'Donación de frutas y verduras'),
(CURDATE() - INTERVAL 2 DAY, 3, 3, 'Entregado', 'Donación semanal del supermercado');

-- Detalles de donación
INSERT INTO detalle_donacion (id_donacion, nombre_alimento, cantidad, unidad_medida, calorias_totales) VALUES 
(1, 'Arroz', 50, 'kg', 18250),
(1, 'Frijoles', 30, 'kg', 10200),
(2, 'Manzanas', 20, 'kg', 1040),
(2, 'Bananos', 15, 'kg', 1335),
(3, 'Leche', 50, 'l', 3100),
(3, 'Pan', 100, 'u', 26500);

-- Vista útil para reportes
CREATE VIEW v_donaciones_completas AS
SELECT 
    d.id_donacion,
    d.fecha,
    don.nombre AS donante,
    rec.nombre AS receptor,
    ze.nombre_zona,
    ze.ciudad,
    d.estado,
    d.observaciones,
    COUNT(dd.id_detalle) AS cantidad_items,
    SUM(dd.calorias_totales) AS total_calorias
FROM donacion d
INNER JOIN donante don ON d.id_donante = don.id_donante
INNER JOIN receptor rec ON d.id_receptor = rec.id_receptor
INNER JOIN zona_entrega ze ON rec.id_zona_entrega = ze.id_zona
LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion
GROUP BY d.id_donacion; 