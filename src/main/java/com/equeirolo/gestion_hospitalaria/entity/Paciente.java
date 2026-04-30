package com.equeirolo.gestion_hospitalaria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa un paciente en el sistema de gestión hospitalaria.
 */
@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El RUT no puede estar vacío")
    @Column(name = "rut", unique = true, nullable = false, length = 12)
    private String rut;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @NotNull(message = "La fecha de nacimiento es requerida")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género no puede estar vacío")
    @Column(name = "genero", nullable = false, length = 20)
    private String genero;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "prevision_id", length = 100)
    private String previsionId;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @NotNull
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
}

