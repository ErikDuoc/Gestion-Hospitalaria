# AGENTS.md - Gestion Hospitalaria

## Project Overview

**Gestion Hospitalaria** is a Spring Boot 4.0.6 hospital management system built with Java 21. This is an early-stage application scaffolding with a typical Spring MVC architecture. The project uses Maven for dependency management and builds with the Spring Boot Maven Plugin.

**Key Identifiers:**
- Package root: `com.equeirolo.gestion_hospitalaria`
- Artifact ID: `Gestion_Hospitalaria`
- Main class: `GestionHospitalariaApplication`

## Build & Development Workflow

### Commands
```bash
# Clean and build
./mvnw clean package

# Run application (development)
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build with hot reload (devtools active)
./mvnw spring-boot:run
```

### Key Configuration
- Application name specified in `src/main/resources/application.yaml`
- Spring Boot DevTools enabled for live reload during development
- Lombok configured for compile-time annotation processing (no boilerplate generation needed)

## Architecture Patterns

### Package Structure
```
com.equeirolo.gestion_hospitalaria/
├── GestionHospitalariaApplication    [Main entry point, minimal setup]
├── entity/                           [JPA entities]
│   └── Paciente                      [Patient domain model]
├── repository/                       [Data access layer]
│   └── PacienteRepository           [JPA queries for Patient]
├── service/                          [Business logic layer]
│   └── PacienteService              [Patient business logic]
├── controller/                       [REST endpoints]
│   └── PacienteController           [Patient API]
├── dto/                              [Request/Response objects]
│   ├── CrearPacienteRequest
│   ├── ActualizarPacienteRequest
│   └── PacienteResponse
└── exception/                        [Global error handling]
    └── GlobalExceptionHandler       [Centralized exception handler]
```

### Current Implementation: Patient Microservice

**Status**: ✅ Fully Implemented

The system includes a complete Patient microservice with:
- JPA entity with validation annotations
- Spring Data repository with custom query methods
- Service layer with business logic and transaction management
- REST controller with 3 endpoints (POST, GET, PUT)
- Global exception handler for standardized error responses
- Unit tests for service and controller layers
- MySQL database configuration (db_patients_data)

**Key Files**:
- `src/main/java/.../entity/Paciente.java` - Domain model with @Entity, @Data (Lombok)
- `src/main/java/.../service/PacienteService.java` - Business logic (@Service, @Transactional)
- `src/main/java/.../controller/PacienteController.java` - REST endpoints (@RestController)
- `src/main/resources/application.yaml` - DB configuration
- `README_PACIENTES.md` - Complete API documentation
- `SETUP_PACIENTES.md` - Setup and testing guide

**REST Endpoints**:
```
POST   /api/patients       - Register new patient (201 Created)
GET    /api/patients/{id}  - Get full profile (200 OK)
PUT    /api/patients/{id}  - Update contact info (200 OK)
```

### Expected Future Structure
- `appointment/` - Microservicio de citas/appointments
- `medical-history/` - Historial médico
- `doctor/` - Gestión de doctores
- `authentication/` - Gestión de autenticación

## Development Conventions

### Validation
Spring Validation is configured (`spring-boot-starter-validation`). Use `@Valid` and `@Validated` annotations for input validation:
- Model properties: `@NotNull`, `@Size`, `@Pattern`, etc.
- Controller methods: `@Valid` on parameters
- Response: Standard `MethodArgumentNotValidException` handling (auto-configured)

### Code Generation with Lombok
Lombok is configured with Maven annotation processors. Use annotations instead of manual getters/setters:
- `@Data` - generates getters, setters, toString, equals, hashCode
- `@Getter @Setter` - individual control
- `@RequiredArgsConstructor` - constructor from final fields
- `@Builder` - builder pattern

Example:
```java
@Data
@RequiredArgsConstructor
@Entity
public class Patient {
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    private String name;
    
    @Email
    private String email;
}
```

### Spring Boot Auto-Configuration
The application uses Spring Boot's opinionated defaults. Extend `application.yaml` rather than replacing it. When adding features:
- Database: Add `spring-boot-starter-data-jpa` + driver (mysql, postgresql)
- API documentation: Add `springdoc-openapi` for Swagger/OpenAPI
- Security: Add `spring-boot-starter-security` and configure `SecurityConfig`

## Testing Strategy

- Test location: `src/test/java/com/equeirolo/gestion_hospitalaria/`
- Testing frameworks: JUnit (via `spring-boot-starter-*-test` dependencies)
- Convention: `*Test.java` or `*Tests.java` naming
- Controller testing: Use `@WebMvcTest` + `MockMvc`
- Service testing: Use unit tests without Spring context

Example test structure:
```java
@WebMvcTest(PatientController.class)
class PatientControllerTest {
    @Autowired private MockMvc mockMvc;
    // tests
}
```

## External Dependencies & Integration Points

### Current Dependencies
| Dependency | Purpose | Version |
|---|---|---|
| spring-boot-starter-webmvc | Web framework | 4.0.6 |
| spring-boot-starter-validation | Input validation | 4.0.6 |
| spring-boot-starter-data-jpa | ORM persistence | 4.0.6 |
| mysql-connector-java | MySQL JDBC driver | 8.0.33 |
| spring-boot-devtools | Live reload | 4.0.6 |
| lombok | Code generation | Auto (parent) |

### Expected Additions
- **API Documentation**: SpringDoc OpenAPI (Swagger integration)
- **Logging**: Spring Boot includes SLF4J + Logback (configure via `application.yaml`)
- **Security**: Spring Security for authentication/authorization
- **Caching**: Redis or Caffeine for performance

## Critical Developer Notes

1. **Database configured**: MySQL connection to `db_patients_data` is ready. Create the database locally before running.
2. **Maven wrapper provided** - Always use `./mvnw` (Windows: `mvnw.cmd`) instead of system Maven
3. **Endpoint base path** - Default is `/`, configure with `server.servlet.context-path` in `application.yaml` if needed
4. **Error handling** - Global exception handler at `exception/GlobalExceptionHandler.java` for centralized error responses
5. **Spanish naming convention** - Project uses Spanish identifiers (class names, comments). Maintain consistency when extending.
6. **JPA Auto-Schema**: Tables are auto-created with `ddl-auto: update`. Review schema changes before production.
7. **Validation First**: Input validation is enforced at controller level (@Valid). Invalid requests return 400 with detailed error messages.
8. **Transactional Services**: All service methods marked with `@Transactional` or `@Transactional(readOnly=true)` for query optimization.

## IDE Configuration

- **Java version**: 21 (ensure IDE is set to Java 21)
- **Build system**: Maven (handled by `mvnw` or IDE Maven integration)
- **Hot reload**: DevTools watches `src` directories for changes
- **Annotation processing**: Lombok requires IDE annotation processor enabling (automatic in IntelliJ IDEA)

