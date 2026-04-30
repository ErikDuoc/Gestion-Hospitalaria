-- Script SQL para crear la base de datos y datos de prueba
-- Ejecutar en MySQL

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS db_patients_data;
USE db_patients_data;

-- Crear tabla de pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    prevision_id VARCHAR(100),
    telefono VARCHAR(15),
    direccion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar datos de prueba
INSERT INTO pacientes (rut, nombre, apellido, fecha_nacimiento, genero, email, prevision_id, telefono, direccion, activo)
VALUES
    ('20123456-7', 'Juan', 'Díaz', '1990-05-15', 'M', 'juan.diaz@example.com', 'FONASA', '912345678', 'Calle Principal 123', TRUE),
    ('21987654-3', 'María', 'González', '1985-08-20', 'F', 'maria.gonzalez@example.com', 'ISAPRE', '998765432', 'Paseo Colón 789', TRUE),
    ('22456789-1', 'Carlos', 'Rodríguez', '1992-12-10', 'M', 'carlos.rodriguez@example.com', 'FONASA', '923456789', 'Avenida Central 456', TRUE),
    ('23654321-9', 'Ana', 'Martínez', '1988-03-25', 'F', 'ana.martinez@example.com', 'ISAPRE', '934567890', 'Boulevard Norte 321', TRUE),
    ('24111111-5', 'Roberto', 'López', '1978-07-30', 'M', 'roberto.lopez@example.com', 'FONASA', '945678901', 'Paseo Sur 654', TRUE);

-- Crear índices para optimización
CREATE INDEX idx_rut ON pacientes(rut);
CREATE INDEX idx_email ON pacientes(email);
CREATE INDEX idx_activo ON pacientes(activo);

-- Ver datos insertados
SELECT * FROM pacientes;

-- Contar pacientes
SELECT COUNT(*) as total_pacientes FROM pacientes;

-- Opcional: Ver estructura de la tabla
DESCRIBE pacientes;

