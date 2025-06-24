# Sistema Hambre Cero 🍽️

Sistema de gestión de donaciones de alimentos desarrollado con Spring Boot, JPA y MySQL.

## 📋 Descripción

Este proyecto es un sistema web completo para gestionar donaciones de alimentos, conectando donantes con receptores a través de un sistema eficiente de registro y seguimiento. Implementa todas las operaciones CRUD, procedimientos almacenados y transacciones.

## 🚀 Tecnologías Utilizadas

- **Backend**: Spring Boot 2.7.14
- **ORM**: Spring Data JPA / Hibernate
- **Base de Datos**: MySQL 8.0
- **Frontend**: Thymeleaf + Bootstrap 5
- **Build**: Maven
- **Java**: 11

## 📊 Modelo de Base de Datos

El sistema está construido sobre un modelo relacional normalizado hasta 3FN con las siguientes entidades:

- **donante**: Personas o empresas que realizan donaciones
- **receptor**: Beneficiarios de las donaciones
- **donacion**: Registro de cada donación realizada
- **detalle_donacion**: Detalles de alimentos en cada donación
- **zona_entrega**: Zonas geográficas de distribución

## ⚙️ Configuración del Proyecto

### 1. Prerrequisitos

- JDK 11 o superior
- MySQL 8.0
- Maven 3.6+

### 2. Configuración de Base de Datos

1. Crear la base de datos:
```sql
CREATE DATABASE hambre_cero;
```

2. Ejecutar el script de procedimientos almacenados ubicado en:
```
src/main/resources/procedimiento_almacenado.sql
```

### 3. Configuración de la Aplicación

Actualizar las credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 4. Ejecutar la Aplicación

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en: http://localhost:8080

## 🔧 Funcionalidades Implementadas

### CRUD Completo
- ✅ **Donantes**: Crear, Leer, Actualizar, Eliminar
- ✅ **Receptores**: Gestión completa
- ✅ **Donaciones**: Registro con múltiples detalles
- ✅ **Zonas de Entrega**: Administración de zonas

### Procedimientos Almacenados
- ✅ `sp_registrar_donacion_completa`: Registra donación con transacción
- ✅ `sp_reporte_donaciones_zona`: Genera reportes por zona y fecha

### Consultas Avanzadas
- ✅ JOINs múltiples
- ✅ Agregaciones y estadísticas
- ✅ Consultas con parámetros

### Interfaz Web
- ✅ Dashboard con estadísticas en tiempo real
- ✅ Formularios validados
- ✅ Diseño responsive con Bootstrap
- ✅ Alertas y mensajes de feedback

## 📁 Estructura del Proyecto

```
hambre0/
├── src/
│   └── main/
│       ├── java/com/hambrecero/
│       │   ├── controller/     # Controladores MVC
│       │   ├── entity/         # Entidades JPA
│       │   ├── repository/     # Repositorios JPA
│       │   ├── service/        # Lógica de negocio
│       │   └── HambreCeroApplication.java
│       └── resources/
│           ├── templates/      # Vistas Thymeleaf
│           ├── static/         # CSS, JS
│           └── application.properties
└── pom.xml
```

## 🎯 Endpoints Principales

- `/` - Página principal con estadísticas
- `/donantes` - Gestión de donantes
- `/donaciones` - Gestión de donaciones
- `/donaciones/nueva` - Registrar nueva donación
- `/reportes` - Visualización de reportes

## 👥 Equipo de Desarrollo

- Proyecto desarrollado para la clase de Bases de Datos
- Implementa todos los requisitos solicitados:
  - Modelo normalizado
  - CRUD completo
  - Procedimientos almacenados
  - Transacciones
  - Interfaz web moderna

## 📝 Notas Adicionales

- El sistema maneja transacciones automáticamente con Spring
- Los procedimientos almacenados están integrados con JPA
- La validación se realiza tanto en frontend como backend
- El sistema es escalable y mantenible

## 🎬 Video de Demostración

Para la sustentación, grabar un video mostrando:
1. Modelo de base de datos normalizado
2. Operaciones CRUD funcionando
3. Ejecución de procedimientos almacenados
4. Explicación de las herramientas utilizadas
5. Justificación del procedimiento almacenado

---
Sistema Hambre Cero © 2025 