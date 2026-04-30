# Arquitectura - Microservicio de Pacientes

## Vista General del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    Cliente (Web/Mobile)                      │
│                    Usuario/Aplicación                        │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP/REST
                     │
┌────────────────────▼────────────────────────────────────────┐
│                 API REST Gateway                              │
│              (Controlador REST)                               │
│          /api/patients/{endpoint}                             │
└────────────────────┬────────────────────────────────────────┘
                     │ Inyección de dependencias
                     │
┌────────────────────▼────────────────────────────────────────┐
│            Capa de Servicios (Business Logic)                │
│              PacienteService                                 │
│  - Validaciones de negocio                                   │
│  - Manejo de transacciones                                   │
│  - Lógica de duplicados                                      │
└────────────────────┬────────────────────────────────────────┘
                     │ JpaRepository
                     │
┌────────────────────▼────────────────────────────────────────┐
│          Capa de Repositorio (Data Access)                   │
│            PacienteRepository                                │
│   - findById()                                               │
│   - findByRut()                                              │
│   - findByEmail()                                            │
│   - save()                                                   │
└────────────────────┬────────────────────────────────────────┘
                     │ Hibernate ORM
                     │
┌────────────────────▼────────────────────────────────────────┐
│         Capa de Persistencia (Base de Datos)                 │
│              MySQL Database                                  │
│            db_patients_data                                  │
│             Tabla: pacientes                                 │
└─────────────────────────────────────────────────────────────┘
```

## Patrones de Diseño Utilizados

### 1. MVC (Model-View-Controller)
- **Model**: Entidad `Paciente` (JPA)
- **View**: DTOs (`PacienteResponse`, `CrearPacienteRequest`)
- **Controller**: `PacienteController` (REST)

### 2. Repository Pattern
El patrón Repository abstrae el acceso a datos:
```
┌─────────────────────────────┐
│   PacienteService           │
│   (Usa la interfaz)         │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│   PacienteRepository        │ (Interfaz)
│   (Contrato de acceso)      │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│   JpaRepository (Spring)    │ (Implementación)
│   (Genera SQL automático)   │
└─────────────────────────────┘
```

### 3. Service Layer Pattern
- **Responsabilidades**:
  - Validaciones de negocio
  - Manejo de transacciones (`@Transactional`)
  - Orquestación de repositorios
  - Conversión de entidades a DTOs

### 4. DTO (Data Transfer Object)
- **Separación de conceptos**: Entidades JPA vs. DTOs de API
- **Ventajas**:
  - Validación de entrada separada
  - Oculta detalles de la BD
  - Permite evolucionar la BD sin cambiar la API
  - Serialización JSON controlada

### 5. Global Exception Handler
- **Centralización**: Todo manejo de errores en un único punto
- **Consistencia**: Respuestas de error uniformes
- **Mantenibilidad**: Cambios globales sin modificar controladores

## Flujo de Solicitud

### POST /api/patients - Registrar Paciente

```
1. Cliente envía JSON
   ↓
2. Spring mapea JSON → CrearPacienteRequest
   ↓
3. @Valid valida anotaciones (@NotBlank, @Email, etc.)
   ↓
4. PacienteController.registrarPaciente() recibe request
   ↓
5. Llama → PacienteService.registrarPaciente()
   ↓
6. Servicio:
   - Valida RUT no exista: findByRut()
   - Valida email no exista: findByEmail()
   - Crea objeto Paciente (builder pattern)
   - Guarda en BD: repository.save()
   ↓
7. Convierte Paciente → PacienteResponse (DTO)
   ↓
8. Retorna ResponseEntity con HTTP 201 Created
   ↓
9. Spring serializa DTO → JSON
   ↓
10. Cliente recibe respuesta JSON
```

### Manejo de Errores

```
Validación fallida (email inválido)
    ↓
@Valid lanza MethodArgumentNotValidException
    ↓
GlobalExceptionHandler.handleValidationExceptions()
    ↓
Retorna HTTP 400 Bad Request con detalles de errores
```

```
Email duplicado
    ↓
PacienteService.registrarPaciente() lanza IllegalArgumentException
    ↓
GlobalExceptionHandler.handleIllegalArgumentException()
    ↓
Retorna HTTP 409 Conflict con mensaje de negocio
```

## Validación en Capas

```
┌─────────────────────────────────┐
│  Validación de Entrada (HTTP)   │
│  - @Valid en controller         │
│  - @NotNull, @Email, etc.       │
│  - HTTP 400 si falla            │
└──────────────┬──────────────────┘
               ↓
┌─────────────────────────────────┐
│  Validación de Negocio          │
│  - Duplicados (RUT, email)      │
│  - Relaciones                   │
│  - HTTP 409 Conflict si falla   │
└──────────────┬──────────────────┘
               ↓
┌─────────────────────────────────┐
│  Persistencia en BD             │
│  - Constraints SQL              │
│  - Triggers (si existen)        │
└─────────────────────────────────┘
```

## Transacciones ACID

```java
@Transactional  // Por defecto: readOnly=false
public PacienteResponse registrarPaciente(...) {
    // 1. Operación 1: findByRut() - READ
    // 2. Operación 2: findByEmail() - READ
    // 3. Operación 3: save() - WRITE
    // Si alguna falla → ROLLBACK de todas
    // Si todas exitosas → COMMIT automático
}

@Transactional(readOnly=true)  // Optimización
public PacienteResponse obtenerPaciente(Long id) {
    // Spring optimiza: no necesita log de cambios
    // Mejor rendimiento en lecturas
}
```

## DTOs vs Entidades

### ¿Cuándo Usar DTOs?

| Aspecto | Entidad JPA | DTO |
|--------|-----------|-----|
| Propósito | Mapeo BD → Objetos | Transferencia de datos |
| Anotaciones | @Entity, @Column | @Valid, validación |
| Exposición | No directamente en API | Siempre en API |
| Cambios BD | Afecta a toda la app | Solo afecta DTOs |
| Serialización | Lazy loading, ciclos | Control total |

### Ejemplo: Conversión

```java
// Recibir entrada
@PostMapping
public ResponseEntity<PacienteResponse> crear(
    @Valid @RequestBody CrearPacienteRequest request  // DTO entrada
) {
    // Procesar con servicio
    PacienteResponse response = service.registrar(request);
    
    // Retornar DTO salida
    return ResponseEntity.status(201).body(response);
}

// En el servicio
Paciente paciente = Paciente.builder()  // Entidad JPA
    .rut(request.getRut())
    .nombre(request.getNombre())
    // ...
    .build();

Paciente guardado = repository.save(paciente);

// Convertir a DTO para respuesta
return convertirAResponse(guardado);
```

## Ejemplo de Tabla SQL Generada

Hibernate auto-genera basado en anotaciones:

```sql
CREATE TABLE pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(20) NOT NULL,
    prevision_id VARCHAR(100),
    telefono VARCHAR(15),
    direccion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT true
);
```

## Dependencias de Inyección

```java
@Service
@RequiredArgsConstructor  // Constructor automático con Lombok
public class PacienteService {
    private final PacienteRepository pacienteRepository;  // Auto-inyectado
}

@RestController
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;  // Auto-inyectado
}
```

## Métricas de Rendimiento (Futuros)

Para optimizar:
- **Índices DB**: RUT, email, activo
- **Paginación**: Evitar `findAll()` sin límite
- **Caché**: Resultados frecuentes
- **Lazy Loading**: Cargar relacionados bajo demanda
- **Monitoreo**: Spring Actuator + Prometheus

---

**Conclusión**: Arquitectura en capas, desacoplada, testeable y mantenible.

