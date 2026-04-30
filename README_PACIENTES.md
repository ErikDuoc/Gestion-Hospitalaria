# Microservicio de Pacientes (Patient Service)

## Descripción

Microservicio responsable de administrar la información personal, previsional y de contacto de los pacientes del sistema de gestión hospitalaria.

## Configuración Previa

### 1. Base de Datos MySQL

Antes de ejecutar la aplicación, asegúrate de tener MySQL instalado y crear la base de datos:

```sql
CREATE DATABASE db_patients_data;
```

### 2. Configuración de Credenciales

Edita `src/main/resources/application.yaml` con tus credenciales de MySQL:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_patients_data
    username: root
    password: tu_password_aqui
```

## Estructura del Proyecto

```
com.equeirolo.gestion_hospitalaria/
├── entity/
│   └── Paciente.java              # Entidad JPA para pacientes
├── repository/
│   └── PacienteRepository.java    # Acceso a datos
├── service/
│   └── PacienteService.java       # Lógica de negocio
├── controller/
│   └── PacienteController.java    # Endpoints REST
├── dto/
│   ├── CrearPacienteRequest.java  # DTO para crear paciente
│   ├── ActualizarPacienteRequest.java  # DTO para actualizar
│   └── PacienteResponse.java      # DTO de respuesta
└── exception/
    └── GlobalExceptionHandler.java # Manejo global de errores
```

## Entidades

### Paciente
- **id**: Identificador único (generado auto)
- **rut**: Único, identificador de personas (máx 12 caracteres)
- **nombre**: Nombre del paciente
- **apellido**: Apellido del paciente
- **fechaNacimiento**: Fecha de nacimiento
- **genero**: Género (M, F, Otro)
- **email**: Email único del paciente
- **previsionId**: Identificador de previsión (FONASA, ISAPRE, etc.)
- **telefono**: Número de teléfono
- **direccion**: Dirección residencial
- **activo**: Estado del paciente (activo/inactivo)

## Endpoints REST

### 1. Registrar Nuevo Paciente

**Endpoint**: `POST /api/patients`

**Descripción**: Registra un nuevo paciente en el sistema.

**Request Body**:
```json
{
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan@example.com",
  "previsionId": "FONASA",
  "telefono": "123456789",
  "direccion": "Calle 123, Apartment 4B"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan@example.com",
  "previsionId": "FONASA",
  "telefono": "123456789",
  "direccion": "Calle 123, Apartment 4B",
  "activo": true
}
```

**Códigos de Error**:
- `400 Bad Request`: Datos inválidos o incompletos
- `409 Conflict`: RUT o email ya registrado

---

### 2. Obtener Perfil Completo

**Endpoint**: `GET /api/patients/{id}`

**Descripción**: Obtiene el perfil completo de un paciente.

**Parámetros Path**:
- `id` (Long): ID del paciente

**Response** (200 OK):
```json
{
  "id": 1,
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan@example.com",
  "previsionId": "FONASA",
  "telefono": "123456789",
  "direccion": "Calle 123, Apartment 4B",
  "activo": true
}
```

**Códigos de Error**:
- `404 Not Found`: Paciente no encontrado

---

### 3. Actualizar Datos de Contacto

**Endpoint**: `PUT /api/patients/{id}`

**Descripción**: Actualiza datos de contacto de un paciente (email, teléfono, dirección).

**Parámetros Path**:
- `id` (Long): ID del paciente

**Request Body**:
```json
{
  "email": "newemail@example.com",
  "telefono": "987654321",
  "direccion": "Nueva Calle 456"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "newemail@example.com",
  "previsionId": "FONASA",
  "telefono": "987654321",
  "direccion": "Nueva Calle 456",
  "activo": true
}
```

**Códigos de Error**:
- `404 Not Found`: Paciente no encontrado
- `409 Conflict`: Email ya está en uso por otro paciente

## Construcción y Ejecución

### Construir el proyecto
```bash
./mvnw clean package
```

### Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Ejecutar Tests

```bash
./mvnw test
```

## Dependencias Principales

- **Spring Boot 4.0.6**
- **Spring Data JPA** - Acceso a datos
- **MySQL Connector** - Driver JDBC para MySQL
- **Lombok** - Generación de código
- **Spring Validation** - Validación de entrada
- **JUnit 5** - Testing

## Notas Importantes

1. **RUT Único**: Cada paciente debe tener un RUT único en el sistema
2. **Email Único**: Cada paciente debe tener un email único
3. **Auto-incremento de BD**: Las tablas se crean automáticamente con `ddl-auto: update` en Hibernate
4. **Validación**: Los datos se validan en el controlador antes de ser procesados
5. **Manejo de Errores**: Los errores se manejan globalmente con `GlobalExceptionHandler`

## Escalabilidad Futura

Este microservicio está diseñado para extensión:
- Paginación de pacientes
- Búsqueda avanzada (por RUT, email, etc.)
- Soft delete (marcar como inactivo)
- Auditoría (quién modificó qué y cuándo)
- Integración con otros microservicios (citas, historial médico, etc.)

