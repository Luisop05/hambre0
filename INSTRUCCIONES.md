# 📋 Instrucciones de Instalación y Ejecución

## Pasos para ejecutar el proyecto

### 1. Preparar la Base de Datos

1. Abrir MySQL Workbench o línea de comandos MySQL
2. Ejecutar el script de creación de tablas:
   ```bash
   mysql -u root -p < src/main/resources/schema.sql
   ```
   
3. Ejecutar el script de procedimientos almacenados:
   ```bash
   mysql -u root -p hambre_cero < src/main/resources/procedimiento_almacenado.sql
   ```

### 2. Configurar la Aplicación

1. Abrir el archivo `src/main/resources/application.properties`
2. Actualizar las credenciales de MySQL:
   ```properties
   spring.datasource.username=tu_usuario_mysql
   spring.datasource.password=tu_contraseña_mysql
   ```

### 3. Compilar el Proyecto

```bash
# En la carpeta hambre0/
mvn clean install
```

### 4. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

### 5. Acceder a la Aplicación

Abrir el navegador en: http://localhost:8080

## 🧪 Pruebas del Sistema

### Probar CRUD de Donantes
1. Click en "Donantes" en el menú
2. Click en "Nuevo Donante"
3. Llenar el formulario y guardar
4. Verificar que aparece en la lista
5. Probar editar y eliminar

### Probar Registro de Donación
1. Click en "Donaciones" → "Nueva Donación"
2. Seleccionar donante y receptor
3. Agregar al menos un alimento con sus detalles
4. Guardar la donación
5. Verificar en la lista de donaciones

### Probar Procedimiento Almacenado
1. Ir a "Reportes" → "Demo Procedimiento"
2. Ver la información del procedimiento
3. Ejecutar una prueba del procedimiento
4. Verificar el resultado

## 🎥 Para el Video de Sustentación

### Puntos a demostrar:

1. **Modelo de Base de Datos**
   - Mostrar diagrama ER
   - Explicar normalización (1FN, 2FN, 3FN)
   - Mostrar relaciones entre tablas

2. **CRUD Completo**
   - Crear un nuevo donante
   - Listar todos los donantes
   - Editar información
   - Eliminar un registro
   - Hacer lo mismo con donaciones

3. **Procedimiento Almacenado**
   - Mostrar código del procedimiento `sp_registrar_donacion_completa`
   - Explicar por qué es útil (transacción, actualización automática)
   - Ejecutar el procedimiento desde la aplicación
   - Mostrar que actualiza las estadísticas

4. **Tecnologías Utilizadas**
   - Spring Boot para el backend
   - JPA/Hibernate para ORM
   - Thymeleaf para vistas
   - Bootstrap para UI
   - MySQL como base de datos

5. **Consultas Avanzadas**
   - Mostrar JOINs en los repositorios
   - Mostrar agregaciones en estadísticas
   - Mostrar el reporte por zonas

## 🐛 Solución de Problemas

### Error de conexión a MySQL
- Verificar que MySQL esté ejecutándose
- Verificar puerto (por defecto 3306)
- Verificar credenciales

### Error al compilar
- Verificar versión de Java (debe ser 11 o superior)
- Ejecutar `mvn clean` antes de `mvn install`

### La página no carga
- Verificar que no haya otra aplicación en puerto 8080
- Revisar logs en la consola
- Verificar que todas las tablas estén creadas

## 📞 Soporte

Si encuentran algún problema, verificar:
1. Logs de la aplicación en consola
2. Que la base de datos esté correctamente configurada
3. Que todos los procedimientos almacenados estén creados

¡Éxito con la sustentación! 🚀 