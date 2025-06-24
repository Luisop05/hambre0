# Sistema Hambre Cero

Sistema de gestión de donaciones de alimentos para combatir el hambre en la comunidad.

## 1. Modelo Relacional Normalizado

### Entidades Principales

1. **Donante** (3FN)
   - `id_donante` (PK)
   - `nombre`
   - `tipo_documento`
   - `numero_documento` (Único)
   - `email`
   - `telefono`
   - `total_donaciones` (Derivado)
   - `total_calorias_donadas` (Derivado)

2. **Receptor** (3FN)
   - `id_receptor` (PK)
   - `nombre`
   - `tipo_documento`
   - `numero_documento` (Único)
   - `direccion`
   - `telefono`
   - `email`
   - `id_zona` (FK)

3. **Donacion** (3FN)
   - `id_donacion` (PK)
   - `id_donante` (FK)
   - `id_receptor` (FK)
   - `fecha`
   - `estado`
   - `observaciones`

4. **DetalleDonacion** (3FN)
   - `id_detalle` (PK)
   - `id_donacion` (FK)
   - `nombre_alimento`
   - `cantidad`
   - `unidad_medida`
   - `calorias_totales`

5. **ZonaEntrega** (3FN)
   - `id_zona` (PK)
   - `nombre`
   - `descripcion`

### Normalización Aplicada

1. **Primera Forma Normal (1FN)**
   - Todas las tablas tienen una clave primaria
   - No hay grupos repetitivos
   - Todos los atributos son atómicos

2. **Segunda Forma Normal (2FN)**
   - Cumple 1FN
   - Todos los atributos no clave dependen de toda la clave primaria

3. **Tercera Forma Normal (3FN)**
   - Cumple 2FN
   - No hay dependencias transitivas
   - Los campos derivados se calculan mediante triggers o procedimientos

## 2. Interfaz de Usuario y CRUD

### Operaciones CRUD Implementadas

1. **Donantes**
   - Crear: Registro de nuevos donantes con validación de documento único
   - Leer: Lista paginada con búsqueda por nombre
   - Actualizar: Modificación de datos con validación
   - Eliminar: Eliminación lógica preservando integridad referencial

2. **Receptores**
   - CRUD completo con asignación de zonas
   - Validación de documentos únicos
   - Gestión de contacto

3. **Donaciones**
   - Registro mediante procedimiento almacenado transaccional
   - Seguimiento de estado
   - Cálculo automático de estadísticas

4. **Zonas de Entrega**
   - Gestión completa de zonas
   - Asignación de receptores
   - Reportes por zona

### Procedimiento Almacenado Principal

`sp_registrar_donacion_completa`: Procedimiento transaccional que:

1. Registra la donación principal
2. Inserta los detalles de alimentos
3. Actualiza estadísticas del donante
4. Maneja errores y rollback

```sql
DELIMITER //
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
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_mensaje = 'Error al registrar la donación';
    END;

    START TRANSACTION;
    
    -- Lógica de inserción y actualización
    -- Ver procedimiento_almacenado.sql para detalles completos
    
    COMMIT;
END //
DELIMITER ;
```

### Justificación del Procedimiento

1. **Integridad de Datos**: Garantiza que todos los registros relacionados se insertan o ninguno
2. **Consistencia**: Mantiene las estadísticas del donante actualizadas
3. **Eficiencia**: Reduce múltiples llamadas a la base de datos
4. **Manejo de Errores**: Implementa rollback automático en caso de fallos

## Configuración del Proyecto

### Requisitos Previos

- Java JDK 11 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

### Pasos de Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/Luisop05/hambre0.git
cd hambre0
```

2. Configurar la base de datos:
```sql
CREATE DATABASE hambre_cero;
```

3. Configurar `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hambre_cero
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

4. Importar esquema y procedimientos:
```bash
mysql -u tu_usuario -p hambre_cero < src/main/resources/schema.sql
mysql -u tu_usuario -p hambre_cero < src/main/resources/procedimiento_almacenado.sql
```

5. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: http://localhost:8080

## Tecnologías Utilizadas

- **Backend**: Spring Boot 2.7.14
- **Base de Datos**: MySQL 8.0
- **Frontend**: Thymeleaf + Bootstrap 5
- **ORM**: Spring Data JPA
- **Build**: Maven

## Licencia

Sistema Hambre Cero © 2025 