# 📊 Modelo Entidad-Relación - Sistema Hambre Cero

## Diagrama ER (Representación Textual)

```
┌─────────────────┐         ┌──────────────────┐         ┌──────────────────┐
│    DONANTE      │         │    DONACION      │         │    RECEPTOR      │
├─────────────────┤         ├──────────────────┤         ├──────────────────┤
│PK id_donante    │←──1:N──→│PK id_donacion    │←──N:1──→│PK id_receptor    │
│   nombre        │         │FK id_donante     │         │   nombre         │
│   tipo_documento│         │FK id_receptor    │         │   tipo_documento │
│UK numero_doc    │         │   fecha          │         │UK numero_doc     │
│   email         │         │   estado         │         │FK id_zona_entrega│
│   telefono      │         │   observaciones  │         └──────────────────┘
│   total_donac   │         └──────────────────┘                 │ N
│   total_calorias│                   │ 1                        │
└─────────────────┘                   │                          │ 1
                                      │ N                        ↓
                          ┌───────────────────────┐      ┌──────────────────┐
                          │  DETALLE_DONACION     │      │  ZONA_ENTREGA    │
                          ├───────────────────────┤      ├──────────────────┤
                          │PK id_detalle          │      │PK id_zona        │
                          │FK id_donacion         │      │   nombre_zona    │
                          │   nombre_alimento     │      │   ciudad         │
                          │   cantidad            │      └──────────────────┘
                          │   unidad_medida       │
                          │   calorias_totales    │
                          └───────────────────────┘
```

## Entidades y Atributos

### 1. DONANTE
- **id_donante** (PK): Identificador único
- **nombre**: Nombre completo del donante
- **tipo_documento**: Tipo de documento (CC, NIT, etc.)
- **numero_documento** (UK): Número único de documento
- **email**: Correo electrónico
- **telefono**: Número de teléfono
- **total_donaciones**: Contador de donaciones realizadas
- **total_calorias_donadas**: Suma total de calorías donadas

### 2. RECEPTOR
- **id_receptor** (PK): Identificador único
- **nombre**: Nombre del receptor
- **tipo_documento**: Tipo de documento
- **numero_documento** (UK): Número único de documento
- **id_zona_entrega** (FK): Referencia a la zona de entrega

### 3. ZONA_ENTREGA
- **id_zona** (PK): Identificador único
- **nombre_zona**: Nombre de la zona
- **ciudad**: Ciudad a la que pertenece

### 4. DONACION
- **id_donacion** (PK): Identificador único
- **fecha**: Fecha de la donación
- **id_donante** (FK): Referencia al donante
- **id_receptor** (FK): Referencia al receptor
- **estado**: Estado de la donación (Pendiente/Entregado)
- **observaciones**: Comentarios adicionales

### 5. DETALLE_DONACION
- **id_detalle** (PK): Identificador único
- **id_donacion** (FK): Referencia a la donación
- **nombre_alimento**: Nombre del alimento donado
- **cantidad**: Cantidad donada
- **unidad_medida**: Unidad de medida (kg, l, etc.)
- **calorias_totales**: Total de calorías del alimento

## Relaciones

1. **DONANTE ← DONACION**: Un donante puede realizar muchas donaciones (1:N)
2. **RECEPTOR ← DONACION**: Un receptor puede recibir muchas donaciones (1:N)
3. **ZONA_ENTREGA ← RECEPTOR**: Una zona puede tener muchos receptores (1:N)
4. **DONACION ← DETALLE_DONACION**: Una donación puede tener muchos detalles (1:N)

## Normalización

### Primera Forma Normal (1FN)
✅ Todas las columnas contienen valores atómicos
✅ No hay grupos repetitivos
✅ Cada tabla tiene una clave primaria

### Segunda Forma Normal (2FN)
✅ Cumple con 1FN
✅ Todos los atributos no clave dependen completamente de la clave primaria
✅ No hay dependencias parciales

### Tercera Forma Normal (3FN)
✅ Cumple con 2FN
✅ No hay dependencias transitivas
✅ Todos los atributos dependen directamente de la clave primaria

## Justificación del Diseño

1. **Separación de entidades**: Cada entidad representa un concepto único del dominio
2. **Integridad referencial**: Las FK garantizan consistencia entre tablas
3. **Escalabilidad**: El diseño permite agregar nuevos atributos sin afectar la estructura
4. **Eficiencia**: Los índices en campos clave mejoran el rendimiento de las consultas
5. **Flexibilidad**: El modelo permite múltiples detalles por donación 