# Guía de Configuración Rápida - Microservicio de Pacientes

## Requisitos Previos

- Java 21 instalado
- MySQL 8.0+ instalado y ejecutándose
- Maven (o usar `./mvnw`)

## Pasos de Setup

### 1. Crear la Base de Datos

Abre tu cliente MySQL y ejecuta:

```sql
CREATE DATABASE db_patients_data;
```

### 2. Configurar Credenciales (si es diferente a root sin contraseña)

Edita el archivo `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_patients_data
    username: root          # ← Cambiar según tu usuario
    password: tu_password   # ← Cambiar según tu contraseña
```

### 3. Descargar Dependencias

```bash
# Windows
mvnw clean install

# Mac/Linux
./mvnw clean install
```

### 4. Ejecutar la Aplicación

```bash
# Windows
mvnw spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

## Probar los Endpoints (con cURL o Postman)

### 1. Crear un Paciente

```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "20123456-7",
    "nombre": "Juan",
    "apellido": "Díaz",
    "fechaNacimiento": "1990-05-15",
    "genero": "M",
    "email": "juan@example.com",
    "previsionId": "FONASA",
    "telefono": "912345678",
    "direccion": "Calle Principal 123"
  }'
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "rut": "20123456-7",
  "nombre": "Juan",
  "apellido": "Díaz",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan@example.com",
  "previsionId": "FONASA",
  "telefono": "912345678",
  "direccion": "Calle Principal 123",
  "activo": true
}
```

---

### 2. Obtener Perfil del Paciente

```bash
curl -X GET http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json"
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "rut": "20123456-7",
  "nombre": "Juan",
  "apellido": "Díaz",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan@example.com",
  "previsionId": "FONASA",
  "telefono": "912345678",
  "direccion": "Calle Principal 123",
  "activo": true
}
```

---

### 3. Actualizar Datos de Contacto

```bash
curl -X PUT http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan.nuevo@example.com",
    "telefono": "987654321",
    "direccion": "Avenida Nueva 456"
  }'
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "rut": "20123456-7",
  "nombre": "Juan",
  "apellido": "Díaz",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan.nuevo@example.com",
  "previsionId": "FONASA",
  "telefono": "987654321",
  "direccion": "Avenida Nueva 456",
  "activo": true
}
```

---

### 4. Crear Otro Paciente (para pruebas)

```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "21987654-3",
    "nombre": "María",
    "apellido": "González",
    "fechaNacimiento": "1985-08-20",
    "genero": "F",
    "email": "maria@example.com",
    "previsionId": "ISAPRE",
    "telefono": "998765432",
    "direccion": "Paseo Colón 789"
  }'
```

## Ejecutar Tests

```bash
# Windows
mvnw test

# Mac/Linux
./mvnw test
```

## Solución de Problemas

### Error: "Cannot connect to database"
- Verifica que MySQL esté ejecutándose
- Comprueba las credenciales en `application.yaml`
- Verifica que la base de datos existe: `SHOW DATABASES;`

### Error: "Port 8080 already in use"
Cambia el puerto en `application.yaml`:
```yaml
server:
  port: 8081
```

### Error: "Unknown database 'db_patients_data'"
Ejecuta en MySQL:
```sql
CREATE DATABASE db_patients_data;
```

## Estructura de Directorios

```
src/
├── main/
│   ├── java/com/equeirolo/gestion_hospitalaria/
│   │   ├── entity/           # Entidades JPA
│   │   ├── repository/       # Repositorios Data
│   │   ├── service/          # Servicios (lógica)
│   │   ├── controller/       # Controladores REST
│   │   ├── dto/              # DTOs (Request/Response)
│   │   ├── exception/        # Manejo de excepciones
│   │   └── GestionHospitalariaApplication.java
│   └── resources/
│       └── application.yaml  # Configuración
└── test/
    └── java/com/equeirolo/gestion_hospitalaria/
        ├── service/          # Tests de servicios
        └── controller/       # Tests de controladores
```

## Códigos de Respuesta HTTP

| Código | Significado |
|--------|------------|
| 201    | Paciente creado exitosamente |
| 200    | Solicitud exitosa |
| 400    | Datos inválidos (validación fallida) |
| 404    | Paciente no encontrado |
| 409    | Conflicto (RUT o email duplicado) |
| 500    | Error interno del servidor |

## Próximos Pasos

Una vez validado este microservicio, se pueden agregar:

1. **Paginación** - `GET /api/patients?page=0&size=10`
2. **Búsqueda avanzada** - `GET /api/patients/search?rut=...&email=...`
3. **Soft Delete** - `DELETE /api/patients/{id}` (marca como inactivo)
4. **Auditoría** - Rastrear cambios (quién, qué, cuándo)
5. **Integración** - Conectar con microservicios de citas, historial médico, etc.

---

**¡Éxito con el microservicio! 🚀**

