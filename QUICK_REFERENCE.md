# Quick Reference - Gestion Hospitalaria

## 🚀 Start Here

### Para Ejecutar la Aplicación (5 minutos)
```bash
# 1. Asegurar MySQL corriendo
# 2. Crear BD
#    mysql> CREATE DATABASE db_patients_data;

# 3. Descargar y compilar
cd "C:\Users\Erik Queirolo\IdeaProjects\Gestion Hospitalaria"
mvnw clean install

# 4. Ejecutar
mvnw spring-boot:run

# 5. Probar (en otra terminal)
curl http://localhost:8080/api/patients
```

---

## 📚 Documentación Rápida

| Necesito... | Ver Archivo |
|----------|------------|
| Ejecutar el proyecto | SETUP_PACIENTES.md |
| Documentación de API | README_PACIENTES.md |
| Entender la arquitectura | ARQUITECTURA.md |
| Ver qué se implementó | IMPLEMENTACION_SUMMARY.md |
| Verificar tareas | CHECKLIST.md |
| Estado actual | ESTADO_PROYECTO.md |
| Guía para IA | AGENTS.md |

---

## 🔌 Endpoints

### Registrar Paciente
```bash
POST /api/patients
Content-Type: application/json

{
  "rut": "20123456-7",
  "nombre": "Juan",
  "apellido": "Díaz",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "email": "juan@example.com",
  "previsionId": "FONASA",
  "telefono": "912345678",
  "direccion": "Calle 123"
}

Response: 201 Created
{
  "id": 1,
  "rut": "20123456-7",
  ...
}
```

### Obtener Paciente
```bash
GET /api/patients/{id}

Response: 200 OK
{
  "id": 1,
  "rut": "20123456-7",
  ...
}
```

### Actualizar Paciente
```bash
PUT /api/patients/{id}
Content-Type: application/json

{
  "email": "nuevo@example.com",
  "telefono": "987654321",
  "direccion": "Calle Nueva 456"
}

Response: 200 OK
```

---

## 🗂️ Estructura de Directorios

```
src/main/java/com/equeirolo/gestion_hospitalaria/
├── entity/           ← Entidades JPA
├── repository/       ← Acceso a datos
├── service/          ← Lógica de negocio
├── controller/       ← REST endpoints
├── dto/              ← Request/Response
└── exception/        ← Manejo de errores

src/main/resources/
└── application.yaml  ← Configuración (DB, etc)

src/test/java/com/equeirolo/gestion_hospitalaria/
├── service/          ← Tests de servicio
└── controller/       ← Tests de controlador
```

---

## 🔑 Archivos Importantes

| Archivo | Propósito | Líneas |
|---------|-----------|--------|
| `pom.xml` | Dependencias Maven | 119 |
| `application.yaml` | Configuración BD | 16 |
| `entity/Paciente.java` | Modelo JPA | 76 |
| `service/PacienteService.java` | Lógica | 120 |
| `controller/PacienteController.java` | API REST | 50 |
| `exception/GlobalExceptionHandler.java` | Errores | 70 |

---

## 📊 Flujo de Solicitud

```
Cliente HTTP
    ↓
PacienteController @PostMapping /api/patients
    ↓
@Valid → crearPacienteRequest
    ↓
PacienteService.registrarPaciente()
    ├─ findByRut() → validar duplicado
    ├─ findByEmail() → validar duplicado
    └─ save() → persistir
    ↓
convertirAResponse() → PacienteResponse
    ↓
ResponseEntity 201 Created
    ↓
Cliente recibe JSON
```

---

## 🧪 Ejecutar Tests

```bash
# Todos los tests
mvnw test

# Test específico
mvnw test -Dtest=PacienteServiceTest

# Con reporte
mvnw test surefire-report:report
mvnw site

# Sin compilar
mvnw test -DskipCompile
```

---

## 💾 Base de Datos

### Crear BD Manualmente
```sql
CREATE DATABASE db_patients_data;
USE db_patients_data;

-- Ver tabla (auto-creada por Hibernate)
SHOW TABLES;
DESCRIBE pacientes;

-- Insertar dato de prueba
INSERT INTO pacientes (rut, nombre, apellido, fechaNacimiento, genero, email, activo)
VALUES ('20123456-7', 'Juan', 'Díaz', '1990-05-15', 'M', 'juan@example.com', TRUE);
```

### O usar Script SQL
```bash
mysql -u root < init-db-pacientes.sql
```

---

## ⚙️ Configuración

### Cambiar Puerto
```yaml
# application.yaml
server:
  port: 8081
```

### Cambiar Credenciales BD
```yaml
# application.yaml
spring:
  datasource:
    username: tu_usuario
    password: tu_contraseña
```

### Ver SQL Generado
```yaml
# application.yaml
spring:
  jpa:
    show-sql: true  # Muestra SQL en consola
    properties:
      hibernate:
        format_sql: true  # Formatea SQL legible
```

---

## 🐛 Troubleshooting

| Problema | Solución |
|----------|----------|
| Cannot connect to database | Verificar MySQL iniciado |
| Database does not exist | Ejecutar `CREATE DATABASE db_patients_data;` |
| Port 8080 in use | Cambiar puerto en application.yaml |
| Java not found | Instalar Java 21 |
| Maven not working | Usar `.\mvnw.cmd` en lugar de `mvnw` |

---

## 🎯 Códigos HTTP

| Código | Significado | Cuándo |
|--------|-----------|--------|
| 200 | OK | GET, PUT exitosos |
| 201 | Created | POST exitoso |
| 400 | Bad Request | Validación fallida |
| 404 | Not Found | Paciente no existe |
| 409 | Conflict | RUT/email duplicado |
| 500 | Server Error | Error inesperado |

---

## 📦 Dependencias Principales

```xml
<!-- Web -->
spring-boot-starter-webmvc

<!-- Database -->
spring-boot-starter-data-jpa
mysql-connector-java

<!-- Validation -->
spring-boot-starter-validation

<!-- Code Generation -->
lombok

<!-- Development -->
spring-boot-devtools

<!-- Testing -->
spring-boot-starter-webmvc-test
```

---

## 🔐 Validaciones Key

```java
@NotNull      // Requerido, no nulo
@NotBlank     // Requerido, no vacío
@Email        // Email válido
@Size(max=100) // Tamaño máximo
@Unique       // Valor único en BD
@Pattern      // Regex pattern

// En controller
@Valid @RequestBody CrearPacienteRequest request

// Respuesta si falla
HTTP 400 {
  "timestamp": "...",
  "status": 400,
  "error": "Error de validación",
  "errors": {
    "email": "Email must be valid"
  }
}
```

---

## 📝 Convenciones del Proyecto

```
✅ Nombres
- Clases: PascalCase (Paciente, PacienteService)
- Métodos: camelCase (registrarPaciente, obtenerPaciente)
- Variables: camelCase (rut, email, fechaNacimiento)
- Constantes: UPPER_CASE (si existen)

✅ Paquetes
- entity/     ← Entidades JPA
- repository/ ← Data access
- service/    ← Business logic
- controller/ ← REST API
- dto/        ← Transfer objects
- exception/  ← Error handling

✅ Anotaciones
- @Entity    ← Entidad JPA
- @Service   ← Servicio
- @Repository ← Repositorio
- @RestController ← Controlador REST
- @RequestMapping ← URLs
- @Transactional ← Transacciones
- @Valid ← Validación
```

---

## 🚀 Deployment Checklist

- [ ] Código compilado sin errores
- [ ] Tests pasando
- [ ] MySQL configurado
- [ ] application.yaml actualizado
- [ ] Logs configurados
- [ ] Monitoreo enabled (si aplica)
- [ ] Backups configurados (BD)
- [ ] SSL/HTTPS (si necesario)

---

## 💡 Tips

1. **Hot Reload**: DevTools automáticamente recarga cambios
2. **Lombok**: No necesitas writesetter/getter
3. **JPA**: Tablas se crean automáticamente (ddl-auto: update)
4. **DTO**: Siempre usa DTOs en API REST, entidades JPA solo en BD
5. **Service**: Toda lógica de negocio va aquí, no en controller
6. **Tests**: Ejecuta regularmente, mantén coverage alto
7. **Logs**: Usa SLF4J para logging

---

## 📞 Soporte

**Si tienes dudas sobre**:
- **Setup**: Ver SETUP_PACIENTES.md
- **API**: Ver README_PACIENTES.md
- **Arquitectura**: Ver ARQUITECTURA.md
- **Tests**: Ver src/test/java/
- **DB**: Ver init-db-pacientes.sql

---

**¡Listo para trabajar! 🎉**

